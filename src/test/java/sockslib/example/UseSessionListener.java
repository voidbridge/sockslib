package sockslib.example;

import sockslib.server.Session;
import sockslib.server.SocksProxyServer;
import sockslib.server.SocksServerBuilder;
import sockslib.server.listener.CloseSessionException;
import sockslib.server.listener.LoggingListener;
import sockslib.server.listener.SessionCloseListener;
import sockslib.server.listener.SessionCreateListener;

import java.io.IOException;

/**
 * @author Youchao Feng
 * @version 1.0
 * @date Nov 23, 2015 4:47 PM
 */
public class UseSessionListener {

  public static void main(String[] args) throws IOException {
    SocksProxyServer server = SocksServerBuilder.buildAnonymousSocks5Server();
    server.getSessionManager().onSessionCreate("createLogging", new LoggingListener());
    server.getSessionManager().onSessionClose("CloseSession",
                                              new SessionCloseListener()
                                              {
	                                              @Override
	                                              public void onClose(Session session)
	                                              {
		                                              System.out.println("Close Session:" + session.getId());
	                                              }
                                              })
        .onSessionCreate("CreateSession",
                         new SessionCreateListener()
                         {
	                         @Override
	                         public void onCreate(Session session1) throws CloseSessionException
	                         {
		                         System.out.println("Create session" + session1.getId());
	                         }
                         });
    server.start();
  }
}
