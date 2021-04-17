import com.alibaba.fastjson.JSON;
import server.Server;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Main
{
    static Socket socket;



    public static void main(String[] args)
    {
        Server server = new Server(8899);
        server.start();

    }


}
