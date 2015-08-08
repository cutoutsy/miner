package miner.topo.platform;


import miner.spider.utils.MysqlUtil;
import miner.spider.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Project class
 *
 * Created by cutoutsy on 8/5/15.
 */
public class Project {
    private static Jedis redis;

    private String wid;
    private String pid;
    private String name;
    private String description;
    private String datasource;
    private String schedule;
    private String state;

    public String getExecuteNum() {
        return executeNum;
    }

    public void setExecuteNum(String executeNum) {
        this.executeNum = executeNum;
    }

    private String executeNum;

    private String condition;

    public Project(String projectName) {

        redis = RedisUtil.GetRedis();

        List<String> projectList = MysqlUtil.getProject(projectName);
        String projectState = redis.hget("project_state", projectName);
        String projectExecuteNum = redis.hget("project_executenum", projectName);
        this.wid = projectList.get(0);
        this.pid = projectList.get(1);

        this.name = projectList.get(2);
        this.description = projectList.get(3);
        this.datasource = projectList.get(4);
        this.schedule = projectList.get(5);
        this.state = projectState;
        this.condition = projectList.get(6);
        this.executeNum = projectExecuteNum;
    }


    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    //write Project info to redis
    public static void writeProjectToRedis(Project pj){
        String projectKey = pj.wid+"-"+pj.pid;
        String projectValue = pj.state;
        redis = RedisUtil.GetRedis();
        redis.hset("project_state", projectKey, projectValue);
        redis.hincrBy("project_executenum", projectKey, 1);
    }

    public static void main(String[] args){
        Project pj = new Project("1-1");
        System.out.println(pj.condition);
    }

}
