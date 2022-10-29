package com.panda.fix;

import com.panda.fix.session.FixSession;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class FixEngineTest {

    private static final Logger logger = LoggerFactory.getLogger(FixEngineTest.class);

    private FixEngine fixEngine;

    @Before
    public void setup(){
        fixEngine = new FixEngine("conf/fix.ini");
    }

    @Test
    public void testStart(){
        fixEngine.getFixSessions().entrySet().stream().forEach(this::replaceFixSessions);
        fixEngine.start();
    }

    private void replaceFixSessions(Map.Entry<String, FixSession> fixSessionEntry) {
        fixSessionEntry.setValue(new DummyFixSession(fixSessionEntry.getValue()));
    }

}
