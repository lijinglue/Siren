import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.solopie.siren.framework.common.ConnectionPersistor;
import org.solopie.siren.framework.common.SocketMsgIO;
import org.solopie.siren.framework.exception.FailedReadingSocketMsg;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/16/13
 * Time: 12:40 PM
 */
public class ConnectionPersistorTestSet {
    private ServerSocket serverSocket = null;
    private Socket serverSocketConn;
    private Socket clientSocketConn;
    private Thread server;
    private Thread client;
    private int port = 35991 + TestUtil.getPortOffset();

    @Before
    public void setUp() {
        server = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert serverSocket != null;
                try {
                    serverSocketConn = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });
        client = new Thread(new Runnable() {
            @Override
            public void run() {
                clientSocketConn = new Socket();
                try {
                    clientSocketConn.connect(new InetSocketAddress("localhost",port));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void testBroadCast() throws InterruptedException, IOException, FailedReadingSocketMsg {
        server.start();
        client.start();
        while (null == clientSocketConn || !clientSocketConn.isConnected()) {
            Thread.sleep(100L);
        }
        ConnectionPersistor connectionPersistor = ConnectionPersistor.getInstance();
        connectionPersistor.add(serverSocketConn);
        SocketMsgIO socketMsgIO = new SocketMsgIO(clientSocketConn);
        String msgSend = "hello world";
        connectionPersistor.broadcast(msgSend);
        String msgRecv = socketMsgIO.readMsg(String.class);
        Assert.assertEquals(msgRecv, msgSend);
        server.join();
        client.join();
    }
}
