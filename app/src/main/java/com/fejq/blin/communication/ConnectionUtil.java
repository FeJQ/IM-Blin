package com.fejq.blin.communication;

import android.util.Log;
import android.view.inspector.StaticInspectionCompanionProvider;

import com.fejq.blin.config.Const;
import com.fejq.utils.ToastUtil;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class ConnectionUtil
{
    private final String TAG = "ConnectionUtil";
    private static XMPPTCPConnection xmpptcpConnection = null;

    public static XMPPTCPConnection getTcpConnection()
    {
        return xmpptcpConnection;
    }

    /**
     * 开始建立连接
     * @return 是否连接成功
     */
    private boolean toConnect()
    {
        try
        {
            disconnect();
            InetAddress address = InetAddress.getByName(Const.HOST_ADDRESS);
            HostnameVerifier verifier = new HostnameVerifier()
            {
                @Override
                public boolean verify(String hostname, SSLSession session)
                {
                    return false;
                }
            };
            DomainBareJid serviceName = JidCreate.domainBareFrom(Const.SERVER_NAME);
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setHost(Const.HOST_NAME)
                    .setPort(Const.PORT)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setXmppDomain(serviceName)
                    .setHostnameVerifier(verifier)
                    .setHostAddress(address)
                    .setConnectTimeout(Const.TIMEOUT)
                    .build();
            xmpptcpConnection = new XMPPTCPConnection(config);
            //开启重联机制
//                    ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(xmpptcpConnection);
//                    reconnectionManager.setFixedDelay(5);
//                    reconnectionManager.enableAutomaticReconnection();
            //连接
            xmpptcpConnection.connect();
            return true;
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isConnectDone=false;
    private boolean connectResult=false;

    /**
     * 建立连接
     * @return 是否连接成功
     */
    public boolean connect() throws InterruptedException
    {
        if(xmpptcpConnection!=null && xmpptcpConnection.isConnected())
        {
            return true;
        }
        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    isConnectDone=false;
                    connectResult=false;
                    connectResult=toConnect();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                isConnectDone=true;
            }
        });
        thread.start();
        thread.join();
        return connectResult;
    }

    /**
     * 建立连接
     *
     * @param onConnectListener 回调函数
     */
    public void connect(OnConnectListener onConnectListener)
    {
        if(xmpptcpConnection!=null && xmpptcpConnection.isConnected())
        {
            return;
        }
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (toConnect())
                    {
                        onConnectListener.onConnect(true, "连接成功");
                    }
                    else
                    {
                        onConnectListener.onConnect(false, "连接失败");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 断开连接
     */
    public void disconnect()
    {
        if (xmpptcpConnection != null)
        {
            xmpptcpConnection.disconnect();
            xmpptcpConnection = null;
        }
    }

    public interface OnConnectListener
    {
        void onConnect(boolean isConnected, String message) throws InterruptedException, IOException, SmackException, XMPPException;
    }
}
