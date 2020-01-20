package com.study.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * 该类主要是接收客户端发来的消息，并输出到控制台。Netty 服务器的通信步骤为：
 * 1、创建两个 NIO 线程组，一个专门用于接收来自客户端的连接，另一个则用于处理已经被接收的连接。
 * 2、创建一个 ServerBootstrap 对象，配置 Netty 的一系列参数，例如接受传出数据的缓存大小等。
 * 3、创建一个用于实际处理数据的类 ChannelInitializer，进行初始化的准备工作，比如设置接受传出数据的字符集、格式以及实际处理数据的接口。
 * 4、绑定端口，执行同步阻塞方法等待服务器端启动。
 *
 * @author Administrator
 */
public class HelloServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 接收到数据时调用该方法
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
        } finally {
            // 抛弃收到的数据
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 当 Netty 由于 IO 错误或者处理器在处理事件时抛出异常时调用
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}
