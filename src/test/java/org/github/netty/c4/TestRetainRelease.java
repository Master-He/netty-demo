package org.github.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class TestRetainRelease {
    public static void main(String[] args) {

        // Netty 这里采用了引用计数法来控制回收内存，每个 ByteBuf 都实现了 ReferenceCounted 接口
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeBytes("123".getBytes());
        log(buf);
        buf.retain();
        System.out.println("ReferenceCount: " + buf.refCnt());
        System.out.println("release buf, is ReferenceCount zero？: " + buf.release());
        System.out.println("release buf, is ReferenceCount zero？: " + buf.release());

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
