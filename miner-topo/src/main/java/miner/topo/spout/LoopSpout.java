package miner.topo.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import miner.topo.platform.QuartzManager;
import miner.utils.MySysLogger;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

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

    public void nextTuple(){
        try {
            Thread.sleep(1000);

        }catch (Exception e){
            logger.error("loopspout emit failed "+ e);
            e.printStackTrace();
        }
    }


    public void ack(Object msgId){
        _redis.hdel("message_loop", msgId.toString());
        logger.info(msgId.toString()+" ack.");
    }


    public void fail(Object msgId){
        String globalInfo = msgId.toString();
        String emitUrl = _redis.hget("message_loop", globalInfo);
        _collector.emit(new Values(globalInfo, emitUrl), msgId);
        logger.warn(globalInfo+" fail,retry......");
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("loopSpoutGlobalInfo","loopSpoutMessage"));
    }

}
