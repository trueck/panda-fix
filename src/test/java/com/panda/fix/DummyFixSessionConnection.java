package com.panda.fix;

import com.panda.fix.session.FixSessionConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyFixSessionConnection extends FixSessionConnection {

    private static final Logger logger = LoggerFactory.getLogger(DummyFixSessionConnection.class);

    public DummyFixSessionConnection(String sessionName, String host, int port) {
        super(sessionName, host, port);
    }

    @Override
    public void start() {
        logger.info("pretent to start {}", this.getSessionName());
    }

    @Override
    public void stop() {
        logger.info("pretent to stop {}", this.getSessionName());
    }
}
