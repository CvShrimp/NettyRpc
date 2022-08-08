package com.cvshrimp.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by CvShrimp on 2022/8/8
 *
 * @author wkn
 */
@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {

    private String scanPath;

    public String getScanPath() {
        return scanPath;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }
}
