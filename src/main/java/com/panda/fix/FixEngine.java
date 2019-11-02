package com.panda.fix;

import com.panda.fix.config.FixConfig;
import com.panda.fix.constant.FixEngineStatus;
import com.panda.fix.session.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FixEngine {

    private static final Logger logger = LoggerFactory.getLogger(FixEngine.class);
    private FixEngineStatus status;
    private FixConfig fixConfig;
    private Map<String, FixSession> sessions;

    public FixEngine(String configPath){
        status = FixEngineStatus.STOPPED;
        fixConfig = new FixConfig();
        fixConfig.load(configPath);
        sessions = fixConfig.createSessions();
    }

    public static void main(String[] args) {
        new FixEngine("").start();
    }

    public void start(){
        logger.info("LBY Fix Engine starts.");
        status = FixEngineStatus.STARTED;

    }

    public FixEngineStatus getStatus() {
        return status;
    }

    public void stop() {
        logger.info("LBY Fix Engine stops");
        status = FixEngineStatus.STOPPED;
    }

    public FixConfig getConfig() {
        return fixConfig;
    }

    public Map<String, FixSession> getSessions() {
        return sessions;
    }


}
