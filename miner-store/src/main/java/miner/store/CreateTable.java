package miner.store;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.Connection;


import com.mysql.jdbc.Driver;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class CreateTable {
    private static Configuration configuration = null;
    static{
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "127.0.0.1");
        configuration.set("hbase.rootdir","hdfs://master:8020/hbase");
        configuration.set("hbase.master", "hdfs://master:60000");
    }
    public static void main(String args[]) throws SQLException{

        new CreateTable().mysqlCheck(configuration);
    }

    public void mysqlCheck(Configuration conf) throws SQLException{
        boolean flag = true;
        String url = "jdbc:mysql://localhost/test?useUnicode=true&characterEncoding=utf8";
        Properties property = new Properties();
        property.put("user", "root");
        property.put("password", "root");
        Driver driver = new Driver();
        Connection con = driver.connect(url, property);
        Statement sta = con.createStatement();
        String sql = "select * from dataid";
        ResultSet rs = sta.executeQuery(sql);
        rs.last();
        String wid = rs.getString("wid");
        String pid = rs.getString("pid");
        String tid = rs.getString("tid");
        String dataid = rs.getString("dataid");
        String foreignkey = rs.getString("foreignkey");
        if(foreignkey.equals("none")){
            flag = false;
        }
        String tablename = wid+pid+tid+dataid;
        createTable(conf, tablename, flag);
    }


    public void createTable(Configuration conf,String tableName,boolean flag){
        HBaseAdmin admin;
        try {
            admin = new HBaseAdmin(conf);
            if(admin.tableExists(tableName)){
                System.err.print(tableName+"is exist and please check it");
            }else{
                HTableDescriptor tableDescriptor=new HTableDescriptor(TableName.valueOf(tableName));
                tableDescriptor.addFamily(new HColumnDescriptor("info"));
                tableDescriptor.addFamily(new HColumnDescriptor("description"));
                tableDescriptor.addFamily(new HColumnDescriptor("property"));
                tableDescriptor.addFamily(new HColumnDescriptor("link"));
                if(flag){
                    tableDescriptor.addFamily(new HColumnDescriptor("forgeignkey"));
                    tableDescriptor.addFamily(new HColumnDescriptor("foreignvalue"));
                }
                admin.createTable(tableDescriptor);
                System.out.println("end create table");
            }
        } catch (MasterNotRunningException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

