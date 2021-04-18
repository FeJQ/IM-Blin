package server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import server.session.SessionFactory;

public class InServerChannelHandler extends SimpleChannelInboundHandler<String>
{
    /**
     * 通道被激活时触发
     * @param ctx 上下文
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("channelActive:" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("通道被关闭:"+ctx.channel().remoteAddress());
        SessionFactory.getSession().unbind(ctx.channel());
    }

    /**
     * 添加处理器时触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("handlerAdded:" + ctx.channel().remoteAddress());
    }


    /**
     * 接收到客户端的消息时触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        //ByteBuf buf=(ByteBuf)msg;
        System.out.println(msg.getClass());
        System.out.println("channelRead:" + ctx.channel().remoteAddress() + "-" + msg);
        if (msg instanceof String)
        {
            new Thread(() -> {
                MessageHandler messageHandler = new MessageHandler((String) msg,ctx.channel());
                String response = messageHandler.makeResponse();
                ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
            }).start();
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception
    {
        System.out.println("channelRead0:" + ctx.channel().remoteAddress() + "-" + msg);
    }

}
