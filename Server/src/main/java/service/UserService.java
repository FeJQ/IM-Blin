package service;

import common.Status;
import dao.UserDao;
import model.sql.UserInfo;
import util.TokenUtil;
import util.TokenUtil.Token;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Status.Code;

public class UserService extends BaseService
{
    /**
     * 用户名密码登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 状态信息
     * @throws SQLException
     */
    public static Status loginFirst(String userName, String password)
    {
        Status status;
        try
        {
            // 检查用户名是否存在
            List<UserInfo> userInfoList = UserDao.selectUserByUserName(userName);
            UserInfo userInfo=userInfoList.get(0);

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
            dataMap.put("userId",userInfo.getUserId());
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
     * @param userId 用户Id
     * @param token 令牌
     * @return 状态信息
     * @throws SQLException
     */
    public static Status loginWithToken(int userId, String token)
    {
        Status status;
        int result = checkToken(userId,token);
        if(result==USER_NOT_EXIST)
        {
            return new Status(Code.USER_NOT_EXIST);
        }
        else if (result == OK)
        {
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
            if(checkToken(userId,token)!=OK)
            {
                return new Status(Code.TOKEN_FAILURE);
            }
            List<UserInfo> userInfoList = UserDao.selectUserByUserId(userId);
            if (userInfoList.size() == 0)
            {
                return new Status(Code.USER_NOT_EXIST);
            }
            if(userInfoList.get(0).getToken()!=null)
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


}
