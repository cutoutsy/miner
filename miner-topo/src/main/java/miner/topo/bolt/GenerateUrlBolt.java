package miner.topo.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.spider.utils.MyLogger;
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

    private static MyLogger logger = new MyLogger(GenerateUrlBolt.class);

    private OutputCollector _collector;
    private Jedis redis;

    public void execute(Tuple input, BasicOutputCollector collector) {

        String emitUrl = "";
        String emitUrlLoop = "";
        try {
            String globalInfo = input.getString(0);
            String message = input.getString(1);
            emitUrl = PlatformUtils.getEmitUrl(globalInfo, message);
            UUID uuid = UUID.randomUUID();
            globalInfo = globalInfo+"-"+uuid;
            if (!emitUrl.isEmpty()) {
                collector.emit(new Values(globalInfo, emitUrl));
                System.out.println(globalInfo+"---"+emitUrl);
            }

            if(redis.hlen("loopMessage") > 0){
                Set keys = redis.hkeys("loopMessage");
                Iterator it = keys.iterator();
                it.hasNext();
                String globalInfoLoop = it.next().toString();
                String messageLoop = redis.hget("loopMessage", globalInfoLoop);

                JSONObject jsonObject = new JSONObject(messageLoop);
                //对property进行处理
                String property = jsonObject.getString("property");
                JSONObject propertyjson = new JSONObject(property);
                Iterator propertyKeys = propertyjson.keys();
                while (propertyKeys.hasNext()){
                    String key = propertyKeys.next().toString();
                    String propertyValue = propertyjson.getString(key);
                    Pattern pattern = Pattern.compile("\\[");
                    Matcher matcher = pattern.matcher(propertyValue);
                    if(matcher.find()){
                        JSONArray jsonArray = new JSONArray(propertyValue);
                        String propertyData = null;
                        for (int i = 0; i < jsonArray.length();i++) {
                            emitUrlLoop = PlatformUtils.getEmitUrl(globalInfoLoop, jsonArray.getString(i));
                            UUID uuidLoop = UUID.randomUUID();
                            globalInfoLoop = globalInfoLoop+"-"+uuidLoop;
                            if (!emitUrlLoop.isEmpty()) {
                                collector.emit(new Values(globalInfoLoop, emitUrlLoop));
                            }
                        }
                    }else{
                        emitUrlLoop = PlatformUtils.getEmitUrl(globalInfoLoop, propertyValue);
                        UUID uuidLoop = UUID.randomUUID();
                        globalInfoLoop = globalInfoLoop+"-"+uuidLoop;
                        if (!emitUrlLoop.isEmpty()) {
                            collector.emit(new Values(globalInfoLoop, emitUrlLoop));
                        }
                    }
                }
                redis.hdel("loopMessage", globalInfoLoop);
            }
        }catch (Exception ex){
            logger.error("Generate Url error:"+ex);
            ex.printStackTrace();
        }
    }

    public void prepare(Map stormConf, TopologyContext context) {
        redis = RedisUtil.GetRedis();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("g_globalinfo","g_url"));
    }

    public void cleanup() {
    }
}
