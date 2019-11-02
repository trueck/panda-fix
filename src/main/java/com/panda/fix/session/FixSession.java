package com.panda.fix.session;

import com.panda.fix.constant.FixSessionType;

import java.util.Objects;

public class FixSession {
    private String sessionName;
    private String targetCompId;
    private String sourceComId;
    private FixSessionType type;
    private int port;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FixSession that = (FixSession) o;
        return port == that.port &&
                Objects.equals(sessionName, that.sessionName) &&
                Objects.equals(targetCompId, that.targetCompId) &&
                Objects.equals(sourceComId, that.sourceComId) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionName, targetCompId, sourceComId, type, port);
    }
}
