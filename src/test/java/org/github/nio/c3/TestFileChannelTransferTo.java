package org.github.nio.c3;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        try (
                FileChannel from = new FileInputStream("data.txt").getChannel();
                FileChannel to = new FileOutputStream("to.txt").getChannel();
        ) {
            // 效率高，底层会利用操作系统的零拷贝进行优化, 2g 数据
            long size = from.size();
            // left 变量代表还剩余多少字节
            for (long left = size; left > 0; ) {
                System.out.println("position:" + (size - left) + " left:" + left);
                // 零拷贝：不是真正的零拷贝， 而是不经过Jvm内存的拷贝，零拷贝从linux2.4开始，磁盘—>内核缓冲区->网卡， 仅拷贝了两次
                // 零拷贝，减少了用户态和内核态的切换，不利用CPU计算，而是使用DMA将内核缓冲区的写入网卡
                // 整个过程仅只发生了一次用户态与内核态的切换， 就是要从 java 程序的用户态切换至内核态，使用 DMA将数据 读入内核缓冲区
                left -= from.transferTo((size - left), left, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
