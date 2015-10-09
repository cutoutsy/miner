package miner.topo.platform;


import miner.spider.utils.MysqlUtil;

import java.util.List;

/**
 * QuartzJob class
 *
 * Created by cutoutsy on 8/7/15.
 */
public class QuartzJob {

    //private Integer jobId;
    private String jobName;
    private String jobGroup;
    /**
     * 任务状态 0禁用 1启用 2删除 3未注册
     */
    private String jobStatus;
    private String cronExpression;


    public QuartzJob(String projectName){
//        this.scheduler = scheduler;
        List<String> projectList = MysqlUtil.getProject(projectName);
        this.jobGroup = projectList.get(0);
        this.jobName = projectList.get(1);
        this.jobStatus = "3";
        this.cronExpression = projectList.get(5);
    }

//    public Integer getJobId() {
//        return jobId;
//    }
//
//    public void setJobId(Integer jobId) {
//        this.jobId = jobId;
//    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }


}
