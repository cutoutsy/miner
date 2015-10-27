package miner.topo.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.proxy.ProxySetting;
import miner.utils.MySysLogger;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Proxy Bolt:Manage the proxy
 */
public class ProxyBolt extends BaseRichBolt {
    private static MySysLogger logger = new MySysLogger(ProxyBolt.class);
    private OutputCollector _collector;
    private Jedis jedis;
    private RedisUtil ru;
    private Map<String,ProxySetting> workspace_setting=new HashMap<String, ProxySetting>();

    /* 从proxy_pool更新单个workspace的代理池，在workspace初始和运行中两种情况都包含 */
    private void refresh_workspace_proxy_pool(String workspace_id){
        /* 完整的将proxy pool中的IP复制到此workspace的pool中来 */
        Set<String> new_proxy_set = null;
        while (new_proxy_set == null || new_proxy_set.size() == 0) {
            new_proxy_set = jedis.smembers("proxy_pool");
        }
        ru.clean_set(jedis, workspace_id + "_white_set");
        Iterator<String> it=new_proxy_set.iterator();
        while(it.hasNext()){
            String tmp=it.next();
            if(!jedis.sismember(workspace_id+"_black_set", tmp)){
                ru.add(jedis,workspace_id+"_white_set",tmp);
            }
        }
        logger.info("refresh wid:" + workspace_id + " workspace proxy pool");
    }

    private String get_workspace_id(String global_info){
        return global_info.split("-")[0];
    }

    public void execute(Tuple tuple) {
        try {

            String global_info = (String) tuple.getValue(0);
            String download_url = (String) tuple.getValue(1);
        /* delay_time需要从上一个得到 */
            int delay_time = 2 * 1000;
            String workspace_id = get_workspace_id(global_info);
//        System.err.println("WID "+workspace_id);

        /* ------加入workspace的setting------ */
            if (!workspace_setting.containsKey(workspace_id)) {
                workspace_setting.put(workspace_id, new ProxySetting(delay_time));
            }
            ProxySetting current_workspace_setting = workspace_setting.get(workspace_id);
//        System.err.println(current_workspace_setting==null);
        /* ----更新当前workspace的IP pool---- */
            Long last_update_time = current_workspace_setting.get_last_update_time();
        /* 暂且设置成10秒更新一次 */
            if (System.currentTimeMillis() - last_update_time > 1000 * 10) {
                refresh_workspace_proxy_pool(workspace_id);
                current_workspace_setting.set_last_update_time(System.currentTimeMillis());
            }

            String proxy = null;
            do {
            /* ----------更新黑白名单------------ */
                Set<String> black_set = jedis.smembers(workspace_id + "_black_set");
                Iterator<String> it = black_set.iterator();
                while (it.hasNext()) {
                    String tmp_ele = it.next();
                    String[] tmp = tmp_ele.split("_");
                    Long now = System.currentTimeMillis();
                    if (now - Long.parseLong(tmp[1]) > current_workspace_setting.get_delay_time()) {
                        jedis.srem(workspace_id + "_black_set", tmp_ele);
                        jedis.sadd(workspace_id + "_white_set", tmp[0]);
                    }
                }
            /* -------------查询--------------- */
                proxy = ru.pick(jedis, workspace_id + "_white_set");
            } while (proxy == null || proxy.equals(""));
            ru.add(jedis, workspace_id + "_black_set", proxy + "_" + System.currentTimeMillis());
            current_workspace_setting.set_last_action_time(System.currentTimeMillis());
        /* -------------回收--------------- */
            for (Map.Entry<String, ProxySetting> entry : workspace_setting.entrySet()) {
                String key = entry.getKey();
                ProxySetting tps = entry.getValue();
                Long last_action_time = tps.get_last_action_time();
                Long elapse_time = System.currentTimeMillis() - last_action_time;
                int dead_time = tps.get_dead_time();
                if (elapse_time > dead_time) {
                /* 在Redis中删除这个set */
                    ru.clean_set(jedis, workspace_id + "_white_set");
                    ru.clean_set(jedis, workspace_id + "_black_set");
                    workspace_setting.remove(key);
                }
            }
            _collector.emit(tuple, new Values(global_info, download_url, proxy));
            logger.info("manage proxy:"+proxy+" to "+global_info+":"+download_url);
            _collector.ack(tuple);
        }catch (Exception e){
            _collector.fail(tuple);
            logger.error("manage proxy error:"+e);
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("proxy_globalinfo","proxy_downloadurl","proxy"));
    }

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
        this._collector = collector;
        ru = new RedisUtil();
        jedis = ru.getJedisInstance();
    }

    public void cleanup() {
        ru.release_jedis(jedis);
    }
}
