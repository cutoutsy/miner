package miner.spider.utils;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by cutoutsy on 7/24/15.
 */
public class RedisUtil {

//    private String redis_host = StaticValue.redis_host;
//    private String redis_auth = StaticValue.redis_auth;

    public static Jedis GetRedis(){
        Jedis redis = new Jedis(StaticValue.redis_host,6379,10000);
        redis.auth(StaticValue.redis_auth);
        return redis;
    }

    public static Jedis GetRedis(int database){
        Jedis redis = new Jedis(StaticValue.redis_host,6379,10000);
        redis.auth(StaticValue.redis_auth);
        redis.select(database);
        return redis;
    }

    /**
     * @TODO:设置redis里键的过期天数
     * @return:void
     */
    public static void setKeyExpire(Jedis redis,String key,int days){
        int seconds = days*24*60*60;
        redis.expire(key, seconds);
    }

    //将文本写入redis,hash类型写入set
    public static void HashTxt2Redis(String path,String key){
        Jedis redis = RedisUtil.GetRedis();
        List<String> datas = IOUtil.getLineArrayFromFile(path, "utf-8");
        for(int i = 0; i < datas.size();i++){
//    		System.out.println(datas.get(i));
            String onedata = datas.get(i).split(":")[0];
            redis.sadd(key, onedata);
        }
    }


    public static void main(String[] args){
        String path = "/home/cutoutsy/bin/id_price.txt";
        HashTxt2Redis(path,"tuniuId");
    }
}
