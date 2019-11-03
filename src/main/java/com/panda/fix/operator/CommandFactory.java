package com.panda.fix.operator;

import com.panda.fix.FixEngine;

public class CommandFactory {

    public static Command createCommand(String name, FixEngine fixEngine){
        return new ShutdownCommand(fixEngine);
    }
}
