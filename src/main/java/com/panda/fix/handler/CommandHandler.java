package com.panda.fix.handler;

import com.panda.fix.FixEngine;
import com.panda.fix.operator.Command;
import com.panda.fix.operator.CommandFactory;
import com.panda.fix.operator.CommandOperator;
import com.panda.fix.operator.CommandResult;
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

    private FixEngine fixEngine;
    private ChannelHandlerContext ctx;

    public CommandHandler(FixEngine fixEngine) {

        this.fixEngine = fixEngine;
    }



    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        this.ctx = ctx;
        ByteBuf in = (ByteBuf) msg;
        String inMsg = in.toString(CharsetUtil.US_ASCII);

        logger.info("Received command: {}", inMsg);

        Command command = CommandFactory.createCommand(inMsg, fixEngine);

        CommandResult result = command.execute();

        ctx.writeAndFlush(Unpooled.copiedBuffer(result.getResult(), CharsetUtil.US_ASCII));

    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
