package miner.utils;

import junit.framework.TestCase;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * redis测试类
 * User: cutoutsy
 * Date: 4/21/2016
 */
public class RedisUtilTest extends TestCase {

    //测试redis选择不同的数据库
    @Test
    public void testRemoteRedisConnect(){
        String redis_host = "cutoutsy.cn";
        int redis_port = 16379;
        String redis_auth = "xidian123";
        int redis_database0 = 0;
        int redis_database1 = 1;

        RedisUtil ru0 = new RedisUtil(redis_host, redis_port, redis_auth, redis_database0);
        Jedis redis0 = ru0.getJedisInstance();
        redis0.set("foo", "bar");
        assertEquals(redis0.get("foo"), "bar");

        RedisUtil ru1 = new RedisUtil(redis_host, redis_port, redis_auth, redis_database1);
        Jedis redis1 = ru1.getJedisInstance();
        redis1.set("foo", "bar");
        assertEquals(redis1.get("foo"), "bar");

        redis0.del("foo");
        redis1.del("foo");
    }
}
