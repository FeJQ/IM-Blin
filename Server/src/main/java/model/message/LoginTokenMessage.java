package model.message;

import com.alibaba.fastjson.JSONObject;
import service.Status;
import service.UserService;

public class LoginTokenMessage extends Message
{
    private String token;
    public LoginTokenMessage(String uuid, JSONObject data)
    {
        super(uuid);
        String token = data.getString("token");
        this.token = token;
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status= UserService.LoginWithToken(token);

        root.put("status",status.toString());
        return root.toJSONString();
    }
}
