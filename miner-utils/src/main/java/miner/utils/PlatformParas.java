package miner.utils;

/**
 * 平台参数配置
 */
public class PlatformParas {

    public static ReadConfigUtil readConfigUtil= new ReadConfigUtil("/opt/build/platform.properties", true);

    //redis配置信息
    public static String redis_host = readConfigUtil.getValue("redis_host");
    public static String redis_port = readConfigUtil.getValue("redis_port");
    public static String redis_auth = readConfigUtil.getValue("redis_auth");
    public static String redis_database = readConfigUtil.getValue("redis_database");

    //mysql信息配置
    public static String mysql_host = readConfigUtil.getValue("mysql_host");
    public static String mysql_port = readConfigUtil.getValue("mysql_port");
    public static String mysql_user = readConfigUtil.getValue("mysql_user");
    public static String mysql_password = readConfigUtil.getValue("mysql_password");
    public static String mysql_database = readConfigUtil.getValue("mysql_database");

    //hbase zookeeper主机
    public static String hbase_zookeeper_host = readConfigUtil.getValue("hbase_zookeeper_host");

    //log dir
    public static String log_path_dir = readConfigUtil.getValue("log_path_dir");

    //parallelism config
    public static int work_num = Integer.parseInt(readConfigUtil.getValue("work_num"));
    public static int begin_spout_num = Integer.parseInt(readConfigUtil.getValue("begin_spout_num"));
    public static int loop_spout_num = Integer.parseInt(readConfigUtil.getValue("loop_spout_num"));
    public static int generateurl_bolt_num = Integer.parseInt(readConfigUtil.getValue("generateurl_bolt_num"));
    public static int proxy_bolt_num = Integer.parseInt(readConfigUtil.getValue("proxy_bolt_num"));
    public static int fetch_bolt_num = Integer.parseInt(readConfigUtil.getValue("fetch_bolt_num"));
    public static int parse_bolt_num = Integer.parseInt(readConfigUtil.getValue("parse_bolt_num"));
    public static int store_bolt_num = Integer.parseInt(readConfigUtil.getValue("store_bolt_num"));

    //message timeout
    public static int message_timeout_secs = Integer.parseInt(readConfigUtil.getValue("message_timeout_secs"));

    //反射jar包所在目录
    public static String reflect_dir = readConfigUtil.getValue("reflect_dir");

    //日志服务器端口
    public static String logserver_port = readConfigUtil.getValue("logserver_port");

    //代理的订单号
    public static String orderid =readConfigUtil.getValue("orderid");

    public static void main(String[] args) {
        System.out.println(redis_host);
        System.out.println(redis_auth);
        System.out.println(begin_spout_num);
    }

}
