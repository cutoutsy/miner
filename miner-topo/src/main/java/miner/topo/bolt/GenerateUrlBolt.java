package miner.topo.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.topo.platform.PlatformUtils;
import miner.topo.platform.Task;
import miner.utils.MySysLogger;
import miner.utils.RedisUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Bolt is Url Generator
 */
public class GenerateUrlBolt extends BaseRichBolt {

    private static MySysLogger logger = new MySysLogger(GenerateUrlBolt.class);

    private OutputCollector _collector;
    private RedisUtil ru;
    private Jedis _redis;

    public void execute(Tuple input) {
        try {
            String taskInfo = input.getString(0);
            String message = input.getString(1);
            Task ta = new Task(taskInfo);

            if(ta.getIsloop().equals("false")) {
                String emitUrl = PlatformUtils.getEmitUrl(taskInfo, message);
                UUID uuid = UUID.randomUUID();
                String globalInfo = taskInfo + "-" + uuid;
                if (!emitUrl.isEmpty()) {
                    _collector.emit(input, new Values(globalInfo, emitUrl));
                    logger.info(globalInfo + "---" + emitUrl);
                }
                _collector.ack(input);
                //loop process
            }else if(ta.getIsloop().equals("true")){
                if(_redis.exists(taskInfo)){
                    _redis.expire(taskInfo, 600);
                }
                JSONObject jsonObject = new JSONObject(message);
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
                            String loopMessage = jsonArray.getString(i);
                            if(!_redis.sismember(taskInfo, loopMessage)){
                                String emitUrlLoop = PlatformUtils.getEmitUrl(taskInfo, loopMessage);
                                UUID uuidLoop = UUID.randomUUID();
                                String globalInfo = taskInfo+"-"+uuidLoop;
                                if (!emitUrlLoop.isEmpty()) {
                                    _collector.emit(input, new Values(globalInfo, emitUrlLoop));
                                    _redis.sadd(taskInfo, loopMessage);
                              }
                            }
                        }
                    }else{
                        String loopMessage = propertyValue;
                        if(!_redis.sismember(taskInfo, loopMessage)) {
                            String emitUrlLoop = PlatformUtils.getEmitUrl(taskInfo, loopMessage);
                            UUID uuidLoop = UUID.randomUUID();
                            String globalInfo = taskInfo + "-" + uuidLoop;
                            if (!emitUrlLoop.isEmpty()) {
                                _collector.emit(input, new Values(globalInfo, emitUrlLoop));
                                _redis.sadd(taskInfo, loopMessage);
                            }
                        }
                    }
                }
                _collector.ack(input);
            }else{
                logger.warn("task loop config wrong.");
                _collector.fail(input);
            }
        }catch (Exception ex){
            _collector.fail(input);
            logger.error("Generate Url error:"+ex);
            ex.printStackTrace();
        }
    }

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this._collector = collector;
        ru = new RedisUtil();
        _redis = ru.getJedisInstance();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("g_globalinfo","g_url"));
    }

    public void cleanup() {
    }
}
