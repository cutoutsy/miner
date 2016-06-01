package miner.spider.utils;

import miner.utils.PlatformParas;
import miner.utils.StaticValue;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;

/**
 * 日志运行线程
 */
public class LogRunner implements Runnable{

    private ObjectInputStream ois;
    private String logPath;

    public LogRunner(Socket client, String logPath){
        this.logPath = logPath;
        try{
            this.ois = new ObjectInputStream(client.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run(){
        try{
            while(true){

                StringBuilder logStringBuilder = new StringBuilder("");

                //将集群日志写入文件
                String logTime = DateUtil.getStringByDate(new Date());
                logStringBuilder.append(logTime);
                Object obj = ois.readObject();
                logStringBuilder.append(" " + obj.toString()+"\n");

                String todayDate = DateUtil.GetTodayDate();
//                String logPath = PlatformParas.log_path_dir+"miner-"+todayDate+".log";
                String logPath = this.logPath+"miner-"+todayDate+".log";
                System.out.println("logRenamePath:"+logPath);

                if(FileOperatorUtil.existFile(logPath)){
                    if(FileOperatorUtil.getFileSize(logPath) >= 100){
//                        String logRenamePath = PlatformParas.log_path_dir+"miner-"+ logTime + ".log";
                        String logRenamePath = this.logPath+"miner-"+ logTime + ".log";

                        if(FileOperatorUtil.renameFile(logPath, logRenamePath)) {
                            FileOperatorUtil.createFile(logPath);
                            IOUtil.writeFile(logPath, logStringBuilder.toString(), true, StaticValue.default_encoding);
                        }else{
                            System.out.println("rename file failed!");
                        }
                    }else {
                        IOUtil.writeFile(logPath, logStringBuilder.toString(), true, StaticValue.default_encoding);
                    }
                }else{
                    FileOperatorUtil.createFile(logPath);
                    IOUtil.writeFile(logPath, logStringBuilder.toString(), true, StaticValue.default_encoding);
                }

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
