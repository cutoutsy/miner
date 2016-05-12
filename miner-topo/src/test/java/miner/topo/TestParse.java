package miner.topo;


import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by cutoutsy on 8/12/15.
 */
public class TestParse {

    public static void main(String[] args){
        Jedis redis = new Jedis("cutoutsy.cn",16379);
        redis.auth("xidian123");

        Set<String> id = redis.smembers("mailuntaiid");
        Iterator it = id.iterator();

        while (it.hasNext()){
            String tempId = it.next().toString();
            System.out.println(tempId + "----");

        }

    }
}
