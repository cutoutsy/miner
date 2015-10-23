package miner.topo;


import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by cutoutsy on 8/12/15.
 */
public class TestParse {

    public static void main(String[] args){
        Jedis redis = new Jedis("127.0.0.1",6379);
        redis.auth("xidian123");

        Set<String> id = redis.smembers("elongHotelId");
        Iterator it = id.iterator();

        while (it.hasNext()){
            String tempId = it.next().toString();
            System.out.println(tempId + "----");
            redis.sadd("elong_hotel_id", tempId);
            redis.rpush("elong_hotel_id1", tempId);
        }

    }
}
