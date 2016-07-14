package miner.store;

import miner.spider.utils.MysqlUtil;
import miner.utils.MySysLogger;
import miner.utils.PlatformParas;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CreateTable {

    private static MySysLogger logger = new MySysLogger(CreateTable.class);

    private static Configuration configuration = null;
    static{
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", PlatformParas.hbase_zookeeper_host);
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        configuration.set("hbase.rootdir","hdfs://master:8020/hbase");
        configuration.set("hbase.master", "hdfs://master:60000");
    }
    public static void main(String args[]) throws SQLException{

        //new CreateTable().mysqlCheck("1","1");
        new CreateTable().createTable(configuration, "cutoutsy", true);

    }

    public static int rowCount(String tableName) {
        int rowCount = 0;
        try {
            HTable table = new HTable(configuration, tableName);
            Scan scan = new Scan();
            scan.setFilter(new FirstKeyOnlyFilter());
            ResultScanner resultScanner = table.getScanner(scan);
            for (Result result : resultScanner) {
                rowCount += result.size();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rowCount;
    }

    //根据表名获取最近一段时间的数据
    public static List<String> getDataSetByTableName(String tableName){
        List<String> reList = new ArrayList<String>();
        try {
            HTable table = new HTable(configuration, tableName);
            Scan s = new Scan();
            long currentTimeStamp = System.currentTimeMillis();
            long startTimeStamp = currentTimeStamp - 10*60;     //抽取最近10分钟的数据
            s.setTimeRange(startTimeStamp, currentTimeStamp);
            ResultScanner rs = table.getScanner(s);
            for (Result r : rs) {
                for (Cell cell : r.rawCells()) {
                    StringBuffer tempSb = new StringBuffer();
                    //行健$$时间$$列$$值
                    tempSb.append(Bytes.toString(r.getRow()) + "$$" + cell.getTimestamp() + "$$" + Bytes.toString(CellUtil.cloneQualifier(cell))+"$$" + Bytes.toString(CellUtil.cloneValue(cell)));
                    reList.add(tempSb.toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return reList;
    }

    public static void mysqlCheck(String tableWid,String tablePid) throws SQLException{
        boolean flag = true;
        Connection con = MysqlUtil.getConnection();
        Statement sta = con.createStatement();
        String sql = "select * from data order by id desc";
        ResultSet rs = sta.executeQuery(sql);
        while(rs.next()){
        String wid = rs.getString("wid");
        String pid = rs.getString("pid");
            if(wid.equals(tableWid)&&pid.equals(tablePid)){
                String tid = rs.getString("tid");
                String dataid = rs.getString("dataid");
                String processWay = rs.getString("processWay");
                String foreignkey = rs.getString("foreignkey");
                 if(foreignkey.equals("none")){
                     flag = false;
                    }
                // 在hbase创建data处理方式为s的相应的表
                if(processWay.equals("s") || processWay.equals("S")) {
                    String tablename = wid + pid + tid + dataid;
                    createTable(configuration, tablename, flag);
                }
            }else{
                break;
            }
    }
    }

    public static void createTable(Configuration conf,String tableName,boolean flag){
        HBaseAdmin admin;
        try {
            admin = new HBaseAdmin(conf);
            if(admin.tableExists(tableName)){
                System.err.println(tableName+"is exist and please check it");
            }else{
                HTableDescriptor tableDescriptor=new HTableDescriptor(TableName.valueOf(tableName));
                tableDescriptor.addFamily(new HColumnDescriptor("info"));
                tableDescriptor.addFamily(new HColumnDescriptor("property"));
                tableDescriptor.addFamily(new HColumnDescriptor("link"));
                if(flag){
                    tableDescriptor.addFamily(new HColumnDescriptor("foreign"));
                }
                admin.createTable(tableDescriptor);
                System.out.println("end create table");
            }
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
            logger.error("Master Not Running "+ MySysLogger.formatException(e));
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
            logger.error("Zookeeper Connect Exception " + MySysLogger.formatException(e));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO exception"+ MySysLogger.formatException(e));
        }
    }

    public static void createTable(String tableName,boolean flag){
        HBaseAdmin admin;
        try {
            admin = new HBaseAdmin(configuration);
            if(admin.tableExists(tableName)){
                System.err.println(tableName+"is exist and please check it");
            }else{
                HTableDescriptor tableDescriptor=new HTableDescriptor(TableName.valueOf(tableName));
                tableDescriptor.addFamily(new HColumnDescriptor("info"));
                tableDescriptor.addFamily(new HColumnDescriptor("property"));
                tableDescriptor.addFamily(new HColumnDescriptor("link"));
                if(flag){
                    tableDescriptor.addFamily(new HColumnDescriptor("foreign"));
                }
                admin.createTable(tableDescriptor);
                System.out.println("end create table");
            }
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
            logger.error("Master Not Running "+ MySysLogger.formatException(e));
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
            logger.error("Zookeeper Connect Exception " + MySysLogger.formatException(e));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("IO exception"+ MySysLogger.formatException(e));
        }
    }

    public static void initHbaseTable() throws SQLException{
        boolean flag = true;
        Connection con = MysqlUtil.getConnection();
        Statement sta = con.createStatement();
        String sql = "select * from data";
        ResultSet rs = sta.executeQuery(sql);
        while(rs.next()){
            String wid = rs.getString("wid");
            String pid = rs.getString("pid");
            String tid = rs.getString("tid");
            String dataid = rs.getString("dataid");
            String foreignkey = rs.getString("foreignkey");
            if(foreignkey.equals("none")){
                flag = false;
            }
            String tablename = wid+pid+tid+dataid;
            createTable(configuration, tablename, flag);
        }
    }

}

