package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import service.UserService;

public class GetChatHistoryMessage extends Message
{
    private int userId;
    private String token;
    private int chatId;
    private String chatType;

    public GetChatHistoryMessage(String uuid, JSONObject data)
    {
        super(uuid);
        this.userId = data.getInteger("userId");
        this.token = data.getString("token");
        this.chatId = data.getInteger("chatId");
        this.chatType = data.getString("chatType");
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status= UserService.getChatHistory(userId,token,chatId,chatType);

        root.put("status",status);
        return root.toJSONString();
    }
}
