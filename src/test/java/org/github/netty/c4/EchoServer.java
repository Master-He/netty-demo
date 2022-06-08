package org.github.netty.c4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class EchoServer {
    public static void main(String[] args) {
        new ServerBootstrap()
            .group(new NioEventLoopGroup())
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            ByteBuf buffer = (ByteBuf) msg;
                            System.out.println(buffer.toString(Charset.defaultCharset()));

                            // 建议使用 ctx.alloc() 创建 ByteBuf
                            ByteBuf response = ctx.alloc().buffer();
                            response.writeBytes(buffer);
                            ctx.writeAndFlush(response);

                            // 思考：需要释放 buffer 吗
                            buffer.release();  // buffer内容已经写入response里了，可以释放了
                            // 思考：需要释放 response 吗
                            response.release(); // response已经发送并写到如channel了，可以释放了
                        }
                    });
                }
            }).bind(8080);
    }
}
