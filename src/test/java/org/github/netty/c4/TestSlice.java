package org.github.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;


public class TestSlice {
    public static void main(String[] args) {
        ByteBuf origin = ByteBufAllocator.DEFAULT.buffer(10);
        origin.writeBytes(new byte[]{1, 2, 3, 4});
        System.out.println(ByteBufUtil.prettyHexDump(origin));  // 1,2,3,4
        origin.readByte();
        System.out.println(ByteBufUtil.prettyHexDump(origin));  // 2,3,4

        ByteBuf slice = origin.slice();
        System.out.println(ByteBufUtil.prettyHexDump(slice));  // 2,3,4
        // slice.writeBytes("5".getBytes()); // 会报错，因为切片后的 max capacity 被固定了



        // 这个时候再读原始buf,slice不会跟着改变，因为slice维护自己的read,write指针
        origin.readByte();
        System.out.println(ByteBufUtil.prettyHexDump(origin)); // 3,4
        System.out.println(ByteBufUtil.prettyHexDump(slice));  // 2,3,4

        // slice和origin使用同一块内存，有一方修改，另一方也会修改
        slice.setByte(1,2);
        System.out.println(ByteBufUtil.prettyHexDump(origin)); // 2,4
        System.out.println(ByteBufUtil.prettyHexDump(slice));  // 2,2,4
    }

}
