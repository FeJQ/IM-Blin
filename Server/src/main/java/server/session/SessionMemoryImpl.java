package server.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMemoryImpl implements Session {

    private final Map<Integer, ChannelHandlerContext> userIdChannelMap = new ConcurrentHashMap<>();
    private final Map<ChannelHandlerContext, Integer> channelUserIdMap = new ConcurrentHashMap<>();
    private final Map<ChannelHandlerContext,Map<String,Object>> channelAttributesMap = new ConcurrentHashMap<>();

    @Override
    public void bind(int userId, ChannelHandlerContext ctx) {
        userIdChannelMap.put(userId, ctx);
        channelUserIdMap.put(ctx, userId);
        channelAttributesMap.put(ctx, new ConcurrentHashMap<>());
    }

    @Override
    public void unbind(ChannelHandlerContext ctx) {
        int userId  = channelUserIdMap.remove(ctx);
        userIdChannelMap.remove(userId);
        channelAttributesMap.remove(ctx);
    }

    @Override
    public Object getAttribute(ChannelHandlerContext ctx, String name) {
        return channelAttributesMap.get(ctx).get(name);
    }

    @Override
    public void setAttribute(ChannelHandlerContext ctx, String name, Object value) {
        channelAttributesMap.get(ctx).put(name, value);
    }

    @Override
    public ChannelHandlerContext getChannel(int userId) {
        return userIdChannelMap.get(userId);
    }

    @Override
    public String toString() {
        return userIdChannelMap.toString();
    }
}
