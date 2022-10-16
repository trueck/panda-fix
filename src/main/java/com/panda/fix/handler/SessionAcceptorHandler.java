package com.panda.fix.handler;

import com.panda.fix.connector.SessionAcceptor;
import com.panda.fix.constant.SessionStatus;
import com.panda.fix.session.FixSession;
import com.panda.fix.session.FixSessionConnection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class SessionAcceptorHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(SessionAcceptorHandler.class);
    private SessionData sessionData;
    private SessionAcceptor echoServer;
    private ChannelHandlerContext ctx;
    private FixSessionConnection fixSessionConnection;

    public SessionAcceptorHandler(SessionAcceptor sessionAcceptor, String senderCompId, String targetCompId, FixSessionConnection fixSessionConnection) throws IOException {
        sessionData = new SessionData(senderCompId, targetCompId);
        this.echoServer = sessionAcceptor;
        this.fixSessionConnection = fixSessionConnection;
    }

    public void sendLogoutMessage() throws IOException{
        sendMessage(sessionData.createLogoutMsg());
        sessionData.setLogout(true);
    }

    public void sendMessage(String message) throws IOException{
        ctx.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.US_ASCII));
        sessionData.writeToOutDataFile(message);
    }

    private void sendLoginAck() throws IOException{
        sendMessage(sessionData.createLogonMsg());
        sessionData.setLogout(false);
    }

    private void sendHeartbeat(){
        echoServer.setScheduler(Executors.newScheduledThreadPool(1));
        echoServer.getScheduler().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                String hbMsg = sessionData.createHeartbeatMsg();
                try{
                    SessionAcceptorHandler.this.sendMessage(hbMsg);
                }catch (IOException e){
                    throw new RuntimeException();
                }
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException{
        this.ctx = ctx;
        ByteBuf in = (ByteBuf)msg;
        String inMsg = in.toString(CharsetUtil.US_ASCII);

        logger.info("Received message [{}]", inMsg);
        sessionData.updateInSeq(sessionData.getSeqFromMessage(inMsg));
        sessionData.writeToInDataFile(inMsg);

        if(sessionData.isLogin(inMsg)){
            sendLoginAck();
            sendHeartbeat();
            sessionData.setLogout(false);
            fixSessionConnection.setStatus(SessionStatus.CONNECTED);
        }
        if(!sessionData.isLogout() && sessionData.isLogoutMsg(inMsg)){
            sendLogoutMessage();
        }
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{

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
