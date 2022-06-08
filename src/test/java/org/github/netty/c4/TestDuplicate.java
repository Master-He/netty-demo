package org.github.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;


public class TestDuplicate {
    public static void main(String[] args) {
        ByteBuf origin = ByteBufAllocator.DEFAULT.buffer(10);
        origin.writeBytes(new byte[]{1, 2, 3, 4});

        ByteBuf duplicate = origin.duplicate();
        // 和Slice类似，和origin公用一块内存，但是他没有 max capacity 的限制
        duplicate.writeBytes(new byte[] {5});
        System.out.println(ByteBufUtil.prettyHexDump(origin)); // 1,2,3,4
        System.out.println(ByteBufUtil.prettyHexDump(duplicate)); // 1,2,3,4,5

        duplicate.setByte(1, 9);
        System.out.println(ByteBufUtil.prettyHexDump(origin)); // 1,9,3,4
        System.out.println(ByteBufUtil.prettyHexDump(duplicate)); // 1,9,3,4,5
    }

}
