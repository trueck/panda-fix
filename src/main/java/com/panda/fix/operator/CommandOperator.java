package com.panda.fix.operator;

import com.panda.fix.handler.CommandHandler;
import com.panda.fix.handler.SessionAcceptorHandler;
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

public class CommandOperator {

    private final int port;
    private EventLoopGroup group;

    private final String senderCompId;
    private final String targetCompId;
    private CommandHandler commandHandler;

    public CommandOperator(int port, String senderCompId, String targetCompId) {
        this.port = port;
        this.senderCompId = senderCompId;
        this.targetCompId = targetCompId;
    }

    public ChannelFuture start() throws Exception{
        commandHandler = new CommandHandler(this);
        group = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(commandHandler);
                        }
                    });
            ChannelFuture f = b.bind().sync();
            return f;
        }finally {

        }
    }

    public void stop() throws IOException{


        group.shutdownGracefully();
    }


    public int getPort() {
        return port;
    }

    public EventLoopGroup getGroup() {
        return group;
    }

    public void setGroup(EventLoopGroup group) {
        this.group = group;
    }

    public String getSenderCompId() {
        return senderCompId;
    }

    public String getTargetCompId() {
        return targetCompId;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
}
