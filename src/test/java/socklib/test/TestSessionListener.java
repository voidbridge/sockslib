package socklib.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sockslib.client.Socks5;
import sockslib.server.Session;
import sockslib.server.SocksProxyServer;
import sockslib.server.SocksServerBuilder;
import sockslib.server.listener.CloseSessionException;
import sockslib.server.listener.SessionCloseListener;
import sockslib.server.listener.SessionCreateListener;
import sockslib.test.client.SocksTester;

import java.io.IOException;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Youchao Feng
 * @version 1.0
 * @date Nov 23, 2015 4:51 PM
 */
public class TestSessionListener {

  private SocksProxyServer server;

  @Before
  public void init() {
    server = SocksServerBuilder.buildAnonymousSocks5Server();
  }

  @After
  public void clear() throws InterruptedException {
    server.shutdown();
    Thread.sleep(200);
  }

  @Test
  public void testSessionCreateListener() throws IOException {
    final boolean[] isCreateSession = {false};
    server.getSessionManager()
        .onSessionCreate("create", new SessionCreateListener()
        {
	        @Override
	        public void onCreate(Session session) throws CloseSessionException
	        {
		        isCreateSession[0] = true;
	        }
        });
    server.start();
    assertFalse(isCreateSession[0]);
    checkConnect();
    assertTrue(isCreateSession[0]);
  }

  @Test
  public void testSessionCloseListener() throws IOException, InterruptedException {
    final boolean[] isClose = {false};
    server.getSessionManager().onSessionClose("close", new SessionCloseListener()
    {
	    @Override
	    public void onClose(Session session)
	    {
		    isClose[0] = true;
	    }
    });
    server.start();
    assertFalse(isClose[0]);
    checkConnect();
    Thread.sleep(2000);
    assertTrue(isClose[0]);
  }

   private void checkConnect() throws IOException {
    SocksTester.checkConnect(new Socks5("localhost", 1080));
  }

}
