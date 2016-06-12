package miner.topo.platform;

import miner.spider.utils.MysqlUtil;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.HashMap;

/**
 *
 * Task class
 *
 */
public class Task {
    private static Jedis redis;
    private static RedisUtil ru;

    private String wid;
    private String pid;
    private String tid;
//    private String name;
    private String description;
    private String urlpattern;
    private String urlgenerate;
    //任务是否开启代理
    private String proxy_open;

    public String getProxy_open() {
        return proxy_open;
    }

    public void setProxy_open(String proxy_open) {
        this.proxy_open = proxy_open;
    }

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
        HashMap<String, String> taskMap = MysqlUtil.getTask(taskName);
        this.wid = taskMap.get("wid");
        this.pid = taskMap.get("pid");
        this.tid = taskMap.get("tid");

        this.description = taskMap.get("description");;
        this.urlpattern = taskMap.get("urlpattern");;
        this.urlgenerate = taskMap.get("urlgenerate");
        this.isloop = taskMap.get("isloop");
        this.proxy_open = taskMap.get("proxy_open");
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
        String taskValue = ta.description+"$"+ta.urlpattern+"$"+ta.urlgenerate+"$"+ta.state;
//        redis = RedisUtil.GetRedis();
//        ru = new RedisUtil();
//        redis = ru.getJedisInstance();
//        redis.hset("taskInfo", taskKey, taskValue);
    }

    public static void main(String[] args){
        Task ta = new Task("2-1-1");
        System.out.println(ta.urlgenerate+"=="+ta.getUrlpattern()+"--"+ta.getProxy_open());
    }
}
