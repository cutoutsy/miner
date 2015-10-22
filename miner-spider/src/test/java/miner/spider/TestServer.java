package miner.spider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by cutoutsy on 15/9/28.
 */
public class TestServer {

    public static void main(String[] args) throws IOException{
        ServerSocket socket = new ServerSocket(5000);

        while(true){
            Socket client = socket.accept();
            Thread t = new Thread(new LogRunner(client));
            t.start();
        }
    }
}
