package model;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

public class Client
{
    @Getter
    @Setter
    private int userId;
    @Getter
    @Setter
    private Channel channel;
}
