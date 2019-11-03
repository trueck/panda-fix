package com.panda.fix.operator;

public class CommandFactory {

    public static Command createCommand(String name){
        return new ShutdownCommand();
    }
}
