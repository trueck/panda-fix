package com.panda.fix.session;

import com.panda.fix.connector.SessionAcceptor;
import com.panda.fix.connector.SessionInitiator;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FixSessionConnection {

    private static final Logger logger = LoggerFactory.getLogger(FixSessionConnection.class);
    protected String sessionName;
    protected FixSession fixSession;
    protected String status;
    protected ChannelFuture channelFuture;
    protected SessionInitiator sessionInitiator;
    protected SessionAcceptor sessionAcceptor;

    public FixSessionConnection(FixSession fixSession){
        sessionName = fixSession.getSessionName();
        this.fixSession = fixSession;
    }

    public abstract void start();

    public abstract void stop();

    public void send(String message){

    }

    public void addListener(){

    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public FixSession getFixSession() {
        return fixSession;
    }

    public void setFixSession(FixSession fixSession) {
        this.fixSession = fixSession;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public SessionInitiator getSessionInitiator() {
        return sessionInitiator;
    }

    public void setSessionInitiator(SessionInitiator sessionInitiator) {
        this.sessionInitiator = sessionInitiator;
    }

    public SessionAcceptor getSessionAcceptor() {
        return sessionAcceptor;
    }

    public void setSessionAcceptor(SessionAcceptor sessionAcceptor) {
        this.sessionAcceptor = sessionAcceptor;
    }
}
