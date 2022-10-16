package com.panda.fix.handler;

import com.panda.fix.connector.SessionInitiator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class SessionInitiatorHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger logger = LoggerFactory.getLogger(SessionInitiatorHandler.class);

    private SessionData sessionData;
    private ChannelHandlerContext ctx;
    private SessionInitiator sessionInitiator;
    private Consumer<String> stringConsumer;



    public SessionInitiatorHandler(SessionInitiator sessionInitiator, String senderCompId, String targetCompId, Consumer<String> stringConsumer) throws IOException {

        sessionData = new SessionData(senderCompId, targetCompId);
        this.sessionInitiator = sessionInitiator;
        this.stringConsumer = stringConsumer;




    }

    public void sendLogoutMessage() throws IOException{
        sendMessage(sessionData.createLogoutMsg());
        sessionData.setLogout(true);
    }

    private void sendLogin() throws IOException{
        sendMessage(sessionData.createLogonMsg());
        sessionData.setLogout(false);
    }

    private void sendMessage(String message) throws IOException{
        ctx.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.US_ASCII));
        sessionData.writeToOutDataFile(message);
    }

    private void sendHeartbeat(ChannelHandlerContext ctx){
        sessionInitiator.setScheduler(Executors.newScheduledThreadPool(1));
        sessionInitiator.getScheduler().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                String hbMsg = sessionData.createHeartbeatMsg();
                try{
                    SessionInitiatorHandler.this.sendMessage(hbMsg);
                }catch (IOException e){
                    throw new RuntimeException();
                }
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    public void channelActive(final ChannelHandlerContext ctx) throws IOException{
        this.ctx = ctx;
        sendLogin();
    }



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) throws Exception {

        String msg = in.toString((CharsetUtil.US_ASCII));
        logger.info("received: {}", msg);

        if(msg.length() < 50){
            logger.info("ignore the message as the length of the message is less than 50.");
            return;
        }

        sessionData.updateInSeq(sessionData.getSeqFromMessage(msg));
        sessionData.writeToInDataFile(msg);

        if(sessionData.isLoginAck(msg)){
            sendHeartbeat(ctx);
        }
        if(!sessionData.isLogout() && sessionData.isLogoutMsg(msg)){
            sendLogoutMessage();
        }
        if(stringConsumer != null){
            stringConsumer.accept(msg);
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }

    public SessionData getSessionData() {
        return sessionData;
    }

    public void setSessionData(SessionData sessionData) {
        this.sessionData = sessionData;
    }
}
