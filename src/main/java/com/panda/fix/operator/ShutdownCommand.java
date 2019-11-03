package com.panda.fix.operator;

import com.panda.fix.FixEngine;

public class ShutdownCommand implements Command {

    private FixEngine fixEngine;

    public ShutdownCommand(FixEngine fixEngine) {
        this.fixEngine = fixEngine;

    }

    @Override
    public void execute() {
        this.fixEngine.shutdown();

    }
}
