package com.cvshrimp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * Created by wukn on 2017/6/15.
 */
public class RpcClient {

    private String host;

    private int port;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object sendCommand(Class clazz, Method method, Object[] args) {
        MethodInvoker invoker = new MethodInvoker(clazz, method.getName(), args, method.getParameterTypes());
        ClientHandler clientHandler = new ClientHandler(invoker);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ObjectDecoder(1024 * 1024,
                            ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                    ch.pipeline().addLast(new ObjectEncoder());
                    ch.pipeline().addLast(clientHandler);
                }
            });

            ChannelFuture f = b.connect(new InetSocketAddress(host, port)).sync();
            f.channel().closeFuture().sync();
            return clientHandler.getResponse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
        return null;
    }
}
