package miner.topo.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.proxy.TaskProxySetting;
import miner.spider.utils.MySysLogger;
import miner.proxy.RedisUtil;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by white on 15/9/24.
 */
public class ProxyBolt extends BaseRichBolt {
    private static MySysLogger logger = new MySysLogger(ProxyBolt.class);
    private OutputCollector _collector;
    private Jedis jedis;
    private RedisUtil ru;
    private Map<String,TaskProxySetting> workspace_setting;

    /* 更新单个workspace的代理池，在workspace初始和运行中两种情况都包含 */
    private void refresh_workspace_proxy_pool(int workspace_id){
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
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()) + " refresh wid:" + workspace_id + " workspace proxy pool");
    }

    private String get_workspace_id(String global_info){
        return global_info.split("-")[0];
    }

    private void check_workspace_id(String workspace_id,int delay_mills){
        if(!workspace_setting.containsKey(workspace_id+"")){
            workspace_setting.put(workspace_id+"",new TaskProxySetting(delay_mills));
        }
    }

    public void execute(Tuple input) {
        String download_url="";
        try {
            String global_info = input.getString(0);
            download_url = input.getString(1);
            String workspace_id=get_workspace_id(global_info);
            this.check_workspace_id(workspace_id,1000*2);


            /* 查询redis得到proxy */


            //对handle_name的set进行查询，得到proxy，并在white_set中删除proxy
            //将此proxy+"_"+当前的时间戳，存入black_set中
            //emit，并且要得到ack，否则继续查询并emit
//            while(true){
//
//
//                proxy=ru.pick(jedis,workspace_handle_name+"_white_set");
//                /* 加入黑名单 */
//                ru.add(jedis,workspace_handle_name+"_black_set",proxy+"_"+System.currentTimeMillis());
//                if(proxy!=null&&!proxy.equals("")){
//                    break;
//                }
//            }


//            logger.info("downloadurl:"+download_url+" get proxy:"+proxy);
//            _collector.emit(new Values(global_info, download_url, proxy));
            _collector.ack(input);
        }catch(Exception e){
            logger.error(download_url+" get proxy error "+e);
            _collector.fail(input);
        }
    }

    public void prepare(Map conf,TopologyContext context,OutputCollector collector){
        this._collector = collector;
        /* 初始化Jedis */
        //conf里面要封装ip port和password
        ru = new RedisUtil("127.0.0.1",6379,"password");
        jedis=ru.get_jedis_instance();
        workspace_setting=new HashMap<String, TaskProxySetting>();
    }
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("p_globalinfo","p_download_url","p_proxy"));
    }
}
