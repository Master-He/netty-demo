package org.github.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

@Slf4j
public class Server2 {
    public static void main(String[] args) throws IOException {
        // open()获取selector
        Selector selector = Selector.open();

        // open()获取ssc， 并设置非阻塞
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // ssc注册到selector, 获取sscKey
        SelectionKey sscKey = ssc.register(selector, 0, null);// att是attachment，附件的意思

        // sscKey.interestOps(SelectionKey.OP_ACCEPT)设置监听事件类型
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.info("sscKey:{}", sscKey);

        // ssc绑定8080端口
        ssc.bind(new InetSocketAddress(8080));

        while (true) {
            // selector.select()
            selector.select();

            // selectedChannels遍历
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                // from iterator next() get selectionKey
                SelectionKey selectionKey = iterator.next();

                // iterator remove
                iterator.remove();
                log.debug("selectionKey: {}", selectionKey);

                // selectionKey 根据监听事件类型分别处理
                if (selectionKey.isAcceptable()) {
                    // accept事件, 这个事件事ssc监听的，ssc处理， ssc接受新sc, 然后并设置sc为read,
                    // 获取ssc对应的channel
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    // 获取sc, 并设置非阻塞
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    // 获取scKey, 并设置读监听事件
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.info("sc :" + sc);
                    log.info("scKey :" + scKey);

                } else if (selectionKey.isReadable()) {
                    // read事件是sc监听的，sc处理， sc读数据到buffer
                    try {
                        SocketChannel sc = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(4);
                        int read = sc.read(buffer);
                        if (read == -1) {
                            selectionKey.cancel();
                        } else {
                            buffer.flip();
                            log.info("buffer: " + Charset.defaultCharset().decode(buffer));
                        }
                    } catch (IOException e) {
                        log.warn("io exception", e);
                    }

                }
            }
        }
    }
}
