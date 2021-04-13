package model.message;

import com.alibaba.fastjson.JSONObject;
import service.Status;
import service.UserService;

public class RegisterMessage extends Message
{
    private String userName;
    private String password;

    public RegisterMessage(String uuid, JSONObject data)
    {
        super(uuid);
        String userName = data.getString("userName");
        String password = data.getString("password");
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status = UserService.signIn(userName, password);
        root.put("status",status.toString());

        String response = root.toJSONString();
        return response;


    }
}
