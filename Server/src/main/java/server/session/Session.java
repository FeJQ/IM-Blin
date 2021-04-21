package server.session;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 会话管理接口
 */
public interface Session {

    /**
     * 绑定会话
     * @param ctx 哪个 channel 要绑定会话
     * @param userId 会话绑定用户
     */
    void bind(int userId, ChannelHandlerContext ctx);

    /**
     * 解绑会话
     * @param ctx 哪个 channel 要解绑会话
     */
    void unbind(ChannelHandlerContext ctx);

    /**
     * 获取属性
     * @param ctx 哪个 channel
     * @param name 属性名
     * @return 属性值
     */
    Object getAttribute(ChannelHandlerContext ctx, String name);

    /**
     * 设置属性
     * @param ctx 哪个 channel
     * @param name 属性名
     * @param value 属性值
     */
    void setAttribute(ChannelHandlerContext ctx, String name, Object value);

    /**
     * 根据用户Id获取 channel
     * @param userId 用户Id
     * @return channel
     */
    ChannelHandlerContext getChannel(int userId);
}
