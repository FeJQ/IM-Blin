package com.fejq.blin.net;

import android.content.Context;

import com.fejq.blin.config.Const;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.message.Message;

import org.json.JSONException;
import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class TcpClientChannelInitializer extends ChannelInitializer
{
    @Override
    protected void initChannel(Channel channel) throws Exception
    {
        ChannelPipeline pipeline = channel.pipeline();
        //pipeline.addLast(new StringDecoder());
        //pipeline.addLast(new StringEncoder());

        pipeline.addLast(new ChannelInboundHandlerAdapter()
        {

        /**
         * 通道被激活
         *
         * @param ctx 上下文
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx)
        {
            // 检测消息队列,并发送消息
            new Thread(() -> {
                while (Client.getInstance().sendable())
                {
                    Message message = Client.getInstance().popMessage();
                    if (message == null)
                    {
                        try
                        {
                            Thread.sleep(20);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    new Thread(() -> {
                        // 发送消息
                        ctx.writeAndFlush(Unpooled.buffer().writeBytes(message.toString().getBytes()));

                        // 等待响应
                        long startTimeMillis = System.currentTimeMillis();
                        Message.OnRecvListener onRecvListener = message.getOnRecvListener();

                        while (onRecvListener != null)
                        {
                            try
                            {
                                if (System.currentTimeMillis() - startTimeMillis >= Const.TIMEOUT)
                                {
                                    // 超时
                                    int code=-1;
                                    String msg="请求超时";
                                    Object data=null;
                                    JSONObject status=new JSONObject();
                                    status.put("code",code);
                                    status.put("message",msg);
                                    status.put("data",data);
                                    onRecvListener.onRecv(code, msg,status);
                                    return;
                                }
                                JSONObject status = Client.getInstance().getRecvMap().get(message.getUuid());
                                if (status == null)
                                {
                                    Thread.sleep(20);
                                    continue;
                                }
                                int code=status.getInt("code");
                                String msg=status.getString("message");
                                onRecvListener.onRecv(code, msg,status);
                                return;
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }).start();
        }


        /**
         * 收到服务器的消息
         *
         * @param ctx 上线问
         * @param msg 消息
         * @throws Exception
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
        {
            // 解析收到的消息
            JSONObject root = new JSONObject((String) msg);
            String uuid = root.getString("uuid");
            JSONObject status = root.getJSONObject("status");

            // 将收到的消息添加到Map
            Client.getInstance().getRecvMap().put(uuid, status);

            ByteBuf buf = (ByteBuf) msg;
            System.out.println(msg.getClass());
            System.out.println("Server:" + buf.toString(CharsetUtil.UTF_8));
        }

        /**
         * 发生异常
         *
         * @param ctx   上下文
         * @param cause 异常信息
         * @throws Exception
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
        {
            System.out.println(cause.getMessage());
            ctx.close();
        }
        });
    }
}
