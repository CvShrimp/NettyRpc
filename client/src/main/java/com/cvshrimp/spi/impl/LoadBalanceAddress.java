package com.cvshrimp.spi.impl;

import com.cvshrimp.client.Invoker;
import com.cvshrimp.spi.api.ILoadBalance;

import java.util.List;
import java.util.Random;

/**
 * Created by CvShrimp on 2020/3/7
 *
 * @author wkn
 */
public class LoadBalanceAddress implements ILoadBalance {

    public Invoker loadBalance(List<String> addressList) {
        if(addressList != null && addressList.size() > 0) {
            int size = addressList.size();
            Random random = new Random();
            String address = addressList.get(random.nextInt(size));
            String[] infoArray = address.split(":");
            Invoker invoker = new Invoker();
            invoker.setHost(infoArray[0]);
            invoker.setPort(Integer.valueOf(infoArray[1]));
            return invoker;
        }
        return null;
    }

}
