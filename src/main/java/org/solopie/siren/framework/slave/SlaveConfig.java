package org.solopie.siren.framework.slave;


import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/14/13
 * Time: 7:57 PM
 */
public class SlaveConfig {
    private static final Logger logger = Logger.getLogger(SlaveConfig.class);

    private String masterAddress;
    private Integer masterPort;

    public SlaveConfig(){
        load();
    }

    public void load(){
        try {
            FileInputStream fileInputStream = new FileInputStream("resources/slave.properties");
            Properties properties = new Properties();
            properties.load(fileInputStream);
            masterAddress = (String) properties.get("masterAddress");
            masterPort = (Integer) properties.get("masterPort");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMasterAddress() {
        return masterAddress;
    }
    public Integer getMasterPort() {
        return masterPort;
    }
}
