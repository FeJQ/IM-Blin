package service;

import com.alibaba.fastjson.JSON;
import common.Action;
import common.Status;
import dao.UserDao;
import io.netty.channel.Channel;
import model.sql.ChatEntry;
import model.sql.FriendMessage;
import model.sql.UserInfo;
import server.session.SessionFactory;
import util.TokenUtil;
import util.TokenUtil.Token;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Status.Code;
import util.UuidUtil;

public class UserService extends BaseService
{
    /**
     * 用户名密码登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param channel 该用户的通道
     * @return 状态信息
     * @throws SQLException
     */
    public static Status loginFirst(String userName, String password, Channel channel)
    {
        Status status;
        try
        {
            // 检查用户名是否存在
            List<UserInfo> userInfoList = UserDao.selectUserByUserName(userName);
            UserInfo userInfo = userInfoList.get(0);

            if (userInfoList.size() == 0)
            {
                status = new Status(Code.USER_NOT_EXIST);
                return status;
            }
            // 检查密码是否正确
            if (!password.equals(userInfo.getPassword()))
            {
                status = new Status(Code.INCORRECT_USER_NAME_OR_PASSWORD);
                return status;
            }
            // 生成Token
            Token token = TokenUtil.make(userName, password);
            int count = UserDao.updateUserToken(userInfo.getUserId(), token);
            if (count != 1)
            {
                status = new Status(Code.UNKNOWN_ERROR);
                return status;
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("userId", userInfo.getUserId());
            dataMap.put("token", token.getValue());
            status = new Status(Code.OK.code(), "登录成功", dataMap);
            // 绑定用户 session
            SessionFactory.getSession().bind(userInfo.getUserId(), channel);
            return status;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        status = new Status(Code.UNKNOWN_ERROR);
        return status;
    }


    /**
     * 自动登录
     *
     * @param userId 用户Id
     * @param token  令牌
     * @param channel 该用户的通道
     * @return 状态信息
     * @throws SQLException
     */
    public static Status loginWithToken(int userId, String token, Channel channel)
    {
        Status status;
        int result = checkToken(userId, token);
        if (result == USER_NOT_EXIST)
        {
            return new Status(Code.USER_NOT_EXIST);
        }
        else if (result == OK)
        {
            // 绑定用户session
            SessionFactory.getSession().bind(userId,channel);
            return new Status(Code.OK, "登录成功");
        }
        else
        {
            return new Status(Code.TOKEN_FAILURE);
        }
    }

    /**
     * 注册账户
     *
     * @param userName 用户名
     * @param password 密码
     * @return 状态信息
     */
    public static Status register(String userName, String password)
    {
        Status status;
        try
        {
            // 检查用户名密码格式
            if (userName.length() > 32 || userName.length() < 1)
            {
                status = new Status(Code.USER_NAME_NOT_VALID);
                return status;
            }
            // 检查用户名是否已被注册
            List<UserInfo> userInfoList = UserDao.selectUserByUserName(userName);
            if (userInfoList.size() > 0)
            {
                status = new Status(Code.USER_NAME_ALREADY_EXIST);
                return status;
            }
            // 注册用户
            int count = UserDao.insertUser(userName, password);
            if (count > 0)
            {
                status = new Status(Code.OK, "注册成功");
                return status;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        status = new Status(Code.UNKNOWN_ERROR);
        return status;
    }

    /**
     * 登出
     *
     * @param userId
     * @param token
     * @return 状态信息
     */
    public static Status logout(int userId, String token)
    {
        Status status;
        try
        {
            if (checkToken(userId, token) != OK)
            {
                return new Status(Code.TOKEN_FAILURE);
            }
            List<UserInfo> userInfoList = UserDao.selectUserByUserId(userId);
            if (userInfoList.size() == 0)
            {
                return new Status(Code.USER_NOT_EXIST);
            }
            if (userInfoList.get(0).getToken() != null)
            {
                int count = UserDao.clearToken(userId);
                if (count > 0)
                {
                    status = new Status(Code.OK, "登出成功");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        status = new Status(Code.UNKNOWN_ERROR);
        return status;
    }


    /**
     * 获取消息列表
     *
     * @param userId 用户Id
     * @param token  用户token
     * @return
     */
    public static Status getChatList(int userId, String token)
    {
        try
        {
            if (checkToken(userId, token) != OK)
            {
                return new Status(Code.TOKEN_FAILURE);
            }
            List<ChatEntry> chatEntryList = UserDao.getChatEntryList(userId);

            Map<String, Object> dataMap = new HashMap<>();
            List<Object> chatList = new ArrayList<>();
            for (int i = 0; i < chatEntryList.size(); i++)
            {
                Map<String, Object> chatListMap = new HashMap<>();
                chatListMap.put("chatId", chatEntryList.get(i).getEntryId());
                chatListMap.put("chatType", chatEntryList.get(i).getEntryType());
                chatListMap.put("chatName", chatEntryList.get(i).getEntryName());
                chatListMap.put("lastMessage", chatEntryList.get(i).getChatId());
                chatListMap.put("lastMessageTime", chatEntryList.get(i).getChatId());
                chatList.add(chatListMap);
            }
            dataMap.put("chatList", chatList);
            return new Status(Code.OK.code(), "获取成功", dataMap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return new Status(Code.UNKNOWN_ERROR);
    }

    /**
     * 获取历史消息
     *
     * @param userId   用户Id
     * @param token    token
     * @param chatId   联系人或群组Id
     * @param chatType 群组 or 联系人
     * @return
     */
    public static Status getChatHistory(int userId, String token, int chatId, String chatType)
    {
        if (checkToken(userId, token) != OK)
        {
            return new Status(Code.TOKEN_FAILURE);
        }
        if (chatType.equals("群组"))
        {

        }
        else if (chatType.equals("联系人"))
        {
            List<FriendMessage> friendMessageList = UserDao.getFriendChatHistory(userId, chatId);
            Map<String, Object> dataMap = new HashMap<>();
            List<Object> messageList = new ArrayList<>();
            for (int i = 0; i < friendMessageList.size(); i++)
            {
                Map<String, Object> messageListMap = new HashMap<>();
                messageListMap.put("senderId", friendMessageList.get(i).getSenderId());
                messageListMap.put("senderName", friendMessageList.get(i).getSenderName());
                messageListMap.put("content", friendMessageList.get(i).getContent());
                messageListMap.put("sendTime", friendMessageList.get(i).getSendTime().getTime());
                messageList.add(messageListMap);
            }
            dataMap.put("chatHistoryList", messageList);
            return new Status(Code.OK.code(), "获取成功", dataMap);
        }
        return new Status(Code.UNKNOWN_ERROR);
    }

    /**
     * 发送私聊消息
     *
     * @param userId   用户Id
     * @param token    token
     * @param friendId 好友Id
     * @param content  内容
     * @return
     */
    public static Status sendMessageToFriend(int userId, String token, int friendId, String content)
    {
        if (checkToken(userId, token) != OK)
        {
            return new Status(Code.TOKEN_FAILURE);
        }
        FriendMessage friendMessage = UserDao.insertFriendMessage(userId, friendId, content);
        if (friendMessage!=null)
        {
            Channel channel = SessionFactory.getSession().getChannel(userId);
            if(channel!=null)
            {
                Map<String,Object> root=new HashMap<>();
                root.put("uuid",UuidUtil.make());

                Map<String,Object> status=new HashMap<>();
                root.put("status",status);
                status.put("code",Action.RECEIVED_FRIEND_MESSAGE);
                status.put("message","收到好友消息");

                Map<String,Object> data=new HashMap<>();
                status.put("data",data);
                data.put("senderId",friendMessage.getSenderId());
                data.put("receiverId",friendMessage.getReceiverId());
                data.put("content",friendMessage.getContent());
                data.put("time",friendMessage.getSendTime());
                String jsonString = JSON.toJSONString(root);
                channel.writeAndFlush(jsonString);
            }
            return new Status(Code.OK, "发送成功");
        }
        return new Status(Code.UNKNOWN_ERROR, "发送失败");
    }
}
