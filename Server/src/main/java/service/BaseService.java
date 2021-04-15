package service;

import com.sun.javafx.scene.control.behavior.TextInputControlBehavior;
import dao.UserDao;
import model.sql.UserInfo;
import util.TokenUtil;

import java.util.List;

public class BaseService
{
    public static final int OK=0;
    public static final int USER_NOT_EXIST=1;
    public static final int TOKEN_VERIFY_FAILED=2;
    public static final int TOKEN_NOT_EQUALS=3;
    public static final int TOKEN_OVERDUE=4;

    /**
     * 检查token的有效性
     * @param userId 用户Id
     * @param token 令牌
     * @return 状态
     */
    protected static int checkToken(int userId, String token)
    {
        // 检查用户Id是否存在
        List<UserInfo> userInfoList = UserDao.selectUserByUserId(userId);
        if (userInfoList.size() == 0)
        {
            return USER_NOT_EXIST;
        }
        // 检查Token的有效性
        UserInfo userInfo = userInfoList.get(0);
        if(!TokenUtil.verify(token))
        {
            return TOKEN_VERIFY_FAILED;
        }
        if(!userInfo.getToken().equals(token))
        {
            return TOKEN_NOT_EQUALS;
        }
        if(!(userInfo.getTokenFailureTime().getTime() - System.currentTimeMillis() > 0))
        {
            return TOKEN_OVERDUE;
        }
        return OK;
    }

}
