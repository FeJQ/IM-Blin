package model.message.encodec;


import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import server.MessageProtocol;

public class MessageDecoder extends ReplayingDecoder<Void>
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        System.out.println("decode");
        int length=in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        MessageProtocol messageProtocol=new MessageProtocol();
        messageProtocol.setLength(length);
        messageProtocol.setContent(content);
        out.add(messageProtocol);
    }
}
