package miner.topo.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import miner.store.CreateTable;
import miner.topo.enumeration.ProjectState;
import miner.topo.platform.PlatformUtils;
import miner.topo.platform.Project;
import miner.topo.platform.QuartzManager;
import miner.topo.platform.Task;
import miner.utils.MySysLogger;
import miner.utils.RedisUtil;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeginSpout extends BaseRichSpout{

	private static MySysLogger logger = new MySysLogger(BeginSpout.class);
	private SpoutOutputCollector _collector;
	private HashMap<String, String> _runningProject = new HashMap<String, String>();
	private QuartzManager _qManager;
	private RedisUtil ru;
	private Jedis redis;
	
	public void ack(Object msgId){
		redis.hdel("message", msgId.toString());
		logger.info("message:" + msgId + "processd sucess!");
	}
	
	//ensure the message processed
	public void fail(Object msgId){
		logger.info("消息处理失败:" + msgId + ";正在重新发送......");
		String message = redis.hget("message", msgId.toString());
		String globalInfo = msgId.toString().split("-")[0]+"-"+msgId.toString().split("-")[1]+"-"+msgId.toString().split("-")[2];
		_collector.emit(new Values(globalInfo, message), msgId);
	}

	public void nextTuple() {
		try {
			Thread.sleep(20);
			PlatformUtils.registerProject(_qManager);

			List<String> newAddProject = PlatformUtils.getProjectList();

			if (newAddProject.size() > 0) {
				for (int i = 0; i < newAddProject.size(); i++) {
					String tempProjectName = newAddProject.get(i);
					Project pj = new Project(tempProjectName);
					//create table in hbase
					CreateTable.mysqlCheck(pj.getWid(), pj.getPid());

					String tempDatasource = pj.getDatasource();
					if (redis.llen(tempDatasource + "1") == redis.smembers(tempDatasource).size()) {
						_runningProject.put(newAddProject.get(i), tempDatasource + "1");
					} else if(redis.llen(tempDatasource + "2") == redis.smembers(tempDatasource).size()){
						_runningProject.put(newAddProject.get(i), tempDatasource + "2");
					}else{
						logger.error(tempProjectName + " 消息源数据不一致.");
					}
					redis.hset("project_state", tempProjectName, ProjectState.running.toString());
				}
			}else{
				logger.info("No new project need to execute!");
			}

			for (Map.Entry<String, String> entry : _runningProject.entrySet()) {
				String emitMessage = "";
				String oneProjectName = entry.getKey();
				String oneProjectDatasource = entry.getValue();
				Project pj = new Project(oneProjectName);
				if (redis.llen(oneProjectDatasource) <= 0) {
					redis.hset("project_state", oneProjectName, ProjectState.die.toString());
					//projectExecuteNum++
					pj.addProjectExecuteNum(pj);
					_runningProject.remove(oneProjectName);
				} else {
					List taskList = PlatformUtils.getTaskByProject(oneProjectName);
					for (int i = 0; i < taskList.size(); i++) {
						String dataSource = oneProjectDatasource;
						Task task = new Task(oneProjectName + "-" + taskList.get(i));
						//isloop=false, emit from the datasource
						if (task.getIsloop().equals("false")) {
							emitMessage = redis.lpop(oneProjectDatasource);
							String taskId = task.getTid();
							String globalInfo = oneProjectName + "-" + taskId;
							String msgId = globalInfo+"-"+PlatformUtils.getUUID();
							_collector.emit(new Values(globalInfo, emitMessage), msgId);
							redis.hset("message", msgId, emitMessage);
							logger.info(globalInfo+"--"+emitMessage + "  sending...");
						}
					}
				}
				if(redis.llen(oneProjectDatasource) == 0 ){
					redis.hset("project_state", oneProjectName, ProjectState.die.toString());
				}
				String oneOriginDatasource = oneProjectDatasource.substring(0, oneProjectDatasource.length()-1);
				if (oneProjectDatasource.contains("1") && !emitMessage.isEmpty()) {
					redis.rpush(oneOriginDatasource + "2", emitMessage);
				} else if(oneProjectDatasource.contains("2") && !emitMessage.isEmpty() ){
					redis.rpush(oneOriginDatasource + "1", emitMessage);
				}else{
					logger.error("message exchange error!");
				}
			}
		if(_runningProject.size() <= 0){
			logger.info("No project need to execute!");
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			_collector = collector;

			ru = new RedisUtil();
			redis = ru.getJedisInstance();

			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			_qManager = new QuartzManager();
			_qManager.setScheduler(scheduler);
			PlatformUtils.initRegisterProject(_qManager);
			scheduler.start();
			//init Hbase tables

			CreateTable.initHbaseTable();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("globalInfo","message"));
	}

	public void close() {
		ru.release_jedis(redis);
	}
	
}

