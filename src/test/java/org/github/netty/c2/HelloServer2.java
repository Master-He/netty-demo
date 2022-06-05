package org.github.netty.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;

public class HelloServer2 {
    public static void main(String[] args) {
        // 1.启动器，负责组装netty组件，启动服务器
        // 2.BoosEventLoop, WorkerEventLoop(selector, thread), group组
        // 3.选择服务器的ServerSocketChannel实现
        // 4.boss负责处理连接， worker(child) 负责处理读写，（handler）决定了worker(child) 能执行哪些操作
        // 5.channel代表和客户端进行数据读写的通道 Initializer初始化，负责添加别的handler
        // 6.添加具体handler
            // 将ByteBuf转换为字符串
            // 打印上一步转换好的字符串
        // 7.绑定监听端口
        new ServerBootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(
                new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                }
            )
            .bind(8080);
    }
}
