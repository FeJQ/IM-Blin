package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import service.UserService;

public class RegisterMessage extends Message
{
    private String userName;
    private String password;

    public RegisterMessage(String uuid, JSONObject data)
    {
        super(uuid);
        this.userName = data.getString("userName");
        this.password = data.getString("password");
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status = UserService.register(userName, password);
        root.put("status",status.toString());

        String response = root.toJSONString();
        return response;


    }
}
