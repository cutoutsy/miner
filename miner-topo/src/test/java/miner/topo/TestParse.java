package miner.topo;

import miner.parse.DocObject;
import miner.parse.DocType;
import miner.spider.httpclient.Crawl4HttpClient;
import miner.topo.platform.PlatformUtils;
import miner.topo.platform.QuartzManager;
import miner.utils.RedisUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Created by cutoutsy on 8/12/15.
 */
public class TestParse {

//    private static MyLogger logger = new MyLogger(TestParse.class);

    public static void main(String[] args){
        RedisUtil ru = new RedisUtil();
        Jedis redis = ru.getJedisInstance();

        Set<String> id = redis.smembers("elong_hotel_id");
        Iterator it = id.iterator();

        while (it.hasNext()){
            redis.rpush("elong_hotel_id1", it.next().toString());
        }

    }
}
