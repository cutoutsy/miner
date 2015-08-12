package miner.spider.utils;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Mysql utils
 *
 * Created by cutoutsy on 8/6/15.
 */
public class MysqlUtil {

    // 返回mysql连接
    public static Connection getConnection() {
        String url = "jdbc:mysql://"+StaticValue.mysql_host+":"+StaticValue.mysql_port+"/"+StaticValue.mysql_database+"?useUnicode=true&characterEncoding=utf8";
        Properties info = new Properties();
        info.put("user", StaticValue.mysql_user);
        info.put("password", StaticValue.mysql_password);
        Driver driver;
        Connection con = null;
        try {
            driver = new Driver();
            con = driver.connect(url, info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    public static List getProject(String projectName){
        List reList = new ArrayList();
        try {
//            int wid = Integer.valueOf(projectName.split("-")[0]);
//            int pid = Integer.valueOf(projectName.split("-")[1]);
            String wid = projectName.split("-")[0];
            String pid = projectName.split("-")[1];

            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt
                    .executeQuery("select * from project where wid="
                            + wid+" AND pid=" + pid);
            rs.next();// 指向有效的一行
//            String re = rs.getString(5);
            for(int i = 2; i < 9; i++){
                String re = rs.getString(i);
                //System.out.println(re);
                reList.add(re);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return reList;
    }

    public static List getTask(String taskName){
        List reList = new ArrayList();
        try {
//            int wid = Integer.valueOf(projectName.split("-")[0]);
//            int pid = Integer.valueOf(projectName.split("-")[1]);
            String wid = taskName.split("-")[0];
            String pid = taskName.split("-")[1];
            String tid = taskName.split("-")[2];

            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt
                    .executeQuery("select * from task where wid="
                            + wid+" AND pid=" + pid+" AND tid=" + tid);
            rs.next();// 指向有效的一行
//            String re = rs.getString(5);
            for(int i = 2; i < 10; i++){
                String re = rs.getString(i);
//                System.out.println(re);
                reList.add(re);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return reList;
    }


    public static List getTaskByProject(String wid, String pid){
        List reList = new ArrayList();
        try {
            String twid = wid;
            String tpid = pid;

            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt
                    .executeQuery("select * from task where wid="
                            + wid+" AND pid=" + pid);
            while(rs.next()){
                String tid = rs.getString(4);
                reList.add(tid);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return reList;
    }

    public static void main(String[] args){
//        Connection conn = getConnection();
//        System.out.println(conn);
//        getProject("1-1");
        getTask("1-1-1");
    }

}
