package org.solopie.siren.framework.exception;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/15/13
 * Time: 8:59 PM
 */
public class FailedReadingSocketMsg extends Exception{//This exception must be checked
    public FailedReadingSocketMsg(){
        super();
    }

    public FailedReadingSocketMsg(String msg){
        super(msg);
    }

    public FailedReadingSocketMsg(Throwable tr){
        super(tr);
    }

}
