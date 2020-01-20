一、Netty中的一些核心的概念
=========================
- bootstrap、serverBootstrap：bootstrap的意思是引导，其主要作用是配置整个netty程序，将各个组件整合起来。serverBootstrap是服务器端的引导类。bootstrap用于连接远程主机它有一个EventLoopGroup ；serverBootstrap用于监听本地端口有两个EventLoopGroup。
- eventLoop：eventLoop维护了一个线程和任务队列，支持异步提交执行任务。
- eventLoopGroup：eventLoopGroup 主要是管理eventLoop的生命周期，可以将其看作是一个线程池，其内部维护了一组eventLoop，每个eventLoop对应处理多个Channel，而一个Channel只能对应一个eventLoop。
- channelPipeLine：是一个包含channelHandler的list，用来设置channelHandler的执行顺序。
- Channel：Channel代表一个实体(如一个硬件设备、一个文件、一个网络套接字或者一个能够执行一个或者多个不同的IO操作的程序组件)的开放链接，如读操作和写操作。
- Futrue、ChannelFuture：Future提供了另一种在操作完成时通知应用程序的方式。这个对象可以看作是一个异步操作结果的占位符；它将在未来的某个时刻完成，并提供对其结果的访问。netty的每一个出站操作都会返回一个ChannelFuture。future上面可以注册一个监听器，当对应的事件发生后会出发该监听器。
- ChannelInitializer：它是一个特殊的ChannelInboundHandler,当channel注册到eventLoop上面时，对channel进行初始化
- ChannelHandler：用来处理业务逻辑的代码，ChannelHandler是一个父接口，ChannelnboundHandler和ChannelOutboundHandler都继承了该接口，它们分别用来处理入站和出站。
- ChannelHandlerContext:允许与其关联的ChannelHandler与它相关联的ChannlePipeline和其它ChannelHandler来进行交互。它可以通知相同ChannelPipeline中的下一个ChannelHandler，也可以对其所属的ChannelPipeline进行动态修改。

二、netty中常用的自带解码器和编码器(编解码器名字对应的只列举一个)
============================================================
- DelimiterBasedFrameDecoder：分隔符解码器，以设定的符号作为消息的结束解决粘包问题
- FixedLengthFrameDecoder：定长解码器，作用于定长的消息
- LineBasedFrameDecoder：按照每一行进行分割，也就是特殊的分隔符解码器，它的分割符为\n或者\r\n。
- LengthFieldBasedFrameDecoder：通过消息中设置的长度字段来进行粘包处理。该解码器总共有5个参数。
  ```
  LengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip)，含义如下：
  int maxFrameLength：单个包的最大大小
  int lengthFieldOffset：定义长度的字段的相对包开始的偏移量
  int lengthFieldLength：定义长度字段所占字节数
  int lengthAdjustment：lengthAdjustment = 数据长度字段之后剩下包的字节数 - 数据长度取值(也就是长度字段之后的所有非数据的其他信息)
  int initialBytesToStrip：从包头开始，要忽略的字节数
  ```
- HttpRequestDecoder：将字节解码为HttpRequest、HttpContent和LastHttpContent消息
- HttpResponseDecoder：将字节解码为HttpResponse、HttpContent和LastHttpContent消息
- ReplayingDecoder：一个特殊的ByteToMessageDecoder ，可以在阻塞的i/o模式下实现非阻塞的解码。 ReplayingDecoder 和ByteToMessageDecoder 最大的不同就是ReplayingDecoder 允许你实现decode()和decodeLast()就像所有的字节已经接收到一样，不需要判断可用的字节
- Base64Decoder ：Base64编码器
- StringDecoder：将接收到的ByteBuf转化为String
- ByteArrayDecoder：将接收到的ByteBuf转化为byte 数组
- DatagramPacketDecoder：运用指定解码器来对接收到的DatagramPacket进行解码
- MsgpackDecoder：用于Msgpack序列化的解码器
- ProtobufDecoder：用于Protobuf协议传输的解码器
- HttpObjectAggregator：将http消息的多个部分聚合起来形成一个FullHttpRequest或者FullHttpResponse消息。
- LengthFieldPrepender：将消息的长度添加到消息的前端的编码器，一般是和LengthFieldBasedFrameDecoder搭配使用
- HttpServerCodec：相当于HttpRequestDecoder和HttpResponseEncoder
- HttpClientCodec：相当于HttpRequestEncoder和HttpResponseDecoder
- ChunkedWriteHandler：在进行大文件传输的时候，一次将文件的全部内容映射到内存中，很有可能导致内存溢出，ChunkedWriteHandler可以解决大文件或者码流传输过程中可能发生的内存溢出问题
