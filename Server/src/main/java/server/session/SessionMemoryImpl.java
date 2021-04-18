package server.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMemoryImpl implements Session {

    private final Map<Integer, Channel> userIdChannelMap = new ConcurrentHashMap<>();
    private final Map<Channel, Integer> channelUserIdMap = new ConcurrentHashMap<>();
    private final Map<Channel,Map<String,Object>> channelAttributesMap = new ConcurrentHashMap<>();

    @Override
    public void bind(int userId,Channel channel) {
        userIdChannelMap.put(userId, channel);
        channelUserIdMap.put(channel, userId);
        channelAttributesMap.put(channel, new ConcurrentHashMap<>());
    }

    @Override
    public void unbind(Channel channel) {
        int userId  = channelUserIdMap.remove(channel);
        userIdChannelMap.remove(userId);
        channelAttributesMap.remove(channel);
    }

    @Override
    public Object getAttribute(Channel channel, String name) {
        return channelAttributesMap.get(channel).get(name);
    }

    @Override
    public void setAttribute(Channel channel, String name, Object value) {
        channelAttributesMap.get(channel).put(name, value);
    }

    @Override
    public Channel getChannel(int userId) {
        return userIdChannelMap.get(userId);
    }

    @Override
    public String toString() {
        return userIdChannelMap.toString();
    }
}
