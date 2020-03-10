package com.cvshrimp.client;

import com.cvshrimp.spi.ExtensionLoader;
import com.cvshrimp.spi.api.ILoadBalance;
import com.cvshrimp.spi.impl.LoadBalanceAddress;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
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
            Invoker invoker = ExtensionLoader.getExtensionLoader(ILoadBalance.class).getDefaultExtension().loadBalance(providerAddresses);
            RpcClient rpcClient = new RpcClient(invoker.getHost(), invoker.getPort());
            return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(), new Class[] { rpcInterface },
                    new RpcFactory<>(rpcInterface, rpcClient));
        } catch (Exception e) {
            log.error("No provider, interface={}", rpcInterface.getName());
        }
        return null;
    }
}
