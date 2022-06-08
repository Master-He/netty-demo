package org.github.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.security.acl.LastOwnerException;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class TestMarkReset {
    public static void main(String[] args) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();  // c池化的，不安全的，堆内存

        buf.writeInt(1);
        buf.writeInt(2);
//        buf.markReaderIndex();
        log(buf);
        System.out.println(buf.readInt());
        log(buf);
        buf.resetReaderIndex(); // 如果没有markReaderIndex， 默认恢复到0
        log(buf);

        System.out.println("\n===================================\n");

        buf.markWriterIndex();
        buf.writeInt(3);
        log(buf);
        buf.resetWriterIndex();
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
