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
        fixConfig.getSessionProperties().forEach((sessionName, sessionProp)->{
            FixSession fixSession = createFixSession(sessionName, sessionProp);
            fixSessions.put(sessionName, fixSession);
        });
    }

    private FixSession createFixSession(String sessionName, Properties sessionProp) {
        FixSession fixSession = new FixSession();
        fixSession.setSessionName(sessionName);
        fixSession.setType(sessionProp.containsKey("remote_port")?FixSessionType.INITIATOR:FixSessionType.ACCEPTOR);
        String port = sessionProp.containsKey("remote_port")?sessionProp.getProperty("remote_port"):sessionProp.getProperty("listener_port");
        fixSession.setPort(Integer.parseInt(port));
        fixSession.setHost(sessionProp.getProperty("remote_host"));
        fixSession.setSourceComId(FixUtil.getSourceCompIdFromSessionName(sessionName));
        fixSession.setTargetCompId(FixUtil.getTargetCompIdFromSessionName(sessionName));
        return fixSession;
    }

    public static void main(String[] args) {
        new FixEngine(args[0]).start();
    }


    public void start() {
        logger.info("Panda Fix is starting.");
        startCommandOperator();
        startFixSessions();
        addShutdownHook();
        logger.info("Panda Fix is started.");
    }

    private void startCommandOperator(){
        commandOperator = new CommandOperator(34000, this);
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
            fixSession.stop();
        });
    }

    private void startFixSessions() {
        fixSessions.forEach((sessionName, fixSession) -> {
            try {
                fixSession.start();
            }catch (Exception e){
                logger.error(e.getMessage());
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
