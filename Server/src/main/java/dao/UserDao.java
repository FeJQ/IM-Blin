package dao;

import model.sql.ChatEntry;
import model.sql.FriendMessage;
import model.sql.GroupMessage;
import model.sql.UserInfo;
import util.DBHelper;
import util.ResultSetUtil;
import util.TokenUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao
{
    public static List<UserInfo> selectUserByUserName(String userName)
    {
        String sql = "select * from user_info where userName=?";
        List<Object> params = new ArrayList<>();
        params.add(userName);
        ResultSet res = DBHelper.executeQuery(sql, params);
        List<UserInfo> userInfoList = ResultSetUtil.toList(res, UserInfo.class);
        return userInfoList;
    }

    public static List<UserInfo> selectUserByUserId(int userId)
    {
        String sql = "select * from user_info where userId=?";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        ResultSet res = DBHelper.executeQuery(sql, params);
        List<UserInfo> userInfoList = ResultSetUtil.toList(res, UserInfo.class);
        return userInfoList;
    }

    public static int updateUserToken(int userId, TokenUtil.Token token)
    {
        String sql = "update user_info set token=?,tokenFailureTime=? where userId=?";
        List<Object> params = new ArrayList<>();
        params.add(token.getValue());
        params.add(token.getFailureTime());
        params.add(userId);
        int count = DBHelper.executeOperate(sql, params);
        return count;
    }

    public static List<UserInfo> selectUserByToken(String token)
    {
        String sql = "select * from user_info where token=?";
        List<Object> params = new ArrayList<>();
        params.add(token);
        ResultSet resultSet = DBHelper.executeQuery(sql, params);
        List<UserInfo> userInfoList = ResultSetUtil.toList(resultSet, UserInfo.class);
        return userInfoList;
    }

    public static int insertUser(String userName, String password)
    {
        String sql = "insert into user_info(userName,password) values(?,?)";
        List<Object> params = new ArrayList<>();
        params.add(userName);
        params.add(password);
        int count = DBHelper.executeOperate(sql, params);
        return count;
    }

    public static int clearToken(int userId)
    {
        String sql = "update user_info set token=NULL,tokenFailureTime=NULL where userId=?";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        int count = DBHelper.executeOperate(sql, params);
        return count;
    }

    public static List<ChatEntry> getChatEntryList(int userId) throws SQLException
    {
        String sql = "select chatId,chat_entry.userId,entryId,entryType,userName as entryName from user_info inner join chat_entry on user_info.userId=chat_entry.entryId  where chat_entry.userId=? and user_info.userId in (select chat_entry.entryId from chat_entry where chat_entry.entryType='联系人')";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        ResultSet resultSet = DBHelper.executeQuery(sql, params);
        List<ChatEntry> userChatEntryList = ResultSetUtil.toList(resultSet, ChatEntry.class);


        sql = "select chatId,chat_entry.userId,entryId,entryType,groupName as entryName from group_info inner join chat_entry on group_info.groupId=chat_entry.entryId  where chat_entry.userId=? and group_info.groupId in (select chat_entry.entryId from chat_entry where chat_entry.entryType='群组')";
        params = new ArrayList<>();
        params.add(userId);
        resultSet = DBHelper.executeQuery(sql, params);
        List<ChatEntry> groupChatEntryList = ResultSetUtil.toList(resultSet, ChatEntry.class);

        List<ChatEntry> chatEntryList = new ArrayList<>();
        for (ChatEntry chatEntry : userChatEntryList)
        {
            chatEntryList.add(chatEntry);
        }
        for (ChatEntry chatEntry : groupChatEntryList)
        {
            chatEntryList.add(chatEntry);
        }
        return chatEntryList;
    }

    public static List<FriendMessage> getFriendChatHistory(int userId, int friendId)
    {
        final int maxCount = 100;
        String sql = "select *from (select *,userName as senderName from friend_message inner join user_info on friend_message.senderId=user_info.userId where (senderId=? and receiverId=?) or (senderId=? and receiverId=?)  order by sendTime desc limit ? )as ass order by sendTime";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(friendId);
        params.add(friendId);
        params.add(userId);
        params.add(maxCount);
        ResultSet resultSet = DBHelper.executeQuery(sql, params);
        List<FriendMessage> friendMessageList=new ArrayList<>();
        try
        {
            while (resultSet.next())
            {
                FriendMessage friendMessage=new FriendMessage();
                friendMessage.setFriendMessageId(resultSet.getInt("friendMessageId"));
                friendMessage.setSenderId(resultSet.getInt("senderId"));
                friendMessage.setReceiverId(resultSet.getInt("receiverId"));
                friendMessage.setMessageTypeId(resultSet.getInt("messageTypeId"));
                friendMessage.setSendTime(resultSet.getTimestamp("sendTime"));
                friendMessage.setHaveRead(resultSet.getBoolean("haveRead"));
                friendMessage.setContent(resultSet.getString("content"));
                friendMessage.setSenderName(resultSet.getString("senderName"));
                friendMessageList.add(friendMessage);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return friendMessageList;
    }

    public static List<GroupMessage> getGroupChatHistory(int userId,int groupId)
    {
        final int maxCount = 100;
        String sql="select * from group_message where groupId=1 order by sendTime desc limit ?";
        return null;
    }

    public static int insertFriendMessage(int userId,int friendId,String content)
    {
        String sql="insert into friend_message(senderId,receiverId,messageTypeId,content) values(?,?,(select messageTypeId from message_type where typeName='文本'),?)";
        List<Object> params = new ArrayList<>();
        params.add(userId);
        params.add(friendId);
        params.add(content);
        int count = DBHelper.executeOperate(sql, params);
        return count;
    }

}
