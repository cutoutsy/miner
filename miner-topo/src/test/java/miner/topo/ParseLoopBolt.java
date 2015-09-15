package miner.topo;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.parse.*;
import miner.parse.data.DataItem;
import miner.parse.data.Packer;
import miner.spider.pojo.Data;
import miner.spider.utils.MyLogger;
import miner.spider.utils.MysqlUtil;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Pattern;

public class ParseLoopBolt extends BaseRichBolt {

	private static MyLogger logger = new MyLogger(ParseLoopBolt.class);

	private OutputCollector _collector;

	public void execute(Tuple input) {
		try {
			String result = input.getString(0);

			if(result != null) {
				JSONObject jo = new JSONObject(result);
				String errorNum = jo.getString("error");
				if (errorNum.equals("0")) {
					JSONObject member = jo.getJSONObject("member");
					String avatar = member.getString("avatar");
					System.err.println("avatar:"+avatar);
					String nick = member.getString("nick");
					String registerDays = member.getString("registerDays");
//					collector.emit(new Values(uid, avatar, nick, registerDays));
					emit("loop", nick, input, registerDays);
				}else{
					emit("store", "not exist", input, errorNum);
				}
			}
			_collector.ack(input);
		}catch (Exception ex){
			logger.error("parse error!"+ex);
			ex.printStackTrace();
		}
	}
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
		this._collector = collector;
	}


	private void emit(String streamId, String type, Tuple input, String word){
		_collector.emit(streamId, input, new Values(type, word));
		System.out.println("Distribution, typed word emitted: type="+type+", word="+word);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
//		declarer.declare(new Fields("p_globaleinfo","p_resouce"));
		declarer.declareStream("loop", new Fields("type", "word"));
		declarer.declareStream("store", new Fields("type", "word"));
	}
	
}
