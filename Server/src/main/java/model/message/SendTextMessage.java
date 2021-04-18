package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import service.UserService;


public class SendTextMessage extends Message
{
    private int userId;
    private String token;
    private int type;
    private int chatId;
    private String chatType;
    private String content;

    public SendTextMessage(String uuid, JSONObject data)
    {
        super(uuid);
        this.userId = data.getInteger("senderId");
        this.token = data.getString("token");
        this.chatId = data.getInteger("chatId");
        this.type = data.getInteger("type");
        this.chatType = data.getString("chatType");
        this.content = data.getString("content");
    }

    @Override
    public String handle()
    {
        Status status=null;
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        if(chatType.equals("联系人"))
        {
             status = UserService.sendMessageToFriend(userId, token, chatId, content);
        }
        else if(chatType.equals("群组"))
        {

        }
        root.put("status", status);
        return root.toJSONString();
    }
}
