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

        final SessionAcceptor sessionAcceptor = new SessionAcceptor(listenerPort, fixSession.getSourceComId(), fixSession.getTargetCompId());

        try{
            ChannelFuture f = sessionAcceptor.start();
            setChannelFuture(f);
            setSessionAcceptor(sessionAcceptor);
        }catch (Exception e){
            setStatus(SessionStatus.DISCONNECTED);
            throw new ApplicationException("failed to start session:" + sessionName, e);
        }
        setStatus(SessionStatus.CONNECTED);
        logger.info("session started: {}", sessionName);
    }
}
