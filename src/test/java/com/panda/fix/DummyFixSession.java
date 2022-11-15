package com.panda.fix;

import com.panda.fix.session.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyFixSession extends FixSession {

    private static final Logger logger = LoggerFactory.getLogger(DummyFixSession.class);
    public DummyFixSession(FixSession fixSession) {
        super(fixSession.getSessionName(), fixSession.getTargetCompId(), fixSession.getSourceComId(),
                fixSession.getType(), fixSession.getHost(), fixSession.getPort());
    }

    @Override
    public void start(){
        this.setFixSessionConnection(new DummyFixSessionConnection(this.getSessionName(), this.getHost(), this.getPort()));
        this.getFixSessionConnection().start();
    }
}
