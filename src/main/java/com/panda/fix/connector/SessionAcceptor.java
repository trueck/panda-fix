package com.panda.fix.connector;

import com.panda.fix.constant.SessionStatus;
import com.panda.fix.handler.SessionAcceptorHandler;
import com.panda.fix.session.FixSessionConnection;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledExecutorService;

public class SessionAcceptor {

    private final int port;
    private EventLoopGroup group;
    private ScheduledExecutorService scheduler;
    private final String senderCompId;
    private final String targetCompId;
    private SessionAcceptorHandler sessionAcceptorHandler;
    private FixSessionConnection fixSessionConnection;

    public SessionAcceptor(int port, String senderCompId, String targetCompId, FixSessionConnection fixSessionConnection) {
        this.port = port;
        this.senderCompId = senderCompId;
        this.targetCompId = targetCompId;
        this.fixSessionConnection = fixSessionConnection;
    }

    public ChannelFuture start() throws Exception{
        sessionAcceptorHandler = new SessionAcceptorHandler(this, this.senderCompId, this.targetCompId, fixSessionConnection);
        group = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(sessionAcceptorHandler);
                        }
                    });
            ChannelFuture f = b.bind().sync();

            return f;
        }finally {

        }
    }

    public void stop() throws IOException{
        if(scheduler != null){
            scheduler.shutdown();
        }
        this.sessionAcceptorHandler.getSessionData().getOutDataWriter().close();
        this.sessionAcceptorHandler.getSessionData().getInDataWriter().close();
        group.shutdownGracefully();
    }



    public ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public void setScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    public SessionAcceptorHandler getSessionAcceptorHandler() {
        return sessionAcceptorHandler;
    }

    public void setSessionAcceptorHandler(SessionAcceptorHandler sessionAcceptorHandler) {
        this.sessionAcceptorHandler = sessionAcceptorHandler;
    }
}
