package model.message;

import com.alibaba.fastjson.JSONObject;
import common.Status;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import service.UserService;

public class LoginTokenMessage extends Message
{
    private int userId;
    private String token;
    private ChannelHandlerContext ctx;
    public LoginTokenMessage(String uuid, JSONObject data, ChannelHandlerContext ctx)
    {
        super(uuid);
        int userId=data.getInteger("userId");
        String token = data.getString("token");
        this.token = token;
        this.userId=userId;
        this.ctx=ctx;
    }

    @Override
    public String handle()
    {
        JSONObject root = new JSONObject();
        root.put("uuid", getUuid());

        Status status= UserService.loginWithToken(userId,token,ctx);

        root.put("status",status.toString());
        return root.toJSONString();
    }
}
