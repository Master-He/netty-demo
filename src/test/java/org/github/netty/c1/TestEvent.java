package org.github.netty.c1;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEvent {
    public static void main(String[] args) {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        eventExecutors.next().execute(() -> {
            log.info("1");
        });
    }
}
