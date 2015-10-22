package miner.utils;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class StaticValue {
    /**
     * redis host定义和密码认证
     */
//    public static String redis_host = "127.0.0.1";
    public static String redis_host = "192.168.1.211";
    public static String redis_auth = "xidian123";

    /**
     * mysql:host, username, password, port, database
     */
//    public static String mysql_host = "127.0.0.1";
//    public static String mysql_user = "root";
//    public static String mysql_port = "3307";
//    public static String mysql_password = "simple";
//    public static String mysql_database = "platform_config";

    public static String mysql_host = "192.168.1.212";
    public static String mysql_user = "root";
    public static String mysql_port = "3306";
    public static String mysql_password = "LNkiller&212";
    public static String mysql_database = "platform_config";

    /**
     * hbase:host
     */
    public static String hbase_zookeeper_host = "192.168.1.230";
//    public static String hbase_zookeeper_host = "127.0.0.1";

    // 集群日志存放目录
    public static String logPathDir = "/usr/local/storm/logs/";
//    public static String logPathDir = "/Users/cutoutsy/";

    //设置请求和传输超时时间,单位ms
    public static int http_connection_timeout = 5000; //请求超时时间
    public static int http_read_timeout = 5000;	//传输超时时间

    //设置代理是否开启
    public static boolean proxy_open = false;
    //是否使用本机ip
    public static boolean proxy_self = false;

    public static String default_encoding = "utf-8";
    public static String gbk_encoding = "gbk";
    public static final String gb2312_encoding = "gb2312";

    public static String default_refer = "http://www.baidu.com/";

    public static String baidu_index = "http://www.baidu.com";

    /**
     * 符号定义
     */
    public static String separator_tab = "\t";
    public static String separator_next_line = "\n";
    public static String separator_space = " ";
    public static String separator_file_path = "/";

    public static String separator_left_bracket = "(";
    public static String separator_right_bracket = ")";

    /**
     * 专为解决网页编码提取而添加
     */
    // 单点定义
    public static final char point = '.';

    //网页最短定义
    public static int url_data_min_byte_length = 500;

    /**
     * 以下是对网页的charset下的charset相应定义
     */
    // 默认编码方式
    public static final String SYSTEM_ENCODING = "utf-8";
    // 默认gbk中文的处理编码
    public static final String GBK_ENCODING = "gbk";
    // 默认gb2312中文的处理编码
    public static final String GB2312_ENCODING = "gb2312";
    // 台湾big5编码
    public static final String BIG5_ENCODING = "big5";
    // 日本Shift_JIS
    public static final String Japan_Shift_ENCODING = "shift_jis";
    public static final String Japan_Euc_ENCODING = "euc-jp";
    // 西里尔文window
    public static final String Xili_Window_ENCODING = "windows-1251";
    /**
     * 以下是对网页的charset部分的lang来定义
     */
    public static final String Japan_Lang = "ja";
    public static final String Japan_Lang_First = Japan_Shift_ENCODING;
}
