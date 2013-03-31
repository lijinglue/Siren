package org.solopie.siren.framework.slave;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.solopie.siren.framework.common.ConnectionPersistor;
import org.solopie.siren.framework.common.SocketMsgIO;
import org.solopie.siren.framework.common.model.MasterSlaveMsgDto;
import org.solopie.siren.framework.constant.MsgString;
import org.solopie.siren.framework.exception.FailedReadingSocketMsg;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/13/13
 * Time: 10:50 PM
 */
public class SlaveClient implements Runnable {
    private Logger logger = Logger.getLogger(this.getClass());
    private Socket socket = null;
    private SlaveConfig slaveConfig = new SlaveConfig();
    private ConnectionPersistor connectionPersistor = ConnectionPersistor.getInstance();

    @Override
    public void run() {
        socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(slaveConfig.getMasterAddress(), slaveConfig.getMasterPort());
        connectionPersistor.add(socket);
        SocketMsgIO socketMsgIO = null;

        while (!socket.isConnected()) {
            try {
                socket.connect(socketAddress);
            } catch (IOException e) {
                logger.error(MsgString.SOCKET_CANNOT_CONNECT);
                if (logger.getLevel() == Level.DEBUG)
                    e.printStackTrace();
            }
            try {
                if (!socket.isConnected()) {
                    logger.info(MsgString.RETRY_CONN_MSG);
                    Thread.sleep(5000L);
                }
            } catch (InterruptedException e) {
                if (logger.getLevel().isGreaterOrEqual(Priority.DEBUG))
                    e.printStackTrace();
            }
            try {
                socketMsgIO = new SocketMsgIO(socket);
            } catch (IOException e) {
                socket = new Socket();//if cannot read from socket, reconnect
                logger.error(MsgString.SOCKET_CANNOT_READ);
                if (logger.getLevel().isGreaterOrEqual(Priority.DEBUG))
                    e.printStackTrace();
            }
        }


        while (!Thread.currentThread().isInterrupted()) {
            if (null != socketMsgIO)
                try {
                    MasterSlaveMsgDto masterSlaveMsgDto = socketMsgIO.readMsg(MasterSlaveMsgDto.class);
                } catch (FailedReadingSocketMsg failedReadingSocketMsg) {
                    failedReadingSocketMsg.printStackTrace();
                }
        }

    }
}
