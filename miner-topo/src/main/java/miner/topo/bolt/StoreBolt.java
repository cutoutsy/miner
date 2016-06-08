package miner.topo.bolt;
/**
 * 存储Bolt类
 */

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import miner.store.ImportData;
import miner.utils.MySysLogger;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class StoreBolt extends BaseRichBolt {

	private static MySysLogger logger = new MySysLogger(StoreBolt.class);
	private Jedis jedis;
	private RedisUtil ru;
	private OutputCollector _collector;

	public void execute(Tuple tuple) {
		long startTime=System.currentTimeMillis();
        String globalInfo  = tuple.getString(0);
        String data = tuple.getString(1);
		try {
            String workspace_id = get_workspace_id(globalInfo);
            //利用redis来进行数据的去重
            if(!jedis.sismember(workspace_id+"_unique", globalInfo)) {
                //将数据存放进HBase
                ImportData.importData(data);
                logger.info(globalInfo + ":save into hbase succeed!");
                jedis.sadd(workspace_id+"_unique", globalInfo);
                _collector.ack(tuple);
            }else{
                logger.warn(globalInfo+":已经存进数据库了.");
            }
		} catch (Exception ex) {
			_collector.fail(tuple);
			logger.error("store error!"+MySysLogger.formatException(ex));
			ex.printStackTrace();
		}

        long endTime=System.currentTimeMillis();
        logger.info(globalInfo+"在StoreBolt的处理时间:"+(endTime-startTime)/1000+"s.");
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this._collector = collector;
        ru = new RedisUtil();
        jedis = ru.getJedisInstance();
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

    //获取workspace的id
    private String get_workspace_id(String global_info){
        return global_info.split("-")[0];
    }
}
