package server;

import com.alibaba.fastjson.JSONObject;
import common.Action;
import common.RequestMapping;
import io.netty.channel.Channel;
import model.message.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler
{
    private String message;
    private Channel channel;

    public MessageHandler(String message, Channel channel)
    {
        this.message = message;
        this.channel=channel;
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

        System.out.println("message:"+data.toJSONString());
        Message messageObj = MessageFactory.make(action, uuid, data,channel);
        response=messageObj.handle();
        return response;
    }
}
