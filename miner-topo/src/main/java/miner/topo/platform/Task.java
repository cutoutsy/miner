package miner.topo.platform;

import miner.spider.utils.MysqlUtil;
import miner.spider.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 *
 * Task class
 *
 */
public class Task {
    private static Jedis redis;

    private String wid;
    private String pid;
    private String tid;
    private String name;
    private String description;
    private String urlpattern;
    private String urlgenerate;

    public String getIsloop() {
        return isloop;
    }

    public void setIsloop(String isloop) {
        this.isloop = isloop;
    }

    private String isloop;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;

    public Task(String taskName) {
        redis = RedisUtil.GetRedis();
//        String taskValue = redis.hget("taskInfo", taskName);
        List<String> taskList = MysqlUtil.getTask(taskName);
        this.wid = taskList.get(0);
        this.pid = taskList.get(1);
        this.tid = taskList.get(2);

        this.name = taskList.get(3);;
        this.description = taskList.get(4);;
        this.urlpattern = taskList.get(5);;
        this.urlgenerate = taskList.get(6);
        this.isloop = taskList.get(7);
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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    public String getUrlpattern() {
        return urlpattern;
    }

    public void setUrlpattern(String urlpattern) {
        this.urlpattern = urlpattern;
    }

    public String getUrlgenerate() {
        return urlgenerate;
    }

    public void setUrlgenerate(String urlgenerate) {
        this.urlgenerate = urlgenerate;
    }

    //write Project info to redis
    public static void writeTaskToRedis(Task ta){
        String taskKey = ta.wid+"-"+ta.pid+"-"+ta.tid;
        String taskValue = ta.name+"$"+ta.description+"$"+ta.urlpattern+"$"+ta.urlgenerate+"$"+ta.state;
        redis = RedisUtil.GetRedis();
        redis.hset("taskInfo", taskKey, taskValue);
    }

    public static void main(String[] args){
        Task ta = new Task("1-1-1");
        System.out.println(ta.urlgenerate);
    }
}
