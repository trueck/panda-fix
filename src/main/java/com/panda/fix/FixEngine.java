package com.panda.fix;

import com.panda.fix.config.FixConfig;
import com.panda.fix.constant.FixEngineStatus;
import com.panda.fix.exception.ApplicationException;
import com.panda.fix.schedule.StopFixSessionTask;
import com.panda.fix.session.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class FixEngine {

    private static final Logger logger = LoggerFactory.getLogger(FixEngine.class);
    private FixEngineStatus status;
    private FixConfig fixConfig;
    private Map<String, FixSession> fixSessions;

    public FixEngine() {
        status = FixEngineStatus.STOPPED;
        fixConfig = new FixConfig();
        fixConfig.load();
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


    }

    public FixEngineStatus getStatus() {
        return status;
    }

    public void stop() {
        logger.info("Panda Fix is stopping");
        status = FixEngineStatus.STOPPED;
    }



}
