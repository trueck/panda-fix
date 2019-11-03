package com.panda.fix.handler;

import com.panda.fix.operator.Command;
import com.panda.fix.operator.CommandFactory;
import com.panda.fix.operator.CommandOperator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommandHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    private CommandOperator commandOperator;
    private ChannelHandlerContext ctx;

    public CommandHandler(CommandOperator commandOperator) throws IOException {

        this.commandOperator = commandOperator;
    }



    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        this.ctx = ctx;
        ByteBuf in = (ByteBuf) msg;
        String inMsg = in.toString(CharsetUtil.US_ASCII);

        logger.info("Received command: {}", inMsg);

        Command command = CommandFactory.createCommand(inMsg);

        command.execute();

    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
