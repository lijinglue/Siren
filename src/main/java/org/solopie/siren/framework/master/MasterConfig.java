package org.solopie.siren.framework.master;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/15/13
 * Time: 8:22 PM
 */
public class MasterConfig {
    private static final Logger logger = Logger.getLogger(MasterConfig.class);

    private Integer port;

    public MasterConfig() {
        load();
    }

    public void load() {
        try {
            FileInputStream fileInputStream = new FileInputStream("resources/master.properties");
            Properties properties = new Properties();
            properties.load(fileInputStream);
            port = (Integer) properties.get("masterPort");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getPort() {
        return port;
    }
}
