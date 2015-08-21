package miner.topo;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.spider.utils.MySysLogger;
import miner.spider.utils.RedisUtil;
import miner.topo.platform.PlatformUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Bolt is Url Generator
 */
public class GenerateUrlBolt extends BaseBasicBolt {

    private static MySysLogger logger = new MySysLogger(GenerateUrlBolt.class);

    private OutputCollector _collector;

    public void execute(Tuple input, BasicOutputCollector collector) {


        try {
            String url = input.getString(0);
            if(url.length() > 10) {
                collector.emit(new Values(url));
                System.out.println(url);
            }else{
                System.err.println("======>loop back====>"+url);
            }
        }catch (Exception ex){
            logger.error("Generate Url error:"+ex);
            ex.printStackTrace();
        }
    }

    public void prepare(Map stormConf, TopologyContext context) {
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("g_globalinfo"));
    }

    public void cleanup() {
    }
}
