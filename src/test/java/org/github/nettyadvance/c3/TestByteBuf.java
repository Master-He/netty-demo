package org.github.nettyadvance.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestByteBuf {
    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new LoggingHandler());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 根据源码分析和控制台日志打印分析， 可以通过-Dio.netty.allocator.type=unpooed -Dio.netty.noPreferDirect=true设置buffer为非池化堆内存
                                ByteBuf buf = ctx.alloc().buffer();  // PooledUnsafeDirectByteBuf // 安卓系统默认unpooled，其他的默认pooled
                                log.debug("alloc buf {}", buf);  // 用TestBacklogClient作为客户端

                                log.debug("receive buf {}", msg); // PooledUnsafeDirectByteBuf, 网络IO和allocator分配的不一样， 网络IO使用直接内存效率高
                                System.out.println("");
                            }
                        });
                    }
                }).bind(8080);
    }
}
