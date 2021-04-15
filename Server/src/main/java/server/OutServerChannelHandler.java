package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class OutServerChannelHandler extends ChannelOutboundHandlerAdapter
{
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
    {
        System.out.println("write:"+msg);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("flush");
    }
}
