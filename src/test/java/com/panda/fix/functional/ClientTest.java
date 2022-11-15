package com.panda.fix.functional;

import com.panda.fix.FixEngine;
import com.panda.fix.handler.BuySideMessageListener;
import com.panda.fix.handler.SellSideMessageListener;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClientTest {
    private static final Logger logger = LoggerFactory.getLogger(ClientTest.class);

    @Test
    public void testClientSendMsg() throws InterruptedException, IOException {
        FixEngine sellSideFixEngine = new FixEngine("conf/fix-sell.ini");
        FixEngine buySideFixEngine = new FixEngine("conf/fix-buy.ini");



        sellSideFixEngine.start();
        buySideFixEngine.start();

        sellSideFixEngine.getFixSessions().get("APPIA-NEWFIXENGINE").getFixSessionConnection().getSessionAcceptor().getSessionAcceptorHandler()
                .addListener(
                        new SellSideMessageListener(sellSideFixEngine.getFixSessions().get("APPIA-NEWFIXENGINE").getFixSessionConnection())
                );
        buySideFixEngine.getFixSessions().get("NEWFIXENGINE-APPIA").getFixSessionConnection().getSessionInitiator().getSessionInitiatorHandler()
                .addListener(
                        new BuySideMessageListener(buySideFixEngine.getFixSessions().get("NEWFIXENGINE-APPIA").getFixSessionConnection())
                );


        Thread.sleep(3000);
        logger.info("start to send msg.");
        buySideFixEngine.getFixSessions().get("NEWFIXENGINE-APPIA").getFixSessionConnection().send("8=FIX.4.2 9=163 35=D 34=972 49=NEWFIXENGINE 52=20190206-16:25:10.403 56=APPIA 11=141636850670842269979 21=2 38=100 40=1 54=1 55=AAPL 60=20190206-16:25:08.968 207=TO 6000=TEST1234 10=106 ");

        Thread.sleep(10000);

    }
}
