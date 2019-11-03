package com.panda.fix;

import com.panda.fix.constant.FixEngineStatus;
import com.panda.fix.constant.FixSessionType;
import com.panda.fix.session.FixSession;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class FixEngineTest {

    private static final Logger logger = LoggerFactory.getLogger(FixEngineTest.class);

    private FixEngine fixEngine;

    @Before
    public void setup(){
        fixEngine = new FixEngine();
    }

    @Test
    public void testMain(){
        logger.info("testMain is running.");
    }

    @Test
    public void testStatus() throws IOException {
        fixEngine.setFixSessions(new HashMap<>());
        assertEquals("the status is not correct before start", FixEngineStatus.STOPPED, fixEngine.getStatus());
        fixEngine.start();
        assertEquals("the status is not correct after start", FixEngineStatus.STARTED, fixEngine.getStatus());
        fixEngine.stop();
        assertEquals("the status is not correct after stop", FixEngineStatus.STOPPED, fixEngine.getStatus());
    }


}
