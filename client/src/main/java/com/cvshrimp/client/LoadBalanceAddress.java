package com.cvshrimp.client;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * Created by CvShrimp on 2020/3/7
 *
 * @author wkn
 */
@Component
public class LoadBalanceAddress {

    private String host;

    private int port;

    public void loadBalance(List<String> addressList) {
        if(addressList != null && addressList.size() > 0) {
            int size = addressList.size();
            Random random = new Random();
            String address = addressList.get(random.nextInt(size));
            String[] infoArray = address.split(":");
            this.host = infoArray[0];
            this.port = Integer.valueOf(infoArray[1]);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
