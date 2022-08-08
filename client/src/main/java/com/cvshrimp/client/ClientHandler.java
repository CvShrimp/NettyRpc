package com.cvshrimp.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by wukn on 2017/6/15.
 */
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

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
        log.info("channel read {}", msg);
        response = msg;
        ctx.close();
    }

    public Object getResponse() {
        return response;
    }
}
