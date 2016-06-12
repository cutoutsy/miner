package miner.spider;

import junit.framework.TestCase;
import miner.spider.httpclient.Crawl4HttpClient;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 测试Httpclient网页下载
 */
public class CrawlTest extends TestCase{

    public void testDownLoadPage(){
        RedisUtil ru = new RedisUtil();

        Jedis jedis = ru.getJedisInstance();

        Set<String> proxy_pool = jedis.smembers("proxy_pool");

        List proxy_list = new ArrayList(proxy_pool);

        for(int i = 22; i < 1000; i++){

            String tempUrl = "https://account.wandoujia.com/v4/api/simple/profile?uid="+String.valueOf(i);

//            String re = Crawl4HttpClient.downLoadPage(tempUrl, proxy_list.get(i).toString());
            String re = Crawl4HttpClient.downLoadPage(tempUrl);
            System.out.println(tempUrl);
            System.out.println(re);
        }

    }
}
