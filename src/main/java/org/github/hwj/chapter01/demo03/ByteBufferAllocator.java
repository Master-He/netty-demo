package org.github.hwj.chapter01.demo03;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ByteBufferAllocator {
    public static void main(String[] args) {
        // class java.nio.HeapByteBuffer, java堆内存读写效率较低， 会受到GC影响（标记复制，标记整理，标记清除）
        System.out.println(ByteBuffer.allocate(16).getClass());
        // class java.nio.DirectByteBuffer， 直接内存，读写效率较高（少一次拷贝）， 不会受GC影响
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
    }
}
