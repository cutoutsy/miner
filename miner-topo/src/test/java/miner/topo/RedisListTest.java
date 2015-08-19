package miner.topo;

import miner.spider.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * Created by cutoutsy on 8/13/15.
 */
public class RedisListTest {
    public static void main(String[] args){
        Jedis redis = RedisUtil.GetRedis();
        List reList = redis.lrange("testList", 0, -1);

        for (int i =  0; i < reList.size(); i++){
            System.out.println(reList.get(i));
        }
    }
}
