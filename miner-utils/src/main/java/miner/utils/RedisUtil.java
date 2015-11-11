package miner.utils;
import java.util.Iterator;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
/**
 * @name RedisUtil
 * @author white
 * @info Pack the operation of Redis
 */
public class RedisUtil {
    private JedisPool pool = null;
    int jedis_instance_count = 0;
    public RedisUtil(){
        pool = this.initJedisPool(PlatformParas.redis_host, 6379, PlatformParas.redis_auth);
    }
    public RedisUtil(String ip_address, int port, String password) {
        pool = this.initJedisPool(ip_address, port, password);
    }
    private JedisPool initJedisPool(String ip, int port, String password) {
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
    public JedisPool getJedisPool() {
        return this.pool;
    }
    /* 从JedisPool中得到Jedis实例，并使引用计数加1 */
    public Jedis getJedisInstance() {
        Jedis jedis = pool.getResource();
        jedis_instance_count++;
        if (jedis != null) {
            return jedis;
        }
        return null;
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
    public static void main(String[] args){
        RedisUtil ru = new RedisUtil();
        Jedis redis = ru.getJedisInstance();
        Set<String> proxy = redis.smembers("proxy_pool");
        Iterator it = proxy.iterator();
        while (it.hasNext()){
            System.out.println(it.next());
        }
    }
}
