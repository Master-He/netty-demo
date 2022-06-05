package org.github.netty.c1;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

public class TestEventLoopGroup {
    public static void main(String[] args) {
        DefaultEventLoopGroup group = new DefaultEventLoopGroup(2);
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        for (EventExecutor eventExecutor : group) {
            System.out.println(eventExecutor);
        }
        group.shutdownGracefully();
    }
}
