package com.fejq.blin.model.message;

import android.util.Log;

import com.fejq.blin.net.MessageProtocol;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class MessageDecoder extends ReplayingDecoder<Void>
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        int length=in.readInt();
        byte[] content = new byte[length];
        in.readBytes(content);
        Log.i("Blin","decode:"+new String(content, Charset.forName("utf-8")));
        MessageProtocol messageProtocol=new MessageProtocol();
        messageProtocol.setLength(length);
        messageProtocol.setContent(content);
        out.add(messageProtocol);

    }
}
