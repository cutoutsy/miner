package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

//Sql Server 连接测试
public class SqlServerConnectTest {

    public static void dbConnect(String db_connect_string, String db_userid, String db_password){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
            System.out.println("connected");

            //创建SQL命令对象
            Statement stmt = conn.createStatement();
            //创建表
            String query = "create table TABLE1(ID NCHAR(2), NAME NCHAR(10))";
            stmt.executeUpdate(query);
            System.out.println("create table success.");

            //输入数据
            String a1 = "INSERT INTO TABLE1 VALUES('1', 'jack')";
            String a2 = "INSERT INTO TABLE1 VALUES('2', 'tom')";
            String a3 = "INSERT INTO TABLE1 VALUES('3', 'jason')";

            stmt.executeUpdate(a1);
            stmt.executeUpdate(a2);
            stmt.executeUpdate(a3);

            System.out.println("insert data success.");

            //读取数据
            ResultSet rs = stmt.executeQuery("SELECT * FROM TABLE1");
            while(rs.next()){
                System.out.println(rs.getString("ID")+"\t"+rs.getString("NAME"));
            }
            System.out.println("read data over.");

            //close connection
            stmt.close();   //关闭命令对象连接
            conn.close();   //关闭数据库连接


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void serverConnect(String db_connect_string, String db_userid, String db_password){

        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(db_connect_string, db_userid, db_password);
            System.out.println("connected");
            //close connection
            conn.close();   //关闭数据库连接


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //得到数据库连接实例
    public static Connection getSqlServerConnection(){
        Connection conn = null;
        String url = "jdbc:jtds:sqlserver://127.0.0.1:1081/";
        String userid = "sa";
        String password = "xiaochuan";
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(url, userid, password);
            System.out.println("connected");
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    //列出Sql server服务器下所有的数据库
    public static void listSqlServerDatabases(){
        try {
            Connection conn = getSqlServerConnection();
            //创建SQL命令对象
            Statement stmt = conn.createStatement();
            String listDatabases = "SELECT Name FROM Master..SysDatabases ORDER BY Name";
            ResultSet rs = stmt.executeQuery(listDatabases);
            while (rs.next()) {
                System.out.println(rs.getString("Name"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public static void main(String[] args)
    {
//        String url = "jdbc:sqlserver://127.0.0.1:1081;";
//        String userid = "sa";
//        String password = "xiaochuan";
//        System.out.println("start connect...");
//        dbConnect(url, userid, password);
        //serverConnect(url, userid, password);
//        listSqlServerDatabases();
//        String relativelyPath=System.getProperty("user.dir");
//        System.out.println(relativelyPath);
        getSqlServerConnection();

    }
}
