package miner.spider.utils;


import miner.spider.pojo.ProxyPojo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class SystemParas {
    // 日志
    public static MyLogger logger = new MyLogger(SystemParas.class);

    public static ReadConfigUtil readConfigUtil = new ReadConfigUtil(
            "crawl.properties", true);

    // http请求遇到error时，重复请求的次数
    public static int http_req_error_repeat_number = Integer
            .parseInt(readConfigUtil.getValue("http_req_error_repeat_number"));
    // 一次http请求要等待的时间
    public static int http_req_once_wait_time = Integer.parseInt(readConfigUtil
            .getValue("http_req_once_wait_time"));

    /**
     * 可以抓取url的最大重复次数
     */
    public static int crawl_page_repeat_number = Integer
            .parseInt(readConfigUtil.getValue("crawl_page_repeat_number"));

    /**
     * 代理参数设置
     */

    // proxy代理是否启用
    public static boolean proxy_open = Boolean.parseBoolean(readConfigUtil
            .getValue("proxy_open"));
    public static boolean proxy_self = Boolean.parseBoolean(readConfigUtil
            .getValue("proxy_self"));
    // 从文件中取得每对proxy ip的ip与port并加入proxyList集合
    public static String ip_proxy_file_path = readConfigUtil
            .getValue("ip_proxy_file_path");

    // 读取代理列表，并加入到proxy中
    public static List<ProxyPojo> proxyList = new LinkedList<ProxyPojo>();
    public static int proxy_fail_max_count = Integer.parseInt(readConfigUtil
            .getValue("proxy_fail_max_count"));

    // 设置链接超时时间,这个是链接时间
    public static int http_connection_timeout = Integer.parseInt(readConfigUtil
            .getValue("connection_timeout"));
    // 这个时读取超时时间
    public static int http_read_timeout = Integer.parseInt(readConfigUtil
            .getValue("read_timeout"));

    //rs username
    public static String username = readConfigUtil.getValue("username");
    //rs password
    public static String password = readConfigUtil.getValue("password");

    public static void main(String[] args) {
        System.out.println(password);
    }
}
