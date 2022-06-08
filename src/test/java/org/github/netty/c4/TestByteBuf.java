package org.github.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class TestByteBuf {
    public static void main(String[] args) {

//        ByteBuf buf = ByteBufAllocator.DEFAULT.heapBuffer();  // c池化的，不安全的，堆内存

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();  // 池化的，不安全的，直接内存

//         System.out.println(buf);  // PooledUnsafeDirectByteBuf(ridx: 0, widx: 0, cap: 256)
        log(buf);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            sb.append("a");
        }

        buf.writeBytes(sb.toString().getBytes());
//         System.out.println(buf);  // PooledUnsafeDirectByteBuf(ridx: 0, widx: 0, cap: 512)
        log(buf);

        buf.clear();
        buf.writeBoolean(true);
        log(buf);

        buf.clear();
        buf.writeChar(1);
        log(buf);

        buf.clear();
        buf.writeInt(10);
        log(buf);

        buf.clear();
        buf.writeLong(20L);
        log(buf);

    }

    private static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
            .append("read index:").append(buffer.readerIndex())
            .append(" write index:").append(buffer.writerIndex())
            .append(" capacity:").append(buffer.capacity())
            .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
