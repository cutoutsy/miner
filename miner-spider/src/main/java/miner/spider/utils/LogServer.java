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
        ServerSocket socket = new ServerSocket(Integer.valueOf(PlatformParas.logserver_port));
        System.out.println("log server start...");
        while(true){
            Socket client = socket.accept();
            Thread t = new Thread(new LogRunner(client));
            t.start();
        }
    }
}
