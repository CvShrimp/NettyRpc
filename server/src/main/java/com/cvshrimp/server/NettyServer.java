package com.cvshrimp.server;

import com.cvshrimp.tool.ThreadPoolManagerUtil;
import com.google.common.collect.Maps;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Created by CvShrimp on 2019/11/4.
 *
 * @author wkn
 */
@Slf4j
@Component
public class NettyServer implements ApplicationContextAware, InitializingBean {

    @Value("${export.port}")
    private int port;

    private Map<String, Object> serviceMap = Maps.newHashMap();

    @Autowired
    private ServiceRegistry serviceRegistry;

    /**
     * 设置ApplicationContext，先于InitializingBean的afterPropertiesSet被调用
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        serviceRegistry.registerRoot();
        for(Object serviceBean : serviceBeanMap.values()) {
            Class<?> clazz = serviceBean.getClass();
            Class<?>[] interfaces = clazz.getInterfaces();
            for(Class<?> inter : interfaces) {
                String interfaceName = inter.getName();
                serviceMap.put(interfaceName, serviceBean);
                serviceRegistry.registerService(inter, "127.0.0.1:" + port);
                // 注册服务成功
                log.info("Register service successful: {}", interfaceName);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadPoolManagerUtil.getInstance().addTask(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            // 将注册的服务给NettyServerHandler
            NettyServerHandler nettyServerHandler = new NettyServerHandler(serviceMap);
            try {
                ServerBootstrap bootstrap = new ServerBootstrap().group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new ObjectDecoder(1024 * 1024,
                                        ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                                ch.pipeline().addLast(new ObjectEncoder());
                                ch.pipeline().addLast(nettyServerHandler);
                            }
                        }).option(ChannelOption.SO_BACKLOG, 1024)
                        .childOption(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture future = bootstrap.bind(port).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        });
    }


}
