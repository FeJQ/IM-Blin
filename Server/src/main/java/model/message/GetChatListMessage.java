package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import service.UserService;

public class GetChatListMessage extends Message
{
    private int userId;
    private String token;
    public GetChatListMessage(String uuid, JSONObject data)
    {
        super(uuid);
        this.token = data.getString("token");
        this.userId=data.getInteger("userId");;
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status= UserService.getChatList(userId,token);

        root.put("status",status);
        return root.toJSONString();
    }
}
