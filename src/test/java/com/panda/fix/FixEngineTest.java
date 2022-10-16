package com.panda.fix;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FixEngineTest {

    private static final Logger logger = LoggerFactory.getLogger(FixEngineTest.class);

    private FixEngine fixEngine;

    @Before
    public void setup(){
        fixEngine = new FixEngine("conf/fix.ini");
    }


}
