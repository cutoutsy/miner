package miner.store;



import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 15-8-12.
 */
public class ImportData {

    private static Configuration configuration = null;
    static{
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", "127.0.0.1");
        configuration.set("hbase.rootdir","hdfs://master:8020/hbase");
        configuration.set("hbase.master", "hdfs://master:60000");
    }

    public static  void main(String[] args){
       String data = "{\n" +
                "\"workstation_id\":\"1\",\n" +
                "\"project_id\":\"1\",\n" +
                "\"task_id\":\"1\",\n" +
                "\"data_id\":\"1\",\n" +
                "\"description\":\"city\",\n" +
                "\"property\":{\n" +
                "\t\"cityname\":\"Xian\",\n" +
                "\t\"url\":[\"12\",\"34\"]\n" +
                "},\n" +
                "\"row_key\":\"12233\",\n" +
                "\"foreign_key\":\"1233344\",\n" +
                "\"foreign_value\":\"kp\",\n" +
                "\"link\":\"alone\"\n" +
                "}";
        new ImportData().importData(data);
    }

    public static void importData(String data){

        try {
            String rowKey = null;
            JSONObject jsonObject = new JSONObject(data);
            //获取info中的信息
            String workStationID = jsonObject.getString("workstation_id");
            String projectID = jsonObject.getString("project_id");
            String taskID = jsonObject.getString("task_id");
            String dataID = jsonObject.getString("data_id");
            //构造出表名，以各个id命名
            String tableName = workStationID+projectID+taskID+dataID;

            String foreignKey = jsonObject.getString("foreign_key");
            String foreignValue = jsonObject.getString("foreign_value");
            String link = jsonObject.getString("link");
            String rowKeyBak = jsonObject.getString("row_key");

            //构造出行键，当rowkey不为none的时候，使用时间戳与行键，否则使用UUID
            if(!rowKeyBak.equals("none")) {
                rowKey = System.currentTimeMillis() + "$" + rowKeyBak;
            }else {
               rowKey = UUID.randomUUID().toString();
            }

            //对property进行处理
            String property = jsonObject.getString("property");
            JSONObject propertyjson = new JSONObject(property);
            Iterator keys = propertyjson.keys();
            while (keys.hasNext()){
                String key = keys.next().toString();
                String propertyValue = propertyjson.getString(key);
                Pattern pattern = Pattern.compile("\\[");
                Matcher matcher = pattern.matcher(propertyValue);
                if(matcher.find()){
                JSONArray jsonArray = new JSONArray(propertyValue);
                    String propertyData = null;
                for (int i = 0; i < jsonArray.length();i++) {
                    propertyData = propertyData + "[!==!]"+jsonArray.getString(i);
                }
                    addData(configuration, tableName, rowKey, "property", key, propertyData);
                }else{
                    addData(configuration, tableName, rowKey, "property", key, propertyValue);
                }
            }

            addData(configuration,tableName,rowKey,"info","workStationID",workStationID);
            addData(configuration,tableName,rowKey,"info","projectID",projectID);
            addData(configuration,tableName,rowKey,"info","taskID",taskID);
            addData(configuration,tableName,rowKey,"info","dataID",dataID);
            addData(configuration,tableName,rowKey,"link","link",link);
            if(!foreignKey.equals("none")){
                addData(configuration,tableName,rowKey,"foreign", "foreignKey", foreignKey);
                addData(configuration,tableName,rowKey,"foreign","foreignValue",foreignValue);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void addData(Configuration configuration,String tableName,String Rowkey,String family,String column,
                        String value){
        HBaseAdmin admin;
        try {
            admin = new HBaseAdmin(configuration);
            if(admin.tableExists(tableName)){
                @SuppressWarnings("resource")
                HTable table=new HTable(configuration, tableName);
                Put put=new Put(Bytes.toBytes(Rowkey));
                put.add(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));
                table.put(put);
                //System.out.println("add success!");
            }else{
                System.out.println(tableName+"Table does not exist!");
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
