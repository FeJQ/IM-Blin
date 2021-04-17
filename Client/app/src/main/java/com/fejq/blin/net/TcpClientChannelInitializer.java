package com.fejq.blin.net;

import android.util.Log;

import com.fejq.blin.config.Const;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.message.Request;

import org.json.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
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
                Client.getInstance().setConnected(true);
                Log.e("Blin","服务器连接成功");
                // 检测消息队列,并发送消息
                new Thread(() -> {
                    while (Client.getInstance().sendable())
                    {
                        Request request = Client.getInstance().popMessage();
                        if (request == null)
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
                            Log.i("uuid", request.getUuid());
                            // 发送消息
                            ctx.writeAndFlush(Unpooled.buffer().writeBytes(request.getContent().getBytes()));

                            // 等待响应
                            long startTimeMillis = System.currentTimeMillis();
                            Request.OnRecvListener onRecvListener = request.getOnRecvListener();

                            while (onRecvListener != null)
                            {
                                try
                                {
                                    if (System.currentTimeMillis() - startTimeMillis >= Const.TIMEOUT)
                                    {
                                        // 超时
                                        int code = -1;
                                        String msg = "请求超时";
                                        JSONObject data = new JSONObject();
                                        onRecvListener.onRecv(code, msg, data);
                                        break;
                                    }
                                    JSONObject status = Client.getInstance().getRecvMap().get(request.getUuid());
                                    if (status == null)
                                    {
                                        Thread.sleep(20);
                                        continue;
                                    }
                                    int code = status.getInt("code");
                                    String msg = status.getString("message");
                                    JSONObject data = status.getJSONObject("data");
                                    onRecvListener.onRecv(code, msg, data);
                                    Client.getInstance().getRecvMap().remove(request.getUuid());
                                    break;
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
                ByteBuf buf=(ByteBuf)msg;

                Log.i("Blin",msg.getClass().getName());
                Log.i("Blin", buf.toString(CharsetUtil.UTF_8));
                String message=buf.toString(CharsetUtil.UTF_8);
                // 解析收到的消息
                JSONObject root = new JSONObject(message);

                String uuid = root.getString("uuid");
                JSONObject status = root.getJSONObject("status");

                // 将收到的消息添加到Map
                Client.getInstance().getRecvMap().put(uuid, status);
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
