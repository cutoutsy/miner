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

import java.util.Map;


public class ParseBolt extends BaseBasicBolt{

	private static MyLogger logger = new MyLogger(ParseBolt.class);

	private OutputCollector _collector;

	public void execute(Tuple input, BasicOutputCollector collector) {
		try {

			String grobalInfo = input.getString(0);
			String resource = input.getString(1);
//			System.out.println(grobalInfo+"-------"+resource.substring(0, 20));
//			_collector.emit(new Values(grobalInfo, resource));
			collector.emit(new Values(grobalInfo, resource));

		}catch (Exception ex){
			logger.error("parse error!"+ex);
			ex.printStackTrace();
		}
		
	}
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
		_collector = collector;
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("p_globaleinfo","p_resouce"));
	}
	
	
	
}
