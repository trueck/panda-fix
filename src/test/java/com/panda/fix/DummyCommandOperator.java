package com.panda.fix;

import com.panda.fix.operator.CommandOperator;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyCommandOperator extends CommandOperator {

    private static final Logger logger = LoggerFactory.getLogger(DummyFixSessionConnection.class);


    public DummyCommandOperator(int port, FixEngine fixEngine) {
        super(port, fixEngine);
    }

    @Override
    public ChannelFuture start() {
        logger.info("pretent to start CommandOperator.");
        return null;
    }

    @Override
    public void stop() {
        logger.info("pretent to stop CommandOperator");
    }
}
