package miner.utils;

import junit.framework.TestCase;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * class function.
 * User: cutoutsy
 * Date: 4/21/2016
 */
public class RedisUtilTest extends TestCase {

    @Test
    public void testGetJedisInstance(){
        RedisUtil ru = new RedisUtil();
        Jedis re = ru.getJedisInstance();
        assertNotNull(re);
    }

    @Test
    public void testRemoteRedisConnect(){
        Jedis redis = new Jedis("1.85.180.168", 16379);
        redis.auth("xidian123");
        Set<String> wdjid =  redis.smembers("wdjid");
        Iterator it = wdjid.iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }
    }
}
