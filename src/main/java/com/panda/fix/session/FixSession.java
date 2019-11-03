package com.panda.fix.session;

import com.panda.fix.constant.FixSessionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class FixSession {

    public static final Logger logger = LoggerFactory.getLogger(FixSession.class);

    private String sessionName;
    private String targetCompId;
    private String sourceComId;
    private FixSessionType type;
    private int port;
    private String host;
    private FixSessionConnection fixSessionConnection;

    public void start(){
        logger.info("Fix sessoin {} is starting.", sessionName);
        if(type.equals(FixSessionType.INITIATOR)){
            fixSessionConnection = new FixSessionInitializerConnection(this);
        }else{
            fixSessionConnection = new FixSessionAcceptorConnection(this);
        }
        fixSessionConnection.start();
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setTargetCompId(String targetCompId) {
        this.targetCompId = targetCompId;
    }

    public String getTargetCompId() {
        return targetCompId;
    }

    public void setSourceComId(String sourceComId) {
        this.sourceComId = sourceComId;
    }

    public String getSourceComId() {
        return sourceComId;
    }

    public void setType(FixSessionType type) {
        this.type = type;
    }

    public FixSessionType getType() {
        return type;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public FixSessionConnection getFixSessionConnection() {
        return fixSessionConnection;
    }

    public void setFixSessionConnection(FixSessionConnection fixSessionConnection) {
        this.fixSessionConnection = fixSessionConnection;
    }
}
