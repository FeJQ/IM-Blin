package com.fejq.blin.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fejq.blin.R;
import com.fejq.blin.communication.UserClient;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.net.InetAddress;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";

    // Used to load the 'native-lib' library on application startup.
    static
    {
        System.loadLibrary("native-lib");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserClient user = new UserClient();
                String userName="test1";
                String password="123456";
                if(user.register(userName,password))
                {
                    Log.i(TAG,"注册成功");
                }
                else
                {
                    Log.i(TAG,"注册失败");
                    return;
                }
                if(user.login(userName,password))
                {
                    Log.i(TAG,"登录成功");
                }
                else
                {
                    Log.i(TAG,"登录失败");
                }

            }
        });
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native int add(int a, int b);
}