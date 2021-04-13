import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerChannelHandler extends SimpleChannelInboundHandler<String>
{
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("channelActive:" + ctx.channel().remoteAddress());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("handlerAdded:" + ctx.channel().remoteAddress());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        //ByteBuf buf=(ByteBuf)msg;
        System.out.println(msg.getClass());
        System.out.println("channelRead:" + ctx.channel().remoteAddress() + "-" + msg);
        if (msg instanceof String)
        {
            new Thread(() -> {
                MessageHandler messageHandler = new MessageHandler((String) msg);
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
