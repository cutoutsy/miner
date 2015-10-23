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

import java.util.Map;

public class StoreBolt extends BaseRichBolt {

	private static MySysLogger logger = new MySysLogger(StoreBolt.class);
	private OutputCollector _collector;

	public void execute(Tuple tuple) {
		try {
			String globalInfo  = tuple.getString(0);
			String data = tuple.getString(1);

			logger.info("globalINfo:"+globalInfo);
			logger.info("data:" + data);

//			ImportData.importData(data);

			_collector.ack(tuple);
		} catch (Exception ex) {
			_collector.fail(tuple);
			logger.error("store error!"+ex);
			ex.printStackTrace();
		}

	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this._collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}
