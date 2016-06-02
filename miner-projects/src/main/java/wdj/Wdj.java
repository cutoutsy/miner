package wdj;

import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * 关于豌豆荚的类
 */
public class Wdj {

    //爬取前的准备工作
    public static void runJdPrepare(){
        RedisUtil re = new RedisUtil("cutoutsy.cn", 16379, "xidian123", 1);
        Jedis redis = re.getJedisInstance();
        if(redis.exists("2_white_set")){
            redis.del("2_white_set");
            System.out.println("2_white_set delete.");
        }
        if(redis.exists("2_black_set")){
            redis.del("2_black_set");
            System.out.println("2_black_set delete.");
        }
        if(redis.exists("wdjid1")){
            redis.del("wdjid1");
            System.out.println("wdjid1 delete.");
        }
        if(redis.exists("wdjid2")){
            redis.del("wdjid2");
            System.out.println("wdjid2 delete.");
        }

        System.out.println(redis.hget("project_state", "2-1"));
        redis.hset("project_state", "2-1", "die");
        System.out.println(redis.hget("project_state", "2-1"));
        redis.hset("project_executenum", "2-1", "0");
        redis.hset("project_cronstate", "2-1", "1");
        for(int i = 22; i < 30; i++){
            redis.sadd("wdjid", String.valueOf(i));
        }
        Set<String> allPages = redis.smembers("wdjid");
        Iterator it = allPages.iterator();
        while (it.hasNext()){
            String tempId = it.next().toString();
            System.out.println(tempId);
            redis.lpush("wdjid1", tempId);
        }

        redis.lpush("project_execute", "2-1");
    }

    public static void main(String[] args){
        runJdPrepare();
    }
}
