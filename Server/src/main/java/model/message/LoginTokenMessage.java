package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import service.UserService;

public class LoginTokenMessage extends Message
{
    private int userId;
    private String token;
    public LoginTokenMessage(String uuid, JSONObject data)
    {
        super(uuid);
        int userId=data.getInteger("userId");
        String token = data.getString("token");
        this.token = token;
        this.userId=userId;
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status= UserService.loginWithToken(userId,token);

        root.put("status",status.toString());
        return root.toJSONString();
    }
}
