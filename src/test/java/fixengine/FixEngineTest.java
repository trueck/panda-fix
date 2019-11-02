package fixengine;

import com.panda.fix.FixEngine;
import com.panda.fix.constant.FixEngineStatus;
import com.panda.fix.constant.FixSessionType;
import com.panda.fix.session.FixSession;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class FixEngineTest {

    private static final Logger logger = LoggerFactory.getLogger(FixEngineTest.class);

    private FixEngine fixEngine;

    @Before
    public void setup(){
        fixEngine = new FixEngine("");
    }

    @Test
    public void testMain(){
        logger.info("testMain is running.");
    }

    @Test
    public void testStatus(){
        assertEquals("the status is not correct before start", FixEngineStatus.STOPPED, fixEngine.getStatus());
        fixEngine.start();
        assertEquals("the status is not correct after start", FixEngineStatus.STARTED, fixEngine.getStatus());
        fixEngine.stop();
        assertEquals("the status is not correct after stop", FixEngineStatus.STOPPED, fixEngine.getStatus());
    }

    @Test
    public void testCreateSessions(){
        fixEngine.start();

        FixSession fixSession1 = new FixSession();
        fixSession1.setSessionName("TEST-EDWIN");
        fixSession1.setTargetCompId("EDWIN");
        fixSession1.setSourceComId("TEST");
        fixSession1.setType(FixSessionType.ACCEPTOR);
        fixSession1.setPort(12345);

        assertEquals("fix session is not correct", fixSession1, fixEngine.getSessions().get("TEST-EDWIN"));
    }
}
