package com.fejq.blin.communication;

import android.util.Log;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.parts.Localpart;

public class UserClient
{

    public interface OnLoginListener
    {
        void onLogin(boolean isLogin, String message);
    }

    public interface OnRegisterListener
    {
        void onRegister(boolean isRegister, String message);
    }
L
    private boolean isLogin=false;
    public boolean isLogin()
    {
        return isLogin;
    }

    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     */
    public boolean login(String userName, String password)
    {
        //登录
        boolean result = false;
        islogin=false;
        ConnectionUtil connection = new ConnectionUtil();
        try
        {
            if (connection.connect())
            {
                ConnectionUtil.getTcpConnection().login(userName, password);
                result = true;
                islogin=true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 登录
     *
     * @param userName        用户名
     * @param password        密码
     * @param onLoginListener 登录回调
     */
    public void login(String userName, String password, OnLoginListener onLoginListener)
    {
        //登录
        ConnectionUtil connection = new ConnectionUtil();
        connection.connect((isConnected, message) -> {
            XMPPTCPConnection xmppTcpConnection = connection.getTcpConnection();
            xmppTcpConnection.login(userName, password);
            if (xmppTcpConnection.isAuthenticated())
            {
                onLoginListener.onLogin(true, "登录成功");
                islogin=true;
            }
            else
            {
                onLoginListener.onLogin(false, "登录失败");
                islogin=false;
            }
        });
    }


    /**
     * @param userName 用户名
     * @param password 密码
     * @return 是否注册成功
     */
    public boolean register(String userName, String password)
    {
        boolean result = false;
        ConnectionUtil connection = new ConnectionUtil();
        try
        {
            if (connection.connect())
            {
                AccountManager accountManager = AccountManager.getInstance(ConnectionUtil.getTcpConnection());
                accountManager.sensitiveOperationOverInsecureConnection(true);
                accountManager.createAccount(Localpart.from(userName), password);
                result = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 注册账号
     *
     * @param userName           用户名
     * @param password           密码
     * @param onRegisterListener 注册回调
     */
    public void register(String userName, String password, OnRegisterListener onRegisterListener)
    {
        ConnectionUtil connection = new ConnectionUtil();
        connection.connect((isConnected, message) -> {
            if (isConnected)
            {
                try
                {
                    AccountManager accountManager = AccountManager.getInstance(ConnectionUtil.getTcpConnection());
                    accountManager.sensitiveOperationOverInsecureConnection(true);
                    accountManager.createAccount(Localpart.from(userName), password);
                    onRegisterListener.onRegister(true, "注册成功");
                    return;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            onRegisterListener.onRegister(false, "注册失败");
        });
    }

    /**
     * 登出
     */
    public void logout()
    {
        if(!isLogin())
        {
            return;
        }
        XMPPTCPConnection tcpConnection = ConnectionUtil.getTcpConnection();
        if(tcpConnection!=null)
        {
            tcpConnection.disconnect();
        }
        isLogin=false;
    }
}

