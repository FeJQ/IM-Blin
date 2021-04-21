package model.message.encodec;


import java.nio.charset.Charset;
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

        int length=in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        MessageProtocol messageProtocol=new MessageProtocol();
        messageProtocol.setLength(length);
        messageProtocol.setContent(content);
        System.out.println("dncode:"+new String(messageProtocol.getContent(), Charset.forName("utf-8")));
        out.add(messageProtocol);
    }
}
