package miner.topo.platform;

import miner.utils.RedisUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import redis.clients.jedis.Jedis;

/**
 * Project Class
 */
public class ProjectJob implements Job {
    private RedisUtil ru;
    private Jedis redis;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        ru = new RedisUtil();
        redis = ru.getJedisInstance();
//        Jedis redis = RedisUtil.GetRedis();
        String projectWid = context.getJobDetail().getKey().toString().split("\\.")[0];
        String projectPid = context.getJobDetail().getKey().toString().split("\\.")[1];
        redis.lpush("project_execute", projectWid+"-"+projectPid);
    }

}
