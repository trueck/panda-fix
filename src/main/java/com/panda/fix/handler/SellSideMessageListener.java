package com.panda.fix.handler;

import com.panda.fix.session.FixSessionConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SellSideMessageListener implements MessageListener{
    private static final Logger logger = LoggerFactory.getLogger(SellSideMessageListener.class);

    private FixSessionConnection fixSessionConnection;
    public SellSideMessageListener(FixSessionConnection fixSessionConnection){
        this.fixSessionConnection = fixSessionConnection;
    }
    @Override
    public void onMessage(String message) {
        try {
            fixSessionConnection.send("8=FIX.4.2 9=271 35=8 34=974 49=APPIA 52=20190206-16:26:09.059 56=NEWFIXENGINE 6=174.51 11=141636850670842269979 14=100.0000000000 17=3636850671684357979 20=0 21=2 31=174.51 32=100.0000000000 37=1005448 38=100 39=2 40=1 54=1 55=AAPL 60=20190206-16:26:08.435 150=2 151=0.0000000000 10=194 ");
        } catch (IOException e) {
            logger.error("failed to process message [{}]", message, e);
        }
    }
}
