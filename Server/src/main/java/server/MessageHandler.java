package server;

import com.alibaba.fastjson.JSONObject;
import common.Action;
import common.RequestMapping;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import model.message.*;

import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler
{
    private String message;
    private ChannelHandlerContext ctx;

    public MessageHandler(MessageProtocol message, ChannelHandlerContext ctx)
    {
        this.message = new String(message.getContent(), Charset.forName("utf-8"));
        this.ctx=ctx;
    }

    /**
     * 消息响应的入口
     * 根据不同的消息类型,做不同的处理
     * @return 消息的响应内容
     */
    public String makeResponse()
    {
        JSONObject json = JSONObject.parseObject(message);
        String uuid = json.getString("uuid");
        int action = json.getIntValue("action");
        JSONObject data = json.getJSONObject("data");
        String response = null;

        //System.out.println("message:"+data.toJSONString());
        Message messageObj = MessageFactory.make(action, uuid, data,ctx);
        response=messageObj.handle();
        return response;
    }
}
