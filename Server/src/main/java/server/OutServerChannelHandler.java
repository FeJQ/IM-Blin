package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.Charset;

public class OutServerChannelHandler extends ChannelOutboundHandlerAdapter
{
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
    {
        MessageProtocol m = (MessageProtocol) msg;;
        System.out.println("out bound write:"+new String(m.getContent(), Charset.forName("utf-8")));
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("out bound  flush");
    }
}
