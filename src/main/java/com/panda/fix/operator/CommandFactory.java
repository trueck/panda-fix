package com.panda.fix.operator;

import com.panda.fix.FixEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandFactory {
    private static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);
    public static Command createCommand(String name, FixEngine fixEngine){
        logger.info("creating command for [{}]", name);
        return switch(name){
            case "list sessions" -> new ListSessionsCommand(fixEngine);
            case "shutdown" -> new ShutdownCommand(fixEngine);
            default -> new NoOpCommand();
        };
    }
}
