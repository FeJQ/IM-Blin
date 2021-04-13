package service;

import common.RequestMapping;
import util.DBHelper;
import util.TokenUtil;
import util.TokenUtil.Token;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.Status.Code;

public class UserService
{
    /**
     * 用户名密码登录
     *
     * @param _userName 用户名
     * @param _password 密码
     * @return 状态信息
     * @throws SQLException
     */
    public static Status LoginFirst(String _userName, String _password)
    {
        Status status;
        try
        {
            String sql = "select * from user_info where userName=?";
            List<Object> params = new ArrayList<>();
            params.add(_userName);
            ResultSet res = DBHelper.executeQuery(sql, params);
            int i=0;
            while (res.next())
            {
                i++;
            }


            if (res.getRow() != 1)
            {
                status = new Status(Code.USER_NOT_EXIST);
                return status;
            }
            String password = res.getString("password");
            if (!password.equals(_password))
            {
                status = new Status(Code.INCORRECT_USER_NAME_OR_PASSWORD);
                return status;
            }
            Token token = TokenUtil.make(_userName, _password);
            sql = "update user_info set token=?,tokenFailureTime=? where userId=?";
            int userId = res.getInt("userId");
            params = new ArrayList<>();
            params.add(token.getValue());
            params.add(token.getFailureTime());
            params.add(userId);
            int count = DBHelper.executeOperate(sql, params);
            if (count != 1)
            {
                status = new Status(Code.UNKNOWN_ERROR);
                return status;
            }
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("token", token.getValue());
            status = new Status(Code.OK.code(), "登录成功", dataMap);
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
     * @param _token
     * @return 状态信息
     * @throws SQLException
     */
    public static Status LoginWithToken(String _token)
    {
        Status status;
        if (!TokenUtil.verify(_token))
        {
            status = new Status(Code.TOKEN_FAILURE);
            return status;
        }
        status = new Status(Code.OK, "登录成功");
        return status;
    }

    /**
     * 注册账户
     *
     * @param userName 用户名
     * @param password 密码
     * @return 状态信息
     */
    public static Status signIn(String userName, String password)
    {
        Status status;
        try
        {
            if (userName.length() > 32 || userName.length() < 1)
            {
                status = new Status(Code.USER_NAME_NOT_VALID);
                return status;
            }
            String sql = "select * from user_info where userName=?";
            List<Object> params = new ArrayList<>();
            params.add(userName);
            ResultSet res = DBHelper.executeQuery(sql, params);
            if (res.getRow() > 0)
            {
                status = new Status(Code.USER_NAME_ALREADY_EXIST);
                return status;
            }
            sql = "insert into user_info(userName,password) values(?,?)";
            params.add(userName);
            params.add(password);
            int count = DBHelper.executeOperate(sql, params);
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
     * @param token
     * @return 状态信息
     */
    public static Status logout(String token)
    {
        Status status;
        try
        {
            String sql = "select * from user_info where token=?";
            List<Object> params = new ArrayList<>();
            params.add(token);
            ResultSet resultSet = DBHelper.executeQuery(sql, params);
            if (resultSet.getRow() == 0)
            {
                status = new Status(Code.INVALID_TOKEN);
                return status;
            }
            sql = "update user_info set token=NULL,tokenFailureTime=NULL where token=?";
            params = new ArrayList<>();
            params.add(token);
            int count = DBHelper.executeOperate(sql, params);
            if (count > 0)
            {
                status = new Status(Code.OK, "登出成功");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        status = new Status(Code.UNKNOWN_ERROR);
        return status;
    }
}
