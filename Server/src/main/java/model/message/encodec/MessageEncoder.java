package model.message.encodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import server.MessageProtocol;

public class MessageEncoder extends MessageToByteEncoder<MessageProtocol>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception
    {
        System.out.println("encode"+msg.getContent().toString());
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}
