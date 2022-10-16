package com.panda.fix.session;

import com.panda.fix.connector.SessionInitiator;
import com.panda.fix.constant.SessionStatus;
import com.panda.fix.exception.ApplicationException;
import com.panda.fix.util.FixUtil;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixSessionInitiatorConnection extends FixSessionConnection{

    private static final Logger logger = LoggerFactory.getLogger(FixSessionInitiatorConnection.class);

    public FixSessionInitiatorConnection(String sessionName, String host, int port){
        super(sessionName, host, port);
    }

    @Override
    public void start() {
        if(status == null){
            status = SessionStatus.UNKNOWN;
        }
        if(status.equals(SessionStatus.CONNECTED) || status.equals(SessionStatus.CONNECTING) || status.equals(SessionStatus.SUSPENDED)){
            logger.info("session already started: {}", sessionName);
            return;
        }

        startSessionInitiator();
    }

    @Override
    public void stop() {
        if(status.equals(SessionStatus.DISCONNECTED) || status.equals(SessionStatus.DISCONNECTING) || status.equals(SessionStatus.SUSPENDED)){
            logger.info("session already stopped: {}", sessionName);
            return;
        }

        setStatus(SessionStatus.DISCONNECTING);
        try {
            getSessionInitiator().getSessionInitiatorHandler().sendLogoutMessage();

            Thread.sleep(1000);
            getChannelFuture().channel().close();
            getChannelFuture().channel().closeFuture();
            getSessionInitiator().stop();
            setStatus(SessionStatus.DISCONNECTED);
        } catch (Exception e) {
            logger.error("Got error when stopping fix session " + sessionName, e);
        }
    }

    private void startSessionInitiator() {

        setStatus(SessionStatus.CONNECTING);

        final SessionInitiator sessionInitiator = new SessionInitiator(host,
                port,
                FixUtil.getSourceCompIdFromSessionName(sessionName),
                FixUtil.getTargetCompIdFromSessionName(sessionName),
                null);

        try{
            ChannelFuture f = sessionInitiator.start();
            setChannelFuture(f);
            setSessionInitiator(sessionInitiator);
        } catch (Exception e){
            setStatus(SessionStatus.DISCONNECTED);
            throw new ApplicationException("failed to start session:" + sessionName + ", caused by: " + e.getCause().getMessage());
        }
        setStatus(SessionStatus.CONNECTED);
        logger.info("session started: {}", sessionName);
    }


}
