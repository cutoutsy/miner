package utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件读取类
 */
public class IOUtil {

    public static Set<String> readFileToSet(String filePath, String fileEncoding) {
        if (fileEncoding == null) {
            fileEncoding = "utf-8";
        }
        File file = new File(filePath);
        BufferedReader br = null;
        String line = null;
        Set<String> lineSet = new HashSet<String>();
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    file), fileEncoding));
            while ((line = br.readLine()) != null) {
                lineSet.add(line);
            }
            return lineSet;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void writeFile(String path, String value, boolean isAppend,
                                 String encoding) {
        File f = new File(path);
        // 排除其父目录不存在的情况
        FileOutputStream fos = null;
        try {
            if (isAppend) {
                fos = new FileOutputStream(f, isAppend);
            } else {
                fos = new FileOutputStream(f);
            }
            fos.write(value.getBytes(encoding));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean createFile(String destFileName, boolean isDelete){
        File file = new File(destFileName);
        if(file.exists()){
            if(isDelete) {
                file.delete();
            }else{
                return false;
            }
        }
        if(destFileName.endsWith(File.separator)){
            return false;
        }
        //判断目标文件所在目录是否存在
        if(!file.getParentFile().exists()){
            if(!file.getParentFile().mkdirs()){
                return false;
            }
        }
        //创建目标文件
        try{
            if(file.createNewFile()){
                return true;
            }else{
                return false;
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public static List getFilesNameStr(String desc_dir){
        List reList = new ArrayList();
        File dir = new File(desc_dir);
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++){
            //System.out.println(files[i].toString());
//            reList.add(files[i].toString());
            long time = files[i].lastModified();
            String formatTime = IOUtil.TimeStamp2Date(time, "yyyy-MM-dd HH:mm");
            reList.add(files[i].toString()+"$"+formatTime);
        }
        return reList;
    }

    public static String TimeStamp2Date(long timestampString, String formats){
        Long timestamp = timestampString;
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }

    public static void main(String[] args){
        List<String> li = getFilesNameStr("C:\\Program Files\\Microsoft SQL Server\\MSSQL11.MSSQLSERVER\\MSSQL\\Backup");
        System.out.println(li.size());
        for (int i = 0; i < li.size(); i++){
            System.out.println(li.get(i) + "==");
        }
    }

    // 判断文件是否存在
    public static boolean existFile(String filepath){
        return new File(filepath).exists();
    }
}
