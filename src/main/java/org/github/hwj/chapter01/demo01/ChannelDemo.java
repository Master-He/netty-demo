package org.github.hwj.chapter01.demo01;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class ChannelDemo {
    public static void main(String[] args) {
        // FileChannel get from
        // 1. FileInputStream
        // 2. RandomAccessFile
//        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
//        try (FileChannel channel = new RandomAccessFile("data.txt", "rw").getChannel()) {
        try (FileChannel channel = new FileInputStream(new File(ChannelDemo.class.getResource("/resource_data.txt").getPath())).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                // 从channel读取数据， 向buffer写入
                int len = channel.read(buffer);
                if (len == -1) {
                    break;
                }
                log.info("读到的字节数: " + len);

                buffer.flip(); // buffer切换至读模式
                while (buffer.hasRemaining()) { // 是否还有剩余未读数据
                    byte b = buffer.get();
                    log.info("byte: " + b + "  char: " + (char) b);
                }

                buffer.clear(); // 切换为写模式
//                buffer.compact(); // 切换为写模式
//                buffer.flip();  // 也可以切换为写模式
            }

        } catch (IOException ignore) {
        }

    }
}
