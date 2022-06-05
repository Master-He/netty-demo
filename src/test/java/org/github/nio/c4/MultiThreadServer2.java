package org.github.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static org.github.nio.c2.ByteBufferUtil.debugAll;


@Slf4j
public class MultiThreadServer2 {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        Selector boss = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        SelectionKey boosKey = ssc.register(boss, 0, null);
        boosKey.interestOps(SelectionKey.OP_ACCEPT);

        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }

        AtomicInteger index = new AtomicInteger();
        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.info("connected ...{}", sc.getRemoteAddress());
                    log.info("before register ...{}", sc.getRemoteAddress());
                    // boss调用 初始化 worker selector, round robin 轮询
                    workers[index.getAndIncrement() % workers.length].register(sc);
                    log.info("after register ...{}", sc.getRemoteAddress());
                }
            }
        }
    }

    private static class Worker implements Runnable {
        private Thread thread;
        private Selector selector;
        private final String name;
        private volatile boolean start = false; // 还未初始化

        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
        public Worker(String name) {
            this.name = name;
        }

        // 初始化线程，初始化 worker selector
        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                this.selector = Selector.open();
                this.thread = new Thread(this, name);
                thread.start();
                start = true;
            }
            selector.wakeup(); // 唤醒select()方法， 这样可以保证，在boss线程中，可以sc可以register到selector中
            sc.register(selector, SelectionKey.OP_READ, null);
        }


        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        try {
                            if (key.isReadable()) {
                                ByteBuffer buffer = ByteBuffer.allocate(16);
                                SocketChannel channel = (SocketChannel) key.channel();
                                log.info("read ...{}", channel.getRemoteAddress());
                                int read = channel.read(buffer);
                                if (read == -1) {
                                    key.cancel();
                                } else {
                                    buffer.flip();
                                    debugAll(buffer);
                                }
                            }
                        } catch (IOException e) {
                            key.cancel();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
