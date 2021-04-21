package server.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;


public class SslContextFactory
{
    private static final String PROTOCOL = "TLS";    // TODO: which protocols will be adopted?
    private static final SSLContext SERVER_CONTEXT;
    private static final SSLContext CLIENT_CONTEXT;

    static
    {
        SSLContext serverContext = null;
        SSLContext clientContext = null;


        String serverKeyStorePassword = "server123";
        String trustKeyStorePassword = "new08111001";
        String clientKeyStorePassword="client123";
        try
        {
            KeyStore ks = KeyStore.getInstance("BKS");
            ks.load(SslContextFactory.class.getClassLoader().getResourceAsStream("..\\data\\conf\\server.bks"), serverKeyStorePassword.toCharArray());

            // Set up key manager factory to use our key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, serverKeyStorePassword.toCharArray());

            // truststore
            KeyStore ts = KeyStore.getInstance("BKS");
            ts.load(SslContextFactory.class.getClassLoader().getResourceAsStream("..\\data\\conf\\cacer.bks"), trustKeyStorePassword.toCharArray());

            // set up trust manager factory to use our trust store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);

            // Initialize the SSLContext to work with our key managers.
            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        }
        catch (Exception e)
        {
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }

        try
        {
            // keystore
            KeyStore ks = KeyStore.getInstance("BKS");
            ks.load(SslContextFactory.class.getClassLoader().getResourceAsStream("cert\\clienKey.jks"), clientKeyStorePassword.toCharArray());

            // Set up key manager factory to use our key store
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, clientKeyStorePassword.toCharArray());

            // truststore
            KeyStore ts = KeyStore.getInstance("BKS");
            ts.load(SslContextFactory.class.getClassLoader().getResourceAsStream("cert\\clientTrust.jks"), trustKeyStorePassword.toCharArray());

            // set up trust manager factory to use our trust store
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ts);
            clientContext = SSLContext.getInstance(PROTOCOL);
            clientContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        }
        catch (Exception e)
        {
            throw new Error("Failed to initialize the client-side SSLContext", e);
        }

        SERVER_CONTEXT = serverContext;
        CLIENT_CONTEXT = clientContext;
    }

    public static SSLContext getServerContext()
    {
        return SERVER_CONTEXT;
    }

    public static SSLContext getClientContext()
    {
        return CLIENT_CONTEXT;
    }
}


