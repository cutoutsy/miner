package miner.topo.bolt;

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
import miner.spider.utils.MySysLogger;
import miner.spider.utils.MysqlUtil;
import miner.spider.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.*;

public class ParseBolt extends BaseRichBolt {

	private static MySysLogger logger = new MySysLogger(ParseBolt.class);

	private OutputCollector _collector;
	private HashMap<String, Data> _dataScheme;
	private HashMap<String, String> _regex;
	private Jedis _redis;

	public void execute(Tuple input) {
		try {
			String globalInfo = input.getString(0);
			String resource = input.getString(1);
			String projectInfo = globalInfo.split("-")[0]+globalInfo.split("-")[1]+globalInfo.split("-")[2];

			HashMap<String, Data> parseData = new HashMap<String, Data>();
			boolean findDataScheme = true;

			for (Map.Entry<String, Data> entry : _dataScheme.entrySet()) {
				String dataInfo = entry.getKey();
				String tempProjectInfo = dataInfo.split("-")[0]+dataInfo.split("-")[1]+dataInfo.split("-")[2];
				if(projectInfo.equals(tempProjectInfo)){
					parseData.put(dataInfo, entry.getValue());
					findDataScheme = false;
				}
			}

			//data define not in _dataScheme
			if(findDataScheme){
				HashMap<String, Data> newData = MysqlUtil.getDataByDataInfo(globalInfo.split("-")[0], globalInfo.split("-")[1], globalInfo.split("-")[2]);
				for (Map.Entry<String, Data> entry : newData.entrySet()) {
					parseData.put(entry.getKey(), entry.getValue());
				}
			}

			for (Map.Entry<String, Data> entry : parseData.entrySet()) {
				String dataInfo = entry.getKey();
				String taskInfo = dataInfo.split("-")[0]+"-"+dataInfo.split("-")[1]+"-"+dataInfo.split("-")[2];
				Data data = entry.getValue();
				String[] properties = data.getProperty().split("\\$");
				Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
				for(int i = 0; i < properties.length; i++){
					String tagName = properties[i];
					String path = _regex.get(taskInfo+"-"+tagName);
					data_rule_map.put(tagName, new RuleItem(tagName, path));
				}
				Set<DataItem> data_item_set = new HashSet<DataItem>();
				data_item_set.add(new DataItem(data.getWid(), data.getPid(), data.getTid(), data.getDid(), data.getRowKey(), data.getForeignKey(),
						data.getForeignValue(), data.getLink(), properties));
				/* 数据生成器 */
				Generator g = new Generator();
				g.create_obj(resource);
				for (Map.Entry<String, RuleItem> entry1 : data_rule_map.entrySet()) {
					g.set_rule(entry1.getValue());
				}
				g.generate_data();
				Map<String, Object> m = g.get_result();// m里封装了所有抽取的数据
				Iterator<DataItem> data_item_it = data_item_set.iterator();
				if(data.getProcessWay().equals("s")) {
					while (data_item_it.hasNext()) {
						Packer packerData = new Packer(data_item_it.next(), m, data_rule_map);
						String[] result_str=packerData.pack();
						for(int i=0;i<result_str.length;i++){
							emit("store", input, globalInfo, result_str[i]);
							logger.info(result_str[i]);
						}
//						emit("store", input, globalInfo, packerData.pack());
//						logger.info(packerData.pack());
					}
				}else if(data.getProcessWay().equals("e") || data.getProcessWay().equals("E")){
					while (data_item_it.hasNext()) {
						String loopTaskId = data.getLcondition();
						String loopTaskInfo = taskInfo.split("-")[0]+"-"+dataInfo.split("-")[1]+"-"+loopTaskId;
						Packer packerData = new Packer(data_item_it.next(), m, data_rule_map);
						String[] result_str=packerData.pack();
						for(int i=0;i<result_str.length;i++){
							emit("generate-loop", input, loopTaskInfo, result_str[i]);
							logger.info(result_str[i]);
						}
//						emit("generate-loop", input, loopTaskInfo, packerData.pack());
//						logger.info(packerData.pack());
					}
				}else{
					logger.error("there is no valid way to process "+taskInfo+" data");
				}
			}
			_collector.ack(input);
		}catch (Exception ex){
			logger.error("parse error!"+ex);
			ex.printStackTrace();
			_collector.fail(input);
		}
	}

	private void emit(String streamId, Tuple input,String globalInfo, String message){
		_collector.emit(streamId, input, new Values(globalInfo, message));
		logger.info("Parse, message emitted: globalInfo=" + globalInfo + ", message=" + message);
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
		this._collector = collector;
		_dataScheme = MysqlUtil.getData();
		_regex = MysqlUtil.getRegex();
		_redis = RedisUtil.GetRedis();
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
//		declarer.declare(new Fields("p_globaleinfo","p_resouce"));
		declarer.declareStream("generate-loop", new Fields("p_globalinfo", "p_data"));
		declarer.declareStream("store", new Fields("p_globalinfo", "p_data"));
	}

}
