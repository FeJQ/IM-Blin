package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import server.session.SessionFactory;
import service.UserService;

public class LoginFirstMessage extends Message
{
    private String userName;
    private String password;
    private ChannelHandlerContext ctx;

    public LoginFirstMessage(String uuid, JSONObject data, ChannelHandlerContext ctx)
    {
        super(uuid);
        this.userName = data.getString("userName");
        this.password = data.getString("password");
        this.ctx=ctx;
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status = UserService.loginFirst(userName, password,ctx);
        root.put("status",status);



        return root.toJSONString();
    }
}
