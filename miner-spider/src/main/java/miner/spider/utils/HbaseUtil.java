package miner.spider.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class HbaseUtil {

    /**
     * connect hbase by config or code
     */

    private static HTable table = null;

    private static Configuration conf = null;
    //静态代码块
    static {
        conf = HBaseConfiguration.create();
    }

    /**
     *
     * @TODO: 传入表名，判断是否存在
     * @return:boolean
     */
    public static boolean tableIsExist(String tableName){
        boolean flag = true;
        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
            if (hBaseAdmin.tableExists(tableName)) {
            }else{
                flag = false;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    //传入表名，创建hbase表,列簇数组
    public static void createTable(String tableName,String[] families) {
        try {
            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
            if (hBaseAdmin.tableExists(tableName)) {
//              如果存在要创建的表，就不做任何处理
//				hBaseAdmin.disableTable(tableName);
//				hBaseAdmin.deleteTable(tableName);
            }else{
                HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
                for (int i = 0; i < families.length; i++) {
                    tableDescriptor.addFamily(new HColumnDescriptor(families[i]));
                }
                hBaseAdmin.createTable(tableDescriptor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("创建"+tableName+"成功");

    }

    //将今天与昨天品牌id相同的直接存入Hbase
    public static boolean SaveSameBrandIdToHbase(String sameid){
        boolean flag = true;
        HashMap<String,String> brandid_price;
        String yesterdayDate = DateUtil.GetYesterdayDate();
        String date = DateUtil.GetTodayDate();

        Jedis redisdb1 = new Jedis("192.168.1.211",6379,10000);
        redisdb1.auth("xidian123");
        Jedis redisdb2 = new Jedis("192.168.1.211",6379,10000);
        redisdb2.auth("xidian123");
        redisdb2.select(2);

        brandid_price = (HashMap<String, String>) redisdb2.hgetAll(sameid+"-"+yesterdayDate);
        Iterator iter = brandid_price.entrySet().iterator();
        try{
            while(iter.hasNext()){
                String idAndPrice = iter.next().toString();
                String id = idAndPrice.split("=")[0];
                //存入消息来源
                redisdb1.sadd("merchandiseId-"+date, id);
//			String price = redisdb2.hget(sameid+"-"+yesterdayDate, id);
                String price = idAndPrice.split("=")[1];
                System.out.println("相同id写入数据:" + id + ":" + price);

                redisdb2.hset(id.split("-")[0]+"-"+date,id,price);

                table = new HTable(conf, "vip-" + date);
                Put put = new Put(Bytes.toBytes(id));
                put.add(Bytes.toBytes("price"), Bytes.toBytes("price"),
                        Bytes.toBytes(price));
                table.put(put);
                System.out.println(id+"存储成功");
            }
        }catch(Exception e){
            System.out.println("相同id存入Hbase里时发生错误");
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * @TODO:将数据写入Hbase里
     * @return void
     * @throws IOException
     */
    public static void saveDataToHbase(String tableName,String rowKey,String families,String column,String data) throws IOException {
        table = new HTable(conf, tableName);
        Put put = new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(families), Bytes.toBytes(column),
                Bytes.toBytes(data));
        table.put(put);
        System.out.println(rowKey+"存储成功");
    }

    /**
     *
     * @TODO:增加一个列簇
     * @return:void
     */
    public static void addOneFamilies(String tableName, String family){
        try {
            //在添加列簇前要disable表，添加完成后在enable表
            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
            hBaseAdmin.disableTable(Bytes.toBytes(tableName));
            hBaseAdmin.addColumn(Bytes.toBytes(tableName), new HColumnDescriptor(family));
            hBaseAdmin.enableTable(Bytes.toBytes(tableName));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @TODO:根据传入的表名，行键，列簇名，返回该列簇下的所有键
     * @return:List<String>
     */
    public static List<String> getDate(String tableName,String row,String column) {
        List<String> reList = new ArrayList<String>();
        try{
            HTable table = new HTable(conf, tableName);
            Get get = new Get(Bytes.toBytes(row));
            //若不存在该列簇，会报异常，返回null
            get.addFamily(Bytes.toBytes(column));
            Result result = table.get(get);
//        result.containsColumn(family, qualifier)
            // 输出结果
            for (KeyValue rowKV : result.raw()) {
//            System.out.print("Row Name: " + new String(rowKV.getRow()) + " ");
//            System.out.print("Timestamp: " + rowKV.getTimestamp() + " ");
//            System.out.print("column Family: " + new String(rowKV.getFamily()) + " ");
//            System.out.print("Row Name:  " + new String(rowKV.getQualifier()) + " ");
//            System.out.println("Value: " + new String(rowKV.getValue()) + " ");
                reList.add(new String(rowKV.getQualifier()));
            }
        }catch(Exception e){
            reList = null;
            e.printStackTrace();
        }
        return reList;
    }

    public static void main(String[] args){
//        String[] fs = new String[2];
//        fs[0] = "test1";
//        fs[1] = "test2";
////        HbaseUtil hu = new HbaseUtil();
//        HbaseUtil.createTable("kkk",fs);
        System.out.println(tableIsExist("kkk"));
    }

}
