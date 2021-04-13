import java.net.*;

public class Main
{
    static Socket socket;

    public static void main(String[] args)
    {
       Server server=new Server(8899);
       server.start();

    }



}
