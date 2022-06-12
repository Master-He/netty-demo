package org.github.chatroom.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import junit.framework.TestCase;
import org.github.chatroom.message.LoginRequestMessage;

public class MessageCodecTest extends TestCase {
    public void testCodec() throws Exception {

        EmbeddedChannel channel = new EmbeddedChannel(
            new LoggingHandler(),
            new LengthFieldBasedFrameDecoder(
                1024, 12, 4, 0, 0),
            new MessageCodec()
        );

        // test encode
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(message);

        // test decode
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message, buf);
        // 模拟半包
        ByteBuf s1 = buf.slice(0, 100);
        ByteBuf s2 = buf.slice(100, buf.readableBytes() - 100);
        s1.retain(); // 引用计数加1，因为channel.writeInbound(s1)会将引用计数减1
        channel.writeInbound(s1);
        channel.writeInbound(s2);
    }
}