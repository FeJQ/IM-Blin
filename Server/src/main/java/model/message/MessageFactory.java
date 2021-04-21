package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Action;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class MessageFactory
{
    public static Message make(int action, String uuid, JSONObject data, ChannelHandlerContext ctx)
    {
        Message message=null;
        switch (action)
        {
            case common.Action.LOGIN_FIRST:
                message = new LoginFirstMessage(uuid, data,ctx);
                break;
            case common.Action.LOGIN_TOKEN:
                message=new LoginTokenMessage(uuid,data,ctx);
                break;
            case common.Action.REGISTER:
                message=new RegisterMessage(uuid,data);
                break;
            case common.Action.SEND_SINGLE:
                message=new SendTextMessage(uuid,data);
                break;
            case common.Action.SEND_GROUP:
                break;
            case common.Action.GET_RECENT_CHAT:
                message= new GetChatListMessage(uuid,data);
                break;
            case common.Action.GET_FRIEND:
                break;
            case common.Action.GET_CHAT_HISTORY:
                message=new GetChatHistoryMessage(uuid,data);
            default:break;
        }
        return message;
    }
}
