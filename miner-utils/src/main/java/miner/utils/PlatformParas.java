package miner.utils;

/**
 * 平台参数配置
 */
public class PlatformParas {

    public static ReadConfigUtil readConfigUtil= new ReadConfigUtil("platform.properties", true);

    public static String redis_host = readConfigUtil.getValue("redis_host");
}
