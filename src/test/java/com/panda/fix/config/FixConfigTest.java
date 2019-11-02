package com.panda.fix.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FixConfigTest {

    @Test
    public void testLoad() {
        FixConfig fixConfig = new FixConfig();
        fixConfig.load();
        assertEquals(2, fixConfig.getSessionProperties().size());
        assertEquals(1, fixConfig.getTradingSessionProperties().size());
        assertEquals("31000", fixConfig.getSessionProperties().get("NEWFIXENGINE-APPIA").getProperty("remote_port"));
        assertEquals("xxxxx", fixConfig.getSessionProperties().get("NEWFIXENGINE-APPIA").getProperty("remote_host"));
        assertEquals("true", fixConfig.getSessionProperties().get("NEWFIXENGINE2-APPIA").getProperty("auto_eod"));
        assertEquals("32000", fixConfig.getSessionProperties().get("NEWFIXENGINE2-APPIA").getProperty("listener_port"));
        assertEquals("[TEST_TS]", fixConfig.getSessionProperties().get("NEWFIXENGINE2-APPIA").getProperty("trading_session"));
        assertEquals("0 0 19 ? * TUE-SAT", fixConfig.getTradingSessionProperties().get("TEST_TS").getProperty("trading_session_end_time"));
    }
}
