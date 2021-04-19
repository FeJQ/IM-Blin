package com.fejq.blin.model.message;

import android.util.Log;

import com.fejq.blin.net.MessageProtocol;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<MessageProtocol>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception
    {
        Log.i("Blin","encode:"+new String(msg.getContent(), Charset.forName("utf-8")));
        out.writeInt(msg.getLength());
        out.writeBytes(msg.getContent());
    }
}
