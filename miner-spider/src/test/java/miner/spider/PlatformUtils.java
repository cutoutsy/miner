package miner.spider;

import miner.spider.utils.MysqlUtil;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * some PlatformUtils for the platform
 */
public class PlatformUtils {
//    private static MyLogger logger = new MyLogger(PlatformUtils.class);

    private static RedisUtil ru;
    public static Jedis redis;

    static {
        ru = new RedisUtil();
        redis = ru.getJedisInstance();
    }

    //monitor the project, return a project can execute
    public static void monitorProject(){
        String foo = redis.get("foo");
        System.out.println(foo);
    }



}
