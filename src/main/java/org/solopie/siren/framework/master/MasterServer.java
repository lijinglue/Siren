package org.solopie.siren.framework.master;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.solopie.siren.framework.common.ConnectionPersistor;
import org.solopie.siren.framework.constant.MsgString;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/13/13
 * Time: 12:19 AM
 */
public class MasterServer implements Runnable {
    private static Logger logger = Logger.getLogger(MasterServer.class);
    private ServerSocket serverSocket = null;
    private boolean serverOn = true;
    private ConnectionPersistor connectionPersistor = ConnectionPersistor.getInstance();
    private MasterConfig masterConfig = new MasterConfig();

    public MasterServer() throws NullPointerException {
        try {
            serverSocket = new ServerSocket(masterConfig.getPort());
        } catch (IOException e) {
            throw new NullPointerException(MsgString.DEFAULT_PORT_OCCUPIED + ":" + masterConfig.getPort());
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socketClient = serverSocket.accept();
                connectionPersistor.offer(socketClient);
            } catch (IOException e) {
                logger.error(MsgString.SOCKET_CANNOT_CONNECT);
                if (logger.getLevel().isGreaterOrEqual(Priority.DEBUG))
                    e.printStackTrace();
            }
        }
    }
}
