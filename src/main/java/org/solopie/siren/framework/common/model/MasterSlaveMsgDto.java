package org.solopie.siren.framework.common.model;

import java.io.Serializable;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/15/13
 * Time: 9:44 PM
 */
public class MasterSlaveMsgDto implements Serializable {
    private String command;
    private String type;
    private Object content;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString(){
        return String.format("%s,%s,%s",command,type,content.toString());
    }
}
