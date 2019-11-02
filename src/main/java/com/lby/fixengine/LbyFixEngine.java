package com.lby.fixengine;

import com.lby.fixengine.config.FixConfig;
import com.lby.fixengine.constant.FixEngineStatus;
import com.lby.fixengine.session.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class LbyFixEngine {

    private static final Logger logger = LoggerFactory.getLogger(LbyFixEngine.class);
    private FixEngineStatus status;
    private FixConfig fixConfig;
    private Map<String, FixSession> sessions;

    public LbyFixEngine(String configPath){
        status = FixEngineStatus.STOPPED;
        fixConfig = new FixConfig();
        fixConfig.load(configPath);
        sessions = fixConfig.createSessions();
    }

    public static void main(String[] args) {
        new LbyFixEngine("").start();
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
