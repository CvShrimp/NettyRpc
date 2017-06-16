package com;

import java.io.Serializable;
/**
 * Created by wukn on 2017/6/16.
 */
public class MethodInvoker implements Serializable {
    private static final long serialVersionUID = -836688361827224192L;
    private Class clazz;
    private String method;
    private Object[] args;

    public MethodInvoker(Class clazz, String method, Object[] args)
    {
        this.clazz = clazz;
        this.method = method;
        this.args = args;
    }

    public Class getClazz() {
        return this.clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
