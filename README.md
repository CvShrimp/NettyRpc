# NettyRpc
RPC framework which based on Netty:
* service interface will be registered into Zookeeper
* Support SPI extension
* Support auto load-balance between multiple service provider instances

Modules:
1. api(common dependency)
2. client(service invoke wrapper)
3. server(service provider)
