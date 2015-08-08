package miner.topo.platform;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.cutoutsy.utils.MyLogger;
import com.cutoutsy.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * The Bolt is Url Generator
 *
 * Created by cutoutsy on 8/5/15.
 */
public class GenerateUrlBolt extends BaseBasicBolt {

    private static MyLogger logger = new MyLogger(GenerateUrlBolt.class);

    private OutputCollector _collector;
    private Jedis redis;

    public void execute(Tuple input, BasicOutputCollector collector) {

        String emitUrl = "";
        try {
            String globalInfo = input.getString(0);
            String message = input.getString(1);

            String taskName = globalInfo.split("-")[0] + "-" + globalInfo.split("-")[1]+"-"+globalInfo.split("-")[2];
            System.out.println("taskName----"+taskName);
            Task ta = new Task(taskName);

//            System.out.println("ta.isgenerate:"+ta.getUrlgenerate());

            Boolean isGenerate = Boolean.valueOf(ta.getUrlgenerate()).booleanValue();
//            System.out.println("isgenerate:" + isGenerate);
            if (isGenerate) {
                //isGenerate=true, need to generate url
                emitUrl = PlatformUtils.GenerateUrl(message, ta.getUrlpattern());
            } else {
                //isgenerate=flase, do not generate url;
                emitUrl = message;
            }

            if (!emitUrl.isEmpty()) {
                collector.emit(new Values(globalInfo, emitUrl));
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
