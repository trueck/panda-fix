package com.panda.fix;

import com.panda.fix.config.FixConfig;
import com.panda.fix.constant.FixEngineStatus;
import com.panda.fix.constant.FixSessionType;
import com.panda.fix.schedule.StopFixSessionTask;
import com.panda.fix.session.FixSession;
import com.panda.fix.util.FixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FixEngine {

    private static final Logger logger = LoggerFactory.getLogger(FixEngine.class);
    private FixEngineStatus status;
    private FixConfig fixConfig;
    private Map<String, FixSession> fixSessions;

    public FixEngine() {
        status = FixEngineStatus.STOPPED;
        fixConfig = new FixConfig();
        fixConfig.load();
        fixSessions = new HashMap<>();
    }

    public static void main(String[] args) {
        new FixEngine().start();
    }

    public void start() {
        logger.info("Panda Fix starts.");
        startFixSessions();
        addShutdownHook();
        status = FixEngineStatus.STARTED;

    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                shutdown();
            }
        });
    }

    private void shutdown() {
        logger.info("shutting down Panda fix.");
        stopFixSessions();
        logger.info("shutdown done.");
    }

    private void stopFixSessions() {
        fixSessions.forEach((sessionName, fixSession) -> {
            stopFixSession(sessionName);
        });
    }

    private void stopFixSession(String sessionName) {
        new StopFixSessionTask(sessionName).stop();
    }

    private void startFixSessions() {

        fixConfig.getSessionProperties().forEach((sessionName, sessionProp)->{
            FixSession fixSession = createFixSession(sessionName, sessionProp);
            fixSessions.put(sessionName, fixSession);

        });
        fixSessions.forEach((sessionName, fixSession) -> {
            fixSession.start();
        });

    }

    private FixSession createFixSession(String sessionName, Properties sessionProp) {
        FixSession fixSession = new FixSession();
        fixSession.setSessionName(sessionName);
        fixSession.setType(sessionProp.containsKey("remote_port")?FixSessionType.INITIATOR:FixSessionType.ACCEPTOR);
        String port = sessionProp.containsKey("remote_port")?sessionProp.getProperty("remote_port"):sessionProp.getProperty("listener_port");
        fixSession.setPort(Integer.parseInt(port));
        fixSession.setSourceComId(FixUtil.getSourceCompIdFromSessionName(sessionName));
        fixSession.setTargetCompId(FixUtil.getTargetCompIdFromSessionName(sessionName));

        return fixSession;
    }

    public FixEngineStatus getStatus() {
        return status;
    }

    public void stop() {
        logger.info("Panda Fix is stopping");
        status = FixEngineStatus.STOPPED;
    }



}
