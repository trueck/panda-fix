package com.panda.fix;

import com.panda.fix.config.FixConfig;
import com.panda.fix.constant.FixEngineStatus;
import com.panda.fix.schedule.StopFixSessionTask;
import com.panda.fix.session.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FixEngine {

    private static final Logger logger = LoggerFactory.getLogger(FixEngine.class);
    private FixEngineStatus status;

    public FixEngine(){
        status = FixEngineStatus.STOPPED;


    }

    public static void main(String[] args) throws IOException {
        new FixEngine().start();
    }

    public void start() throws IOException {
        logger.info("Panda Fix starts.");
        FixConfig.load();
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
        FixConfig.getFixSessions().forEach((sessionName, fixSession) -> {
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
