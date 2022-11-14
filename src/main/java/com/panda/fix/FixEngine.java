package com.panda.fix;

import com.panda.fix.config.FixConfig;
import com.panda.fix.constant.FixSessionType;
import com.panda.fix.operator.CommandOperator;
import com.panda.fix.session.FixSession;
import com.panda.fix.util.FixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FixEngine {

    private static final Logger logger = LoggerFactory.getLogger(FixEngine.class);

    private FixConfig fixConfig;
    private Map<String, FixSession> fixSessions;
    private CommandOperator commandOperator;

    public FixEngine(String configFile) {
        fixConfig = new FixConfig(configFile);
        fixSessions = new HashMap<>();
        fixConfig.getSessionProperties().forEach(this::createFixSession);
        commandOperator = new CommandOperator(34001, this);
    }


    private void createFixSession(String sessionName, Properties sessionProp) {
        String port = sessionProp.containsKey("remote_port")?sessionProp.getProperty("remote_port"):sessionProp.getProperty("listener_port");
        FixSession fixSession = new FixSession(sessionName,
                FixUtil.getTargetCompIdFromSessionName(sessionName),
                FixUtil.getSourceCompIdFromSessionName(sessionName),
                sessionProp.containsKey("remote_port")?FixSessionType.INITIATOR:FixSessionType.ACCEPTOR,
                sessionProp.getProperty("remote_host"),
                Integer.parseInt(port)
                );
        fixSessions.put(sessionName, fixSession);
    }

    public static void main(String[] args) {
        new FixEngine(args[0]).start();
    }


    public void start() {
        logger.info("Panda Fix is starting.");
        addShutdownHook();
        startCommandOperator();
        startFixSessions();
        logger.info("Panda Fix is started.");
    }

    private void startCommandOperator(){
        commandOperator.start();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                shutdown();
            }
        });
    }

    public void shutdown() {
        logger.info("shutting down Panda fix.");
        stopFixSessions();
        commandOperator.stop();
        logger.info("shutdown done.");
    }

    private void stopFixSessions() {
        fixSessions.forEach((sessionName, fixSession) -> {
            try {
                fixSession.stop();
            }catch (Exception e){
                logger.error("Got error when stopping fix session {}", sessionName, e);
            }
        });
    }

    private void startFixSessions() {
        fixSessions.forEach((sessionName, fixSession) -> {
            try {
                fixSession.start();
            }catch (Exception e){
                logger.error("Got error when starting fix session {}", sessionName, e);
            }
        });
    }

    public FixConfig getFixConfig() {
        return fixConfig;
    }

    public void setFixConfig(FixConfig fixConfig) {
        this.fixConfig = fixConfig;
    }

    public Map<String, FixSession> getFixSessions() {
        return fixSessions;
    }

    public void setFixSessions(Map<String, FixSession> fixSessions) {
        this.fixSessions = fixSessions;
    }

    public CommandOperator getCommandOperator() {
        return commandOperator;
    }

    public void setCommandOperator(CommandOperator commandOperator) {
        this.commandOperator = commandOperator;
    }
}
