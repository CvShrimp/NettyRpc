package com.cvshrimp.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
@Slf4j
public class RpcFactoryBean<T> implements FactoryBean<T> {

    private Class<T> rpcInterface;

//    @Autowired
//    RpcFactory<T> factory;

    public RpcFactoryBean() {}

    public RpcFactoryBean(Class<T> rpcInterface) {
        this.rpcInterface = rpcInterface;
    }

    @Override
    public T getObject() throws Exception {
        return getRpc();
    }

    @Override
    public Class<?> getObjectType() {
        return this.rpcInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings(value = "unchecked")
    private <T> T getRpc() {
        ZooKeeper zooKeeper = ZkClient.getInstance();
        StringBuilder pathBuilder = new StringBuilder("/rpc/");
        pathBuilder.append(rpcInterface.getName());
        String providerAddress;
        try {
            providerAddress = new String(zooKeeper.getData(pathBuilder.toString(), true, new Stat()));
            String[] infoArray = providerAddress.split(":");
            RpcClient rpcClient = new RpcClient(infoArray[0], Integer.valueOf(infoArray[1]));
            return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(), new Class[] { rpcInterface },
                    new RpcFactory<>(rpcInterface, rpcClient));
        } catch (Exception e) {
            log.error("No provider, interface={}", rpcInterface.getName());
        }
        return null;
    }
}
