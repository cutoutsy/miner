package miner.spider.utils;

import miner.utils.PlatformParas;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 日志服务器
 */
public class LogServer {

    public static void main(String[] args) throws IOException{
        startLogServer(Integer.valueOf(PlatformParas.logserver_port), PlatformParas.log_path_dir);
    }

    public static void startLogServer(int logserverPort, String logPath){
        try {
            ServerSocket socket = new ServerSocket(logserverPort);
            System.out.println("log server started at port:"+logserverPort);
            while (true){
                Socket client = socket.accept();
                Thread t = new Thread(new LogRunner(client, logPath));
                t.start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
