package miner.bolt;
/**
 * 存储Bolt类
 */

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import miner.spider.utils.IOUtil;
import miner.store.ImportData;
import miner.utils.MySysLogger;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class StoreBoltTest extends BaseRichBolt {

	private static MySysLogger logger = new MySysLogger(StoreBoltTest.class);
	private OutputCollector _collector;
    private Jedis jedis;
    private RedisUtil ru;

	public void execute(Tuple tuple) {
		try {
			String globalInfo  = tuple.getString(0);
			String data = tuple.getString(1);

			System.err.println(globalInfo + "=========");
//			ImportData.importData(data);

			System.err.println(data);
            String workspace_id = get_workspace_id(globalInfo);


            if(!jedis.sismember(workspace_id+"_unique", globalInfo)) {

                IOUtil.writeFile("/Users/cutoutsy/jd.txt", data + "\n", true, "utf-8");
                logger.info(globalInfo + ":save into hbase succeed!");
                jedis.sadd(workspace_id+"_unique", globalInfo);
            }else{
                System.out.println(globalInfo+":已经存进数据库了.");
            }
//			logger.info(data); //集群上注释
			_collector.ack(tuple);
		} catch (Exception ex) {
			_collector.fail(tuple);
			logger.error("store error!"+ex);
			ex.printStackTrace();
		}
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this._collector = collector;
        ru = new RedisUtil();
        jedis = ru.getJedisInstance();
	}

    private String get_workspace_id(String global_info){
        return global_info.split("-")[0];
    }

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}
