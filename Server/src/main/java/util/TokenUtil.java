package util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.*;

public class TokenUtil
{
    public static class Token
    {
        private String value;
        private Date failureTime;

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        public Date getFailureTime()
        {
            return failureTime;
        }

        public void setFailureTime(Date failureTime)
        {
            this.failureTime = failureTime;
        }
    }

    //设置过期时间
    private static final long EXPIRE_DATE = 1000 * 60 * 60 * 24 * 30;
    //token秘钥
    private static final String TOKEN_SECRET = "W5EQIUBFKKJB9JHK020RQWE";

    /**
     * 根据用户名和密码,生成一个Token
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static Token make(String username, String password)
    {
        Token token = new Token();
        try
        {
            //过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            //携带username，password信息，生成签名
            String tokenValue = JWT.create()
                    .withHeader(header)
                    .withClaim("username", username)
                    .withClaim("password", password).withExpiresAt(date)
                    .sign(algorithm);
            token.setValue(tokenValue);
            token.setFailureTime(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return token;
    }

    /**
     * @desc 验证token，通过返回true
     * @create 2019/1/18/018 9:39
     * @params [token]需要校验的串
     **/
    public static boolean verify(String _token)
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(_token);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
