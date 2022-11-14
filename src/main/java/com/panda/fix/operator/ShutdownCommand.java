package com.panda.fix.operator;

import com.panda.fix.FixEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(NoOpCommand.class);
    private FixEngine fixEngine;

    public ShutdownCommand(FixEngine fixEngine) {
        this.fixEngine = fixEngine;

    }

    @Override
    public CommandResult execute() {
        logger.info("executing command ShutdownCommand");
        this.fixEngine.shutdown();
        return new CommandResult("success");
    }
}
