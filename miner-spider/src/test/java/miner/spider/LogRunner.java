package miner.spider;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by cutoutsy on 15/9/28.
 */
public class LogRunner implements Runnable{

    private ObjectInputStream ois;

    public LogRunner(Socket client){
        try{
            this.ois = new ObjectInputStream(client.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            while(true){
                Object obj = ois.readObject();
                System.out.println(obj.toString()+"-----------");
            }
        }catch (java.io.EOFException e){
            //读取的时候到达尾端抛出的异常,屏蔽掉
        }catch (InterruptedIOException e){
            Thread.currentThread().interrupt();
        }catch (IOException e){
        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }
    }

}
