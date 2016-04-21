package miner.utils;

import junit.framework.TestCase;
import org.junit.Test;
import redis.clients.jedis.Jedis;

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
}
