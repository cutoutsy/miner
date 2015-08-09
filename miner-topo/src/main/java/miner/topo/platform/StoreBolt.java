package miner.topo.platform;
/**
 * 存储Bolt类
 * 
 */

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import miner.spider.utils.MyLogger;

import java.util.Map;

public class StoreBolt extends BaseBasicBolt {

	private static MyLogger logger = new MyLogger(StoreBolt.class);
	private OutputCollector _collector;

	public void execute(Tuple input, BasicOutputCollector collector) {
		try {
			String globalInfo  = input.getString(0);
			String resource = input.getString(1);

			logger.info("globalInfo:"+globalInfo);
			logger.info("resoure:"+resource);


		} catch (Exception ex) {
			logger.error("store error!"+ex);
			ex.printStackTrace();
		}
		
	}

	public void prepare(Map stormConf, TopologyContext context) {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}
