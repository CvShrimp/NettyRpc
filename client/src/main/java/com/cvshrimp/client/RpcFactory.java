package com.cvshrimp.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
public class RpcFactory<T> implements InvocationHandler {

    private RpcClient client;

    private Class<T> clazz;

    private static volatile String address;

    private static volatile Integer port;

    public RpcFactory(Class<T> clazz) {
        this.clazz = clazz;
        this.client = new RpcClient(address, port);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return client.sendCommand(clazz, method, args);
    }

    public static void setAddress(String address) {
        RpcFactory.address = address;
    }

    public static void setPort(Integer port) {
        RpcFactory.port = port;
    }
}
