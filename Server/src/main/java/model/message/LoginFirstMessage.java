package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import service.UserService;

public class LoginFirstMessage extends Message
{
    private String userName;
    private String password;

    public LoginFirstMessage(String uuid, JSONObject data)
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

        Status status = UserService.loginFirst(userName, password);
        root.put("status",status);

        return root.toJSONString();
    }
}
