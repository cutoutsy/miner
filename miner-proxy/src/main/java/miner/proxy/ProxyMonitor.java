package miner.proxy;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by white on 15/10/11.
 */
public class ProxyMonitor {
    private Jedis jedis;

    /* 两种构造器 */
    public ProxyMonitor(String ip_address, int port, String password){
        jedis=new RedisUtil(ip_address, port, password).get_jedis_instance();
    }

    public ProxyMonitor(RedisUtil ru){
        jedis=ru.get_jedis_instance();
    }

    /* 两个基本方法 */
    public int get_set_count(String set_name){
        return jedis.smembers(set_name).size();
    }

    public Set<String> get_set(String set_name){
        return jedis.smembers(set_name);
    }

    /* 对总IP池的访问 */
    public int get_main_proxy_count(){
        return this.get_set_count("proxy_pool");
    }

    public Set<String> get_main_proxy_set(){
        return this.get_set("proxy_pool");
    }

    /* 对各个workspace的IP池的访问*/
    public int get_workspace_pool_count(){
        return this.get_set_count("workspace_pool");
    }

    public Set<String> get_workspace_pool_set(){
        return this.get_set("workspace_pool");
    }

    /* 访问单个workspace的IP池，黑白名单数量用“_”隔开 */
    public String get_single_workspace_proxy_count(String workspace_name){
        int white_size=jedis.smembers(workspace_name+"_white_set").size();
        int black_size=jedis.smembers(workspace_name+"_black_set").size();
        return white_size+"_"+black_size;
    }

    /* 得到关于每个workspace中黑白名单中IP的数量，格式为workspace_name->white_num _ black_num */
    public Map<String,String> get_workspaces_proxy_count(){
        Set<String> workspace_names=this.get_workspace_pool_set();
        Map<String,String> return_map=new HashMap<String,String>();
        Iterator<String> it=workspace_names.iterator();
        while(it.hasNext()){
            String name=it.next();
            return_map.put(name,this.get_single_workspace_proxy_count(name));
        }
        return return_map;
    }

}
