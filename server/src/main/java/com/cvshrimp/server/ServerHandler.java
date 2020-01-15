package com.cvshrimp.server;

import com.cvshrimp.client.MethodInvoker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * Created by wukn on 2017/6/15.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server come here:channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server come here:channelRead");
        MethodInvoker methodInvoker = (MethodInvoker) msg;
        Object service = applicationContext.getBean(methodInvoker.getClazz());

        Method method = service.getClass().getDeclaredMethod(methodInvoker.getMethod(), methodInvoker.getParameterTypes());
        Object result = method.invoke(service, methodInvoker.getArgs());
        ctx.writeAndFlush(result);
//        Method[] methods = service.getClass().getDeclaredMethods();
//        for (Method method : methods) {
//            if(method.getName().equals(methodInvoker.getMethod())) {
//                Object result = method.invoke(service, methodInvoker.getArgs());
//                ctx.writeAndFlush(result);
//            }
//        }
    }
}
