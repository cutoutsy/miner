package miner.topo;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import miner.parse.*;
import miner.parse.data.DataItem;
import miner.parse.data.Packer;
import miner.spider.pojo.Data;
import miner.spider.utils.MyLogger;
import miner.spider.utils.MysqlUtil;

import java.util.*;

public class ParseTestBolt extends BaseBasicBolt{

	private static MyLogger logger = new MyLogger(ParseTestBolt.class);

	private OutputCollector _collector;
	private HashMap<String, Data> _dataScheme;
	private HashMap<String, String> _regex;

	public void execute(Tuple input, BasicOutputCollector collector) {
		try {
			String globalInfo = "1-1-1-uuid";
			String resource = input.getString(0);
			String projectInfo = globalInfo.split("-")[0]+globalInfo.split("-")[1]+globalInfo.split("-")[2];
			HashMap<String, Data> parseData = new HashMap<String, Data>();
			for (Map.Entry<String, Data> entry : _dataScheme.entrySet()) {
				String dataInfo = entry.getKey();
				String tempProjectInfo = dataInfo.split("-")[0]+dataInfo.split("-")[1]+dataInfo.split("-")[2];
				if(projectInfo.equals(tempProjectInfo)){
					parseData.put(dataInfo, entry.getValue());
				}
			}

			for (Map.Entry<String, Data> entry : parseData.entrySet()) {
				String dataInfo = entry.getKey();
				String taskRegex = dataInfo.split("-")[0]+"-"+dataInfo.split("-")[1]+"-"+dataInfo.split("-")[2];
				Data data = entry.getValue();
				String[] properties = data.getProperty().split("\\$");
				Map<String, RuleItem> data_rule_map = new HashMap<String, RuleItem>();
				for(int i = 0; i < properties.length; i++){
					String tagName = properties[i];
					String path = _regex.get(taskRegex+"-"+tagName);
					data_rule_map.put(tagName, new RuleItem(tagName,
							path, "text", DataType.STR));
				}
				Set<DataItem> data_item_set = new HashSet<DataItem>();
				data_item_set.add(new DataItem(data.getWid(), data.getPid(), data.getTid(), data.getDid(), data.getRowKey(), data.getForeignKey(),
						data.getForeignValue(), data.getLink(), properties));
				/* 数据生成器 */
				Generator g = new Generator();
				g.create_obj(resource, Enum.valueOf(DocType.class,data.getDocType().toUpperCase()), CharSet.UTF8);
				for (Map.Entry<String, RuleItem> entry1 : data_rule_map.entrySet()) {
					g.set_rule(entry1.getValue());
				}
				g.generate_data();
				Map<String, Object> m = g.get_result();// m里封装了所有抽取的数据
				Iterator<DataItem> data_item_it = data_item_set.iterator();
				while (data_item_it.hasNext()) {
					Packer packerData = new Packer(data_item_it.next(), m, data_rule_map);
					collector.emit(new Values(globalInfo, packerData.pack()));
				}
			}
		}catch (Exception ex){
			logger.error("parse error!"+ex);
			ex.printStackTrace();
		}
	}
	
	public void prepare(Map stormConf, TopologyContext context){
		_dataScheme = MysqlUtil.getData();
		_regex = MysqlUtil.getRegex();
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("p_globaleinfo","p_resouce"));
	}
	
}
