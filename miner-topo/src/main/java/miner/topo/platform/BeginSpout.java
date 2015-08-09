package miner.topo.platform;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import miner.spider.utils.MyLogger;
import miner.spider.utils.RedisUtil;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BeginSpout extends BaseRichSpout{

	private static MyLogger logger = new MyLogger(BeginSpout.class);

	private SpoutOutputCollector _collector;
	private QuartzManager _qManager;
	private int length;
	private Jedis redis;

	
	//消息成功处理
	public void ack(Object msgId){
		logger.info("message:"+ msgId +"processd sucess!");
	}
	
	//消息失败处理, ensure the message processed
	public void fail(Object msgId){
		logger.info("消息处理失败:"+msgId+";正在重新发送......");
		String message = redis.hget("message", msgId.toString());
		_collector.emit(new Values(msgId, message),msgId);
	}

	public void nextTuple() {

		try{
			Thread.sleep(1000);
			//monitor the project, return the undo project
			PlatformUtils.registerProject(_qManager);

//			String reUndoProject = PlatformUtils.monitorProject();
			String reUndoProject = PlatformUtils.getProject();
		if(!reUndoProject.isEmpty()){

			Project pj = new Project(reUndoProject);

			String dataSource = pj.getDatasource();
			Set urls = redis.smembers(dataSource);
			Iterator it = urls.iterator();
			while(it.hasNext()){
				//sleep 3s for test
//				Thread.sleep(3000);
				String message = it.next().toString();
				//defalut one project has one task
//				String taskId = redis.hget("taskInfo", reUndoProject).split(":")[0];
				String taskId = "001";
				UUID uuid = UUID.randomUUID();

				String globalInfo = reUndoProject+"-"+taskId+"-"+uuid;

				_collector.emit(new Values(globalInfo, message), globalInfo);

				redis.hset("message", globalInfo , message);

				logger.info(message + "  sending...");
			}

			pj.setState("die");
			pj.writeProjectToRedis(pj);

		}else{
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

