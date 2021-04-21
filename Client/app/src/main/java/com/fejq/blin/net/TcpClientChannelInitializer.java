package com.fejq.blin.net;

import android.net.http.SslError;

import com.fejq.blin.App;
import com.fejq.blin.model.Client;
import com.fejq.blin.model.message.MessageDecoder;
import com.fejq.blin.model.message.MessageEncoder;
import com.fejq.blin.net.ssl.SslContextFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslHandler;

public class TcpClientChannelInitializer extends ChannelInitializer
{
    public TcpClientChannelInitializer()
    {
    }

    @Override
    protected void initChannel(Channel channel) throws Exception
    {
        ChannelPipeline pipeline = channel.pipeline();
        if (Client.isSsl)
        {
            SSLContext sslContext = SSLContext.getInstance("SSLv3");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            KeyStore keyStore = KeyStore.getInstance("BKS");
            KeyStore trustStore = KeyStore.getInstance("BKS");
            String password="123456";
            keyStore.load(App.getContext().getAssets().open("client.bks"), password.toCharArray());
            trustStore.load(App.getContext().getAssets().open("client.bks"), password.toCharArray());
            keyManagerFactory.init(keyStore, password.toCharArray());
            trustManagerFactory.init(trustStore);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(true);
            sslEngine.setEnabledProtocols(sslEngine.getSupportedProtocols());
            sslEngine.setEnabledCipherSuites(sslEngine.getSupportedCipherSuites());
            sslEngine.setEnableSessionCreation(true);
            // pipeline.addLast("ssl", new SslHandler(sslEngine));
            pipeline.addLast(new SslHandler(sslEngine));
        }
        pipeline.addLast(new MessageEncoder());
        pipeline.addLast(new MessageDecoder());
        pipeline.addLast(new ClientChannelInboundHandler());
    }
}
