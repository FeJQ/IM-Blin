package server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;
import model.message.encodec.MessageDecoder;
import model.message.encodec.MessageEncoder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.Security;

class SocketChannelInitializer extends ChannelInitializer<SocketChannel>
{

    public SocketChannelInitializer()
    {
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        System.out.println("客户端连接");

        ChannelPipeline pipeline = socketChannel.pipeline();
        if (Server.isSsl)
        {
            String path = this.getClass().getResource("/").getPath();
            SSLContext sslContext = SSLContext.getInstance("SSLv3");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("BKS");
            KeyStore trustStore = KeyStore.getInstance("BKS");
            String password="123456";
            keyStore.load(new FileInputStream(new File(path+"server.bks")), password.toCharArray());//shenbo
            trustStore.load(new FileInputStream(new File(path+"server.bks")), password.toCharArray());
            keyManagerFactory.init(keyStore, password.toCharArray());
            trustManagerFactory.init(trustStore);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);




            SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(false);
            // 双向认证时需要设置
            sslEngine.setNeedClientAuth(true);

            sslEngine.setEnabledProtocols(sslEngine.getSupportedProtocols());
            sslEngine.setEnabledCipherSuites(sslEngine.getSupportedCipherSuites());
            sslEngine.setEnableSessionCreation(true);
            pipeline.addLast(new SslHandler(sslEngine));
        }

        // 添加编码器
        pipeline.addLast("encoder", new MessageEncoder());
        // 添加解码器
        pipeline.addLast("decoder", new MessageDecoder());
        // 添加入站 handler
        pipeline.addLast("in_handler", new InServerChannelHandler());
        // 添加出站 handler
        pipeline.addLast("out_handle", new OutServerChannelHandler());


    }
}
