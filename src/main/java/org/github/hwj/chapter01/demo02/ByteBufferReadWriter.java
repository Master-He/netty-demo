package org.github.hwj.chapter01.demo02;

import java.nio.ByteBuffer;

import static org.github.hwj.chapter01.ByteBufferUtil.debugAll;

public class ByteBufferReadWriter {
    public static void main(String[] args) {
//        case1();
        case2();
//        case3();
    }

    private static void case1() {
        ByteBuffer buffer = ByteBuffer.allocate(10); // 默认是写
        buffer.put((byte) 0x61); // 写 a
        debugAll(buffer); // 61 00 00 00 00 00 00 00 00 00

        buffer.put(new byte[] {0x62, 0x63, 0x64});  // 写 b c d
        debugAll(buffer);  // 61 62 63 64 00 00 00 00 00 00

        buffer.flip();  // 切换至读
        System.out.println(buffer.get());
        debugAll(buffer);  // 61 62 63 64 00 00 00 00 00 00

        buffer.compact();  // 切换至写
        debugAll(buffer); // 62 63 64 64 00 00 00 00 00 00

        buffer.put(new byte[] {0x65, 0x66}); // 写 e f
        debugAll(buffer);  // 62 63 64 65 66 00 00 00 00 00
    }

    private static void case2() {
        ByteBuffer buffer = ByteBuffer.allocate(10); // 默认是写
        buffer.put(new byte[] {0x61, 0x62, 0x63, 0x64, 0x65}); // 写 a, b, c, d, e
        debugAll(buffer); // abcde.....

        buffer.flip();  // 切换至读
        buffer.get(new byte[3]); // 只读3个
        debugAll(buffer);  // abcde.....

        buffer.rewind(); // 重新读, 从头开始读
        buffer.get(new byte[5]);
        debugAll(buffer);

        // mark & reset
        // mark记住当前position,reset是将当前position重置到mark位置
        buffer.rewind();
        buffer.get();
        buffer.get();
        buffer.mark(); // 加标记
        buffer.get();
        buffer.get();
        buffer.reset();
        System.out.println((char) buffer.get());  // 打印 c

        // get(i)
        System.out.println((char) buffer.get(4)); // 打印 e

    }

    private static void case3() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put("hello".getBytes());
        debugAll(buffer);  // 68 65 6c 6c 6f 00 00 00 00 00
    }
}
