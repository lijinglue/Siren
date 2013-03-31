import junit.framework.Assert;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
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
 * Time: 12:46 AM
 */
public class SocketMsgIOTestSet {
    private ServerSocket serverSocket = null;
    private Socket serverSocketConn;
    private Socket clientSocketConn;
    private Thread server;
    private Thread client;
    private int port = 35991+TestUtil.getPortOffset();
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
                    clientSocketConn.connect(new InetSocketAddress("localhost", port));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void testReadWriteMsg() throws IOException, InterruptedException, FailedReadingSocketMsg {
        server.start();
        client.start();
        while (null == clientSocketConn || !clientSocketConn.isConnected()) {
            System.out.println("Waiting Connections");
            Thread.sleep(1000L);
        }
        ;
        SocketMsgIO servSocketMsgIO = new SocketMsgIO(serverSocketConn);
        SocketMsgIO clntSocketMsgIO = new SocketMsgIO(clientSocketConn);
        String sendMsg = "hello world";
        clntSocketMsgIO.writeMsg(sendMsg);
        String recvMsg = servSocketMsgIO.readMsg(String.class);
        Assert.assertEquals(sendMsg, recvMsg);
        server.join(1000L);
        client.join(1000L);
    }
}
