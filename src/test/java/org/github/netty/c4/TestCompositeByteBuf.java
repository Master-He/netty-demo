package org.github.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;

public class TestCompositeByteBuf {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer(5);
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer(5);
        buf2.writeBytes(new byte[]{6, 7, 8, 9, 10});
        System.out.println(ByteBufUtil.prettyHexDump(buf1));
        System.out.println(ByteBufUtil.prettyHexDump(buf2));

        // 这个进行了内存复制，浪费性能
        ByteBuf buf3 = ByteBufAllocator.DEFAULT.buffer(buf1.readableBytes()+buf2.readableBytes());
        buf3.writeBytes(buf1);
        buf3.writeBytes(buf2);
        System.out.println(ByteBufUtil.prettyHexDump(buf3));

        System.out.println("============");
        buf1.resetReaderIndex();
        buf2.resetReaderIndex();
        CompositeByteBuf buf4 = ByteBufAllocator.DEFAULT.compositeBuffer();
        // true 表示增加新的 ByteBuf 自动递增 write index, 否则 write index 会始终为 0
        buf4.addComponents(true, buf1, buf2);
        System.out.println(ByteBufUtil.prettyHexDump(buf4));
    }
}
