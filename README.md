# NettyRpc
基于Netty的RPC调用实现,将服务注册到Zookeeper上,从Zookeeper上获取服务调用地址,支持SPI扩展,实现了多服务提供者自动负载均衡

包括三个模块：
1. api    依赖
2. client 客户端
3. server 服务端
