package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import service.UserService;
import util.TokenUtil;

public class SendToFriendMessage extends Message
{
    private int userId;
    private String token;
    private int friendId;
    private String content;
    public SendToFriendMessage(String uuid, JSONObject data)
    {
        super(uuid);
        this.userId = data.getInteger("userId");
        this.token = data.getString("token");
        this.friendId = data.getInteger("friendId");
        this.content = data.getString("content");
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status= UserService.sendMessageToFriend(userId,token,friendId,content);

        root.put("status",status);
        return root.toJSONString();
    }
}
