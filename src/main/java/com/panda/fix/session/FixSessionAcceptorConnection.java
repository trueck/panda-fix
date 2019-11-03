package com.panda.fix.session;

import com.panda.fix.connector.SessionAcceptor;
import com.panda.fix.constant.SessionStatus;
import com.panda.fix.exception.ApplicationException;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixSessionAcceptorConnection extends FixSessionConnection{

    private static final Logger logger = LoggerFactory.getLogger(FixSessionAcceptorConnection.class);

    public FixSessionAcceptorConnection(FixSession fixSession) {
        super(fixSession);
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

        startSessionAcceptor();
    }

    private void startSessionAcceptor() {
        String sessionName = fixSession.getSessionName();
        setStatus(SessionStatus.CONNECTING);

        int listenerPort = fixSession.getPort();

        final SessionAcceptor sessionAcceptor = new SessionAcceptor(listenerPort, fixSession.getSourceComId(), fixSession.getTargetCompId(), this);

        try{
            setStatus(SessionStatus.DISCONNECTED);
            ChannelFuture f = sessionAcceptor.start();
            setChannelFuture(f);
            setSessionAcceptor(sessionAcceptor);
        }catch (Exception e){
            throw new ApplicationException("failed to start session:" + sessionName, e);
        }

        logger.info("session started: {}", sessionName);
    }

    @Override
    public void stop() {
        boolean sendLogout = true;
        if(status.equals(SessionStatus.DISCONNECTED) || status.equals(SessionStatus.DISCONNECTING) || status.equals(SessionStatus.SUSPENDED)){
            logger.info("session already stopped: {}", sessionName);
            sendLogout = false;
        }

        setStatus(SessionStatus.DISCONNECTING);
        try {
            if(sendLogout){
                getSessionAcceptor().getSessionAcceptorHandler().sendLogoutMessage();
                Thread.sleep(1000);
            }

            getChannelFuture().channel().closeFuture();
            getSessionAcceptor().stop();
        } catch (Exception e) {
            logger.error("Got error when stopping fix session " + sessionName, e);
        }
    }
}
