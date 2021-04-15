package dao;

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

    public static int insertUser(String userName,String password)
    {
        String sql = "insert into user_info(userName,password) values(?,?)";
        List<Object> params=new ArrayList<>();
        params.add(userName);
        params.add(password);
        int count = DBHelper.executeOperate(sql, params);
        return count;
    }

    public static int clearToken(int userId)
    {
        String sql = "update user_info set token=NULL,tokenFailureTime=NULL where userId=?";
        List<Object> params=new ArrayList<>();
        params = new ArrayList<>();
        params.add(userId);
        int count = DBHelper.executeOperate(sql, params);
        return count;
    }
}
