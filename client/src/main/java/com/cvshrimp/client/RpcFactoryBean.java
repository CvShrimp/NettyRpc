package com.cvshrimp.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
@Slf4j
public class RpcFactoryBean<T> implements FactoryBean<T> {

    private Class<T> rpcInterface;

    @Autowired
    private ZkClient zkClient;

    @Autowired
    private LoadBalanceAddress loadBalanceAddress;

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
        ZooKeeper zooKeeper = zkClient.getInstance();
        StringBuilder pathBuilder = new StringBuilder("/rpc/");
        pathBuilder.append(rpcInterface.getName());
        pathBuilder.append("/providers");
        List<String> providerAddresses;
        try {
            providerAddresses = zooKeeper.getChildren(pathBuilder.toString(), true);
            loadBalanceAddress.loadBalance(providerAddresses);
            RpcClient rpcClient = new RpcClient(loadBalanceAddress.getHost(), loadBalanceAddress.getPort());
            return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(), new Class[] { rpcInterface },
                    new RpcFactory<>(rpcInterface, rpcClient));
        } catch (Exception e) {
            log.error("No provider, interface={}", rpcInterface.getName());
        }
        return null;
    }
}
