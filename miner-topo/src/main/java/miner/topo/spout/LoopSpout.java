package miner.topo.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import miner.topo.platform.PlatformUtils;
import miner.topo.platform.QuartzManager;
import miner.utils.MySysLogger;
import miner.utils.RedisUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LoopSpout
 */
public class LoopSpout extends BaseRichSpout{

    private static MySysLogger logger = new MySysLogger(BeginSpout.class);
    private SpoutOutputCollector _collector;
    private RedisUtil ru;
    private Jedis _redis;

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector){
        this._collector = collector;
        this.ru = new RedisUtil();
        this._redis = ru.getJedisInstance();
    }

    //once emit one
    public void nextTuple(){
        try {
            Thread.sleep(100);
            Set<String> infoKeys = _redis.hkeys("message_loop");
            Iterator it = infoKeys.iterator();
            while (it.hasNext()){
                String tempKey = it.next().toString();
                if(!_redis.sismember("message_emit", tempKey)){
                    String tempUrl = _redis.hget("message_loop", tempKey);
                    String emitGlobalInfo = tempKey.split("-")[0]+"-"+tempKey.split("-")[1]+"-"+tempKey.split("-")[2];
                    _collector.emit(new Values(emitGlobalInfo, tempUrl), tempKey);
                    _redis.sadd("message_emit", tempKey);
                    logger.info(tempKey+"--"+tempUrl + "  sending...");
                    break;
                }
            }
        }catch (Exception e){
            logger.error("loopspout emit failed "+ e);
            e.printStackTrace();
        }
    }


    public void ack(Object msgId){
//        _redis.sadd("message_ackkk",_redis.hget("message_loop", msgId.toString()));
        _redis.hdel("message_loop", msgId.toString());
        _redis.srem("message_emit", msgId.toString());
        logger.info(msgId.toString() + " ack.");
    }


    public void fail(Object msgId){
        String globalInfo = msgId.toString();
        String emitUrl = _redis.hget("message_loop", globalInfo);
        String emitGlobalInfo = globalInfo.split("-")[0]+"-"+globalInfo.split("-")[1]+"-"+globalInfo.split("-")[2];
        removeLoopMessage(emitGlobalInfo, emitUrl);
        _collector.emit(new Values(emitGlobalInfo, emitUrl), msgId);
        logger.warn(globalInfo + " fail,retry......");
    }

    public void removeLoopMessage(String taskInfo, String message){
        try {
            JSONObject jsonObject = new JSONObject(message);
            //对property进行处理
            String property = jsonObject.getString("property");
            JSONObject propertyjson = new JSONObject(property);
            Iterator propertyKeys = propertyjson.keys();
            while (propertyKeys.hasNext()) {
                String key = propertyKeys.next().toString();
                String propertyValue = propertyjson.getString(key);
                Pattern pattern = Pattern.compile("\\[");
                Matcher matcher = pattern.matcher(propertyValue);
                if (matcher.find()) {
                    JSONArray jsonArray = new JSONArray(propertyValue);
                    String propertyData = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String loopMessage = jsonArray.getString(i);
                        _redis.srem(taskInfo, loopMessage);
                    }
                } else {
                    String loopMessage = propertyValue;
                    _redis.srem(taskInfo, loopMessage);
                }
            }
        }catch (Exception e){
            logger.error("loopspout remove loopmessage error"+e);
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("loopSpoutGlobalInfo","loopSpoutMessage"));
    }

    public void close() {
        ru.release_jedis(_redis);
    }

}
