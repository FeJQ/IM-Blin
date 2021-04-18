package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import io.netty.channel.Channel;
import server.session.SessionFactory;
import service.UserService;

public class LoginFirstMessage extends Message
{
    private String userName;
    private String password;
    private Channel channel;

    public LoginFirstMessage(String uuid, JSONObject data,Channel channel)
    {
        super(uuid);
        this.userName = data.getString("userName");
        this.password = data.getString("password");
        this.channel=channel;
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status = UserService.loginFirst(userName, password,channel);
        root.put("status",status);



        return root.toJSONString();
    }
}
