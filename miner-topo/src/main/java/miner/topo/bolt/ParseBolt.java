package miner.topo.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.parse.*;
import miner.parse.data.DataItem;
import miner.parse.data.Packer;
import miner.parse.util.Reflect;
import miner.spider.pojo.Data;
import miner.spider.utils.MysqlUtil;
import miner.topo.platform.PlatformUtils;
import miner.utils.MySysLogger;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.*;

public class ParseBolt extends BaseRichBolt {

	private static MySysLogger logger = new MySysLogger(ParseBolt.class);

	private OutputCollector _collector;
	private HashMap<String, Data> _dataScheme;
	private HashMap<String, String> _regex;
	private RedisUtil _ru;
	private Jedis _redis;

	public void execute(Tuple tuple) {
		try {
			String globalInfo = tuple.getString(0);
			String resource = tuple.getString(1);
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
                String parseResource = resource;
				String dataInfo = entry.getKey();
				String taskInfo = dataInfo.split("-")[0]+"-"+dataInfo.split("-")[1]+"-"+dataInfo.split("-")[2];
				Data data = entry.getValue();
				String[] properties = data.getProperty().split("\\$");
				Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
				if(properties[0].equals("reflect")){
					for(int i = 1; i < properties.length; i++){
						String tagName = properties[i];
						String path = _regex.get(taskInfo+"-"+tagName);
						data_rule_map.put(tagName, new RuleItem(tagName, path));
                        int k = i-1;
                        properties[k] = properties[i];
					}
                    parseResource = Reflect.GetReflect("/opt/build/reflect/reflect.jar", parseResource);
				}else {
					for (int i = 0; i < properties.length; i++) {
						String tagName = properties[i];
						String path = _regex.get(taskInfo + "-" + tagName);
						data_rule_map.put(tagName, new RuleItem(tagName, path));
					}
				}
				Set<DataItem> data_item_set = new HashSet<DataItem>();
				data_item_set.add(new DataItem(data.getWid(), data.getPid(), data.getTid(), data.getDid(), data.getRowKey(), data.getForeignKey(),
						data.getForeignValue(), data.getLink(), properties));
				/* 数据生成器 */
				Generator g = new Generator();
				g.create_obj(parseResource);
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
							emit("store", tuple, globalInfo, result_str[i]);
						}
					}

				}else if(data.getProcessWay().equals("e") || data.getProcessWay().equals("E")){
					while (data_item_it.hasNext()) {
						String loopTaskId = data.getLcondition();
						String loopTaskInfo = taskInfo.split("-")[0]+"-"+dataInfo.split("-")[1]+"-"+loopTaskId;
						Packer packerData = new Packer(data_item_it.next(), m, data_rule_map);
						String[] result_str=packerData.pack();
						for(int i=0;i<result_str.length;i++){
							emit("generate-loop", tuple, loopTaskInfo, result_str[i]);
							//set url to redis for LoopSpout get
							//_redis.hset("message_loop", loopTaskInfo, result_str[i]);
						}
					}
				}else if(data.getProcessWay().equals("l") || data.getProcessWay().equals("L")){
					while (data_item_it.hasNext()) {
						String loopTaskId = data.getLcondition();
						String loopTaskInfo = taskInfo.split("-")[0]+"-"+dataInfo.split("-")[1]+"-"+loopTaskId;
						Packer packerData = new Packer(data_item_it.next(), m, data_rule_map);
						String[] result_str=packerData.pack();
						for(int i=0;i<result_str.length;i++){
							//set url to redis for LoopSpout get
							String uuid = PlatformUtils.getUUID();
							String tempEmitInfo = loopTaskInfo+"-"+uuid;
							_redis.hset("message_loop", tempEmitInfo, result_str[i]);
							logger.info(tempEmitInfo + "--" + result_str[i] + "--store to message_loop.");
						}
					}
				}else{
					logger.error("there is no valid way to process "+taskInfo+" data");
				}
			}
			_collector.ack(tuple);
		}catch (Exception ex){
			logger.error("parse error!"+ex);
			ex.printStackTrace();
			_collector.fail(tuple);
		}
	}

	private void emit(String streamId, Tuple tuple,String globalInfo, String message){
		_collector.emit(streamId, tuple, new Values(globalInfo, message));
		logger.info("Parse, message emitted: globalInfo=" + globalInfo + ", message=" + message);
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector){
		this._collector = collector;
		_dataScheme = MysqlUtil.getData();
		_regex = MysqlUtil.getRegex();
		_ru = new RedisUtil();
		_redis = _ru.getJedisInstance();
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("generate-loop", new Fields("p_globalinfo", "p_data"));
		declarer.declareStream("store", new Fields("p_globalinfo", "p_data"));
	}

	public void cleanup() {
		_ru.release_jedis(_redis);
	}
}
