package com.panda.fix.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoOpCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(NoOpCommand.class);
    @Override
    public CommandResult execute() {
        logger.info("executing command NoOpCommand");
        return new CommandResult("success");
    }
}
