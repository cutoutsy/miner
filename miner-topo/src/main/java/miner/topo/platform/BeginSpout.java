package miner.topo.platform;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import miner.spider.utils.MyLogger;
import miner.spider.utils.RedisUtil;
import miner.store.CreateTable;
import miner.topo.enumeration.ProjectState;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;

public class BeginSpout extends BaseRichSpout{

	private static MyLogger logger = new MyLogger(BeginSpout.class);

	private SpoutOutputCollector _collector;
	private HashMap<String, String> _runningProject = new HashMap<String, String>();
	private QuartzManager _qManager;
	private int length;
	private Jedis redis;
	
	//消息成功处理
	public void ack(Object msgId){
		logger.info("message:" + msgId + "processd sucess!");
	}
	
	//消息失败处理, ensure the message processed
	public void fail(Object msgId){
		logger.info("消息处理失败:"+msgId+";正在重新发送......");
		String message = redis.hget("message", msgId.toString());
		_collector.emit(new Values(msgId, message), msgId);
	}

	public void nextTuple() {

		try {
			Thread.sleep(4000);
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
					} else {
						_runningProject.put(newAddProject.get(i), tempDatasource + "2");
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
							_collector.emit(new Values(globalInfo, emitMessage), globalInfo);
							redis.hset("message", globalInfo, emitMessage);
							logger.info(emitMessage + "  sending...");
						}
					}
				}
				if(redis.llen(oneProjectDatasource) == 0 ){
					redis.hset("project_state", oneProjectName, ProjectState.die.toString());
				}
				String oneOriginDatasource = oneProjectDatasource.substring(0, oneProjectDatasource.length()-1);
				if (oneProjectDatasource.contains("1")) {
					redis.rpush(oneOriginDatasource + "2", emitMessage);
				} else {
					redis.rpush(oneOriginDatasource + "1", emitMessage);
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
			redis = RedisUtil.GetRedis();

			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			_qManager = new QuartzManager();
			_qManager.setScheduler(scheduler);
			PlatformUtils.initRegisterProject(_qManager);

			scheduler.start();
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("globalInfo","message"));
	}
	
	
}

