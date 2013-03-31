package org.solopie.siren.framework.common;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.solopie.siren.framework.constant.MsgString;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ConnectionPersistor, it's singleton class.
 * It manages socket connections
 *
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/14/13
 * Time: 11:03 PM
 */

public class ConnectionPersistor {
    private static Logger logger = Logger.getLogger(ConnectionPersistor.class);
    private Queue<SocketMsgIO> socketMsgConnQueue = new ConcurrentLinkedQueue<SocketMsgIO>();
    private Gson gson = new Gson();
    ExecutorService executorService = Executors.newCachedThreadPool();

    private ConnectionPersistor() {
    }

    private static ConnectionPersistor instance = null;

    public static ConnectionPersistor getInstance() {
        if (null == instance)
            instance = new ConnectionPersistor();
        return instance;
    }

    public void offer(Socket socket) {
        if (null == socket || !socket.isConnected())
            return;
        try {
            socketMsgConnQueue.offer(new SocketMsgIO(socket));
        } catch (IOException e) {
            logger.error(MsgString.SOCKET_CANNOT_READ);
            if (logger.getLevel().isGreaterOrEqual(Priority.DEBUG))
                e.printStackTrace();
        }
    }

    public void add(Socket socket){
        offer(socket);
    }

    public void broadcast(final Serializable msg) {
        for (final SocketMsgIO client : socketMsgConnQueue) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    client.writeMsg(msg);
                }
            });
        }
    }
}
