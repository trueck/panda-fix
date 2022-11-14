package com.panda.fix.operator;

import com.panda.fix.FixEngine;
import com.panda.fix.monitor.FixSessionStatus;
import com.panda.fix.session.FixSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ListSessionsCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(ListSessionsCommand.class);
    private FixEngine fixEngine;

    public ListSessionsCommand(FixEngine fixEngine) {
        this.fixEngine = fixEngine;

    }

    @Override
    public CommandResult execute() {
        logger.info("executing command ListSessionsCommand");
        return new CommandResult(getFixSessionStatus(fixEngine.getFixSessions()).toString());
    }

    public List<FixSessionStatus> getFixSessionStatus(Map<String, FixSession> fixSessions) {
        return fixSessions.values().stream()
                .map(this::createFixSessionStatus)
                .toList();
    }

    private FixSessionStatus createFixSessionStatus(FixSession fixSession) {
        return new FixSessionStatus(fixSession.getSessionName(),
                fixSession.getSourceComId(),
                fixSession.getTargetCompId(),
                fixSession.getFixSessionConnection().getStatus());
    }
}
