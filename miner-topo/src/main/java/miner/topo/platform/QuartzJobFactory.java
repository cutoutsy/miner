package miner.topo.platform;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;


public class QuartzJobFactory implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {

        QuartzJob scheduleJob = (QuartzJob)context.getMergedJobDataMap().get("backup_job");

        System.out.println("定时任务开始执行，任务名称[" + scheduleJob.getJobName() + "]");

        Date previousFireTime = context.getPreviousFireTime();

        if(null != previousFireTime){
            System.out.println("定时任务上次调度时间：" + previousFireTime);
        }
        System.out.println("定时任务下次调度时间：" + context.getNextFireTime());
        // 执行业务逻辑
    }
}