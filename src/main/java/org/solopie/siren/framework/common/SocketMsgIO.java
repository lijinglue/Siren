package org.solopie.siren.framework.common;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.solopie.siren.framework.exception.FailedReadingSocketMsg;
import org.solopie.siren.framework.constant.MsgString;

import java.io.*;
import java.net.Socket;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/14/13
 * Time: 8:50 PM
 */
public class SocketMsgIO {
    private static final String MSG_END_TAG="[#END#]";
    private static Logger logger = Logger.getLogger(SocketMsgIO.class);
    private BufferedReader br = null;
    private BufferedWriter bw = null;

    private Gson gson = new Gson();

    public SocketMsgIO(Socket socket) throws IOException {
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void writeMsg(Serializable msg) {
        String serilizedMsg = gson.toJson(msg);
        try {
            bw.write(serilizedMsg + "\n");
            bw.flush();
            bw.write(MSG_END_TAG+"\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String readMsg() {
        StringBuilder strBuilder = new StringBuilder();
        String str;
        try {
            while(true) {
                str=br.readLine();
                if (null==str||str.equals(MSG_END_TAG))
                    break;
                strBuilder.append(str);
            }
        } catch (IOException e) {
            logger.error(MsgString.SOCKET_CANNOT_READ+":"+e.getMessage());
            if(logger.getLevel().isGreaterOrEqual(Priority.DEBUG))
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
        return strBuilder.toString();
    }

    public <T> T readMsg(Class<T> clzz) throws FailedReadingSocketMsg {
        String plainMsg = readMsg();
        if(null==plainMsg)
            return null;
        T t = null;
        try{
            t = gson.fromJson(plainMsg,clzz);
        }catch (RuntimeException e){
            throw  new FailedReadingSocketMsg(e);
        //Necessarily Evil
        }
        return t;
    }
}
