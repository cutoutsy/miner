package miner.topo.platform;

import org.quartz.*;

/**
 * Created by cutoutsy on 8/7/15.
 */
public class QuartzManager {

//    private StdScheduler scheduler;
    private Scheduler scheduler;

    public void setScheduler(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    public void initJob(QuartzJob job, Class cls){
        System.out.println("初始化任务调度");
        try{
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
            CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
            if(null == trigger){
                addQuartzJob(job, trigger, cls);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //add task
    public void addQuartzJob(QuartzJob job, CronTrigger trigger, Class cls){
        System.out.println("向任务调度中添加定时任务");

        try{
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(job.getJobName(), job.getJobGroup()).build();
            jobDetail.getJobDataMap().put(job.getJobName(), job);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    void runJob(QuartzJob job){
        System.out.println("立即运行任务调度中的定时任务");
        try {
            if (null == job) {
                System.out.println("定时任务信息为空，无法立即运行");
                return;
            }
            JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
            if(null == jobKey){
                System.out.println("任务调度中不存在[" + job.getJobName() + "]定时任务，不予立即运行！");
                return;
            }

            scheduler.triggerJob(jobKey);

        } catch (Exception e) {
            System.out.println("立即运行任务调度中的定时任务异常！");
            e.printStackTrace();
        }
    }

    void updateQuartzJob(QuartzJob job, TriggerKey triggerKey, CronTrigger trigger){
        System.out.println("修改任务调度中的定时任务");
        try {
            if (null == job || null == triggerKey || null == trigger) {
                System.out.println("修改调度任务参数不正常！");
                return;
            }
            System.out.println("原始任务表达式:" + trigger.getCronExpression()
                    + "，现在任务表达式:" + job.getCronExpression());
            if (trigger.getCronExpression().equals(job.getCronExpression())) {
                System.out.println("任务调度表达式一致，不予进行修改！");
                return;
            }
            System.out.println("任务调度表达式不一致，进行修改");
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            System.out.println("修改任务调度中的定时任务异常！" + e.getMessage());
        }
    }


    void pauseJob(QuartzJob job){
        System.out.println("暂停任务调度中的定时任务");
        try {
            if (null == job) {
                System.out.println("暂停调度任务参数不正常！");
                return;
            }
            JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
            if(null == jobKey){
                System.out.println("任务调度中不存在[" + job.getJobName() + "]定时任务，不予进行暂停！");
                return;
            }
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            System.out.println("暂停任务调度中的定时任务异常！" + e.getMessage());
        }
    }

    void resumeJob(QuartzJob job){
        System.out.println("恢复任务调度中的定时任务");
        try {
            if (null == job) {
                System.out.println("恢复调度任务参数不正常！");
                return;
            }
            JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
            if(null == jobKey){
                System.out.println("任务调度中不存在[" + job.getJobName() + "]定时任务，不予进行恢复！");
                return;
            }
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            System.out.println("恢复任务调度中的定时任务异常！" + e.getMessage());
        }
    }


    void deleteJob(QuartzJob job){
        System.out.println("删除任务调度中的定时任务");
        try {
            if (null == job) {
                System.out.println("删除调度任务参数不正常！");
                return;
            }
            JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
            if(null == jobKey){
                System.out.println("任务调度中不存在[" + job.getJobName() + "]定时任务，不予进行删除！");
                return;
            }
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            System.out.println("删除任务调度中的定时任务异常！" + e.getMessage());
        }
    }


    void deleteJob(TriggerKey triggerKey){
        System.out.println("删除任务调度定时器");
        try {
            if(null == triggerKey){
                System.out.println("停止任务定时器参数不正常，不予进行停止！");
                return;
            }
            System.out.println("停止任务定时器");
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
        } catch (Exception e) {
            System.out.println("删除任务调度定时器异常！" + e.getMessage());
        }
    }




}
