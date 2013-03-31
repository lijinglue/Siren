import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.solopie.siren.framework.common.MsgQueue;
import org.solopie.siren.framework.common.model.LogDto;
import org.solopie.siren.framework.common.model.MasterSlaveMsgDto;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/15/13
 * Time: 10:55 PM
 */
public class MsgQueueTestSet {

    @Test public void testCreatingQueue(){
        MsgQueue msgQueue = MsgQueue.getInstance(String.class);
    }

    @Test public void testIsolatedQueue(){
        MsgQueue msgQueue = MsgQueue.getInstance(String.class);
        msgQueue.offer("hello");
        msgQueue.offer("world");
        Assert.assertEquals(msgQueue.poll(),"hello");
        Assert.assertEquals(msgQueue.poll(),"world");
    }

    @Test
    public void testGetExistingQueue() {//Singleton for each class
        MsgQueue strMsgQueue1 = MsgQueue.getInstance(String.class);
        MsgQueue strMsgQueue2 = MsgQueue.getInstance(String.class);
        String msg1 = "hello";
        strMsgQueue1.offer(msg1);
        String msg2 = (String) strMsgQueue2.poll();
        System.out.println(msg2);
        Assert.assertEquals(msg1,msg2);
    }

}
