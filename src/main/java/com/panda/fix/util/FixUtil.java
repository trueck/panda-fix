package com.panda.fix.util;

import com.panda.fix.config.FixConfig;
import com.panda.fix.exception.ApplicationException;

import java.io.IOException;

public class FixUtil {

    public static String getSourceCompIdFromSessionName(String sessionName){
        return sessionName.split("-")[0];
    }

    public static String getTargetCompIdFromSessionName(String sessionName){
        return sessionName.split("-")[1];
    }

}
