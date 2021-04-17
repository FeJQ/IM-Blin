package server;

import com.alibaba.fastjson.JSONObject;
import common.Action;
import common.RequestMapping;
import model.message.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class MessageHandler
{
    private String message;

    public MessageHandler(String message)
    {
        this.message = message;
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

        Message message;
        System.out.println("message:"+data.toJSONString());
        switch (action)
        {
            case Action.LOGIN_FIRST:
                message = new LoginFirstMessage(uuid, data);
                response=message.handle();
                break;
            case Action.LOGIN_TOKEN:
                message=new LoginTokenMessage(uuid,data);
                response=message.handle();
                break;
            case Action.REGISTER:
                message=new RegisterMessage(uuid,data);
                response=message.handle();
                break;
            case Action.SEND_SINGLE:
                message=new SendToFriendMessage(uuid,data);
                response=message.handle();
                break;
            case Action.SEND_GROUP:
                break;
            case Action.GET_RECENT_CHAT:
                message= new GetChatListMessage(uuid,data);
                response=message.handle();
                break;
            case Action.GET_FRIEND:
                break;
            case Action.GET_CHAT_HISTORY:
                message=new GetChatHistoryMessage(uuid,data);
                response=message.handle();
            default:break;
        }
        return response;
    }
}
