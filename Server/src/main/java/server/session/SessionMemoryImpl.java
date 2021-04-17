package server.session;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionMemoryImpl implements Session {

    private final Map<Integer, Channel> usernameChannelMap = new ConcurrentHashMap<>();
    private final Map<Channel, Integer> channelUsernameMap = new ConcurrentHashMap<>();
    private final Map<Channel,Map<String,Object>> channelAttributesMap = new ConcurrentHashMap<>();

    @Override
    public void bind(Channel channel, int userId) {
        usernameChannelMap.put(userId, channel);
        channelUsernameMap.put(channel, userId);
        channelAttributesMap.put(channel, new ConcurrentHashMap<>());
    }

    @Override
    public void unbind(Channel channel) {
        int userId = channelUsernameMap.remove(channel);
        usernameChannelMap.remove(userId);
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
        return usernameChannelMap.get(userId);
    }

    @Override
    public String toString() {
        return usernameChannelMap.toString();
    }
}
