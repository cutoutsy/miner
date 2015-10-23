package miner.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @name RedisUtil
 * @author white
 * @info Pack the operation of Redis
 */
public class RedisUtil {
	private JedisPool pool = null;
	int jedis_instance_count = 0;


	public RedisUtil(String ip_address, int port, String password) {
		pool = this.init_jedis_pool(ip_address, port, password);
	}

    /* 保留这个构造器 */
    public RedisUtil(){

    }

	private JedisPool init_jedis_pool(String ip, int port, String password) {
		if (pool == null) {
			int max_active = 1000;
            int max_total = 1000;
			int max_idle = 1000;
			int max_wait = 100;
			JedisPoolConfig config = new JedisPoolConfig();
//			config.setMaxActive(max_active);
            config.setMaxWaitMillis(1000 * max_wait);
			config.setMaxIdle(max_idle);
            config.setMaxTotal(max_total);
//			config.setMaxWait(1000 * max_wait);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			try {
				pool = new JedisPool(config, ip, port, 1000 * 2, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pool;
	}

	public JedisPool get_jedis_pool() {
		return this.pool;
	}

	/* 从JedisPool中得到Jedis实例，并使引用计数加1 */
	public Jedis get_jedis_instance() {
		Jedis jedis = pool.getResource();
		jedis_instance_count++;
		if (jedis != null) {
			return jedis;
		}
		return null;
	}

    public Jedis get_jedis_instance_without_pool(){
        Jedis redis = new Jedis(StaticValue.redis_host,StaticValue.redis_port,10000);
        redis.auth(StaticValue.redis_auth);
        return redis;
    }

    public Jedis get_jedis_instance_without_pool(int database){
        Jedis redis = new Jedis(StaticValue.redis_host,StaticValue.redis_port,10000);
        redis.auth(StaticValue.redis_auth);
        redis.select(database);
        return redis;
    }

    public void set_key_expire(Jedis redis,String key,int days){
        int seconds = days*24*60*60;
        redis.expire(key, seconds);
    }

	public void release_jedis(Jedis jedis) {
		this.pool.returnResource(jedis);
		jedis_instance_count--;
	}

	/* 保存set */
	public void save_set(Jedis jedis, String set_name, Set<String> set) {
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			jedis.sadd(set_name, it.next());
		}
	}

	/* 删除set */
	public void clean_set(Jedis jedis, String set_name) {
		jedis.del(set_name);
	}

    /* 从set中随机取元素 */
    public String pick(Jedis jedis,String set_name){
        return jedis.spop(set_name);
    }

    /* 保存单个元素到set中 */
    public void add(Jedis jedis,String set_name,String element){
        jedis.sadd(set_name,element);
    }

    /* 查询set中的元素数量 */
    public Long num(Jedis jedis,String set_name){
        return jedis.scard(set_name);
    }

    /* 从miner-spider移过来的几个方法 */
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

}
