package com.cvshrimp.client;

import java.io.Serializable;

/**
 * Created by wukn on 2017/6/15.
 */
public class MethodInvoker implements Serializable{

    private static final long serialVersionUID = -836688361827224192L;

    private Class clazz;

    private String method;

    private Object[] args;

    private Class<?>[] parameterTypes;

    public MethodInvoker(Class clazz, String method, Object[] args) {
        this.clazz = clazz;
        this.method = method;
        this.args = args;
    }


    public MethodInvoker(Class clazz, String method, Object[] args, Class<?>[] parameterTypes) {
        this.clazz = clazz;
        this.method = method;
        this.args = args;
        this.parameterTypes = parameterTypes;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
