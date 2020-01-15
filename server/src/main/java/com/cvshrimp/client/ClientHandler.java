package com.cvshrimp.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by wukn on 2017/6/15.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Object response;

    private MethodInvoker methodInvoker;

    public ClientHandler(MethodInvoker methodInvoker) {
        this.methodInvoker = methodInvoker;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       ctx.writeAndFlush(this.methodInvoker);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead:"+ msg);
        response = msg;
        ctx.close();
    }

    public Object getResponse() {
        return response;
    }
}
