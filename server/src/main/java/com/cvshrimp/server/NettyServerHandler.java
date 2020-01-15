package com.cvshrimp.server;

import com.cvshrimp.client.MethodInvoker;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    private Map<String, Object> serviceMap;

    public NettyServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server come here:channelActive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server come here:channelRead");
        MethodInvoker methodInvoker = (MethodInvoker) msg;
        Object service = serviceMap.get(methodInvoker.getClazz().getName());

        Method method = service.getClass().getDeclaredMethod(methodInvoker.getMethod(), methodInvoker.getParameterTypes());
        Object result = method.invoke(service, methodInvoker.getArgs());
        ctx.writeAndFlush(result);
    }
}
