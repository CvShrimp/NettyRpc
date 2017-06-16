package com;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKDynamicService<T> implements InvocationHandler {

    private Class<T> clazz;

    private RpcClient client = new RpcClient("127.0.0.1", 8888);

    public JDKDynamicService(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T get() {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{this.clazz}, this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return client.sendCommand(clazz, method, args);
    }

    public void setClass(Class<T> clazz) {
        this.clazz = clazz;
    }
}