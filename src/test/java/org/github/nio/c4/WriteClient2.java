package org.github.nio.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WriteClient2 {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));

        int count = 0;
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        while (true) {
            int read = sc.read(buffer);
            if (read != -1) {
                count += read;
            }
            buffer.clear();
            System.out.println(count);
        }

    }
}
