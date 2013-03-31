package org.solopie.siren.framework.common;

import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/15/13
 * Time: 10:03 PM
 */
public class MsgQueue {
    private static final ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue> msgQueueMap = new ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue>();
    private static Logger logger = Logger.getLogger(MsgQueue.class);
    private ConcurrentLinkedQueue instanceQueue;
    private Class currentClazz;

    private MsgQueue(Class<?> clazz) {
        shiftQueue(clazz);
    }

    public static MsgQueue getInstance(Class<?> clazz) {
        return new MsgQueue(clazz);
    }

    private void shiftQueue(Class<?> clazz) {
        if (msgQueueMap.containsKey(clazz)) {
            instanceQueue = msgQueueMap.get(clazz);
            currentClazz = clazz;
        } else {
            instanceQueue = new ConcurrentLinkedQueue();
            msgQueueMap.put(clazz, instanceQueue);
            currentClazz = clazz;
        }
    }

    private void addMsg(Object msg) {
        instanceQueue.offer(msg);
    }

    public void offer(Object msg) {
        addMsg(msg);
    }

    public Object poll() {
        return instanceQueue.poll();
    }
}
