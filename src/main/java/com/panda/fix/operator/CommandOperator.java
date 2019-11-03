package com.panda.fix.operator;

import com.panda.fix.FixEngine;
import com.panda.fix.exception.ApplicationException;
import com.panda.fix.handler.CommandHandler;
import com.panda.fix.handler.SessionAcceptorHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledExecutorService;

public class CommandOperator {

    private static final Logger logger = LoggerFactory.getLogger(CommandOperator.class);
    private final int port;
    private EventLoopGroup group;
    private FixEngine fixEngine;
    private CommandHandler commandHandler;

    public CommandOperator(int port, FixEngine fixEngine) {
        this.port = port;
        this.fixEngine = fixEngine;

    }

    public ChannelFuture start() {
        commandHandler = new CommandHandler(this.fixEngine);
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
        } catch (InterruptedException e) {
            throw new ApplicationException(e);
        }
    }

    public void stop() throws IOException{

        logger.info("stopping command operator.");
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


    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public FixEngine getFixEngine() {
        return fixEngine;
    }

    public void setFixEngine(FixEngine fixEngine) {
        this.fixEngine = fixEngine;
    }
}
