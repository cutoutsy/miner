package jd;

import miner.spider.httpclient.Crawl4HttpClient;
import miner.utils.RedisUtil;
import redis.clients.jedis.Jedis;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  关于京东的类
 */
public class Jd {
    //http://club.jd.com/productpage/p-1861111-s-0-t-3-p-0.html?callback=fetchJSON_comment98vv2956

    //生成jd某个商品id的所有评论链接接口
    public static Set<String> produceItemIdCommentLiks(String id){
        String productUrl = "http://club.Jd.com/productpage/p-"+id+"-s-0-t-3-p-0.html?callback=fetchJSON_comment98vv2956";
        String pageSource = Crawl4HttpClient.downLoadPage(productUrl);
        Pattern pattern = Pattern.compile("commentCount\":(.*?),\"");
        Matcher matcher = pattern.matcher(pageSource);
        int commentCount = 0;
        if(matcher.find()){
            System.out.println(matcher.group(1)+"===========");
            commentCount = Integer.valueOf(matcher.group(1));
        }

        Set<String> commentUrls = new HashSet<String>();

        for(int i = 0; i <= commentCount/10; i++){
            String tempCommentUrl = "http://club.Jd.com/productpage/p-"+id+"-s-0-t-3-p-"+String.valueOf(i)+".html?callback=fetchJSON_comment98vv2956";
            commentUrls.add(tempCommentUrl);
            if(i==100){
                break;
            }
        }

        return commentUrls;

    }

    //爬取前的准备工作
    public static void runJdPrepare(){
        RedisUtil re = new RedisUtil();
        Jedis redis = re.getJedisInstance();
        if(redis.exists("5_white_set")){
            redis.del("5_white_set");
            System.out.println("5_white_set delete.");
        }
        if(redis.exists("5_black_set")){
            redis.del("5_black_set");
            System.out.println("5_black_set delete.");
        }
        if(redis.exists("jd1")){
            redis.del("jd1");
            System.out.println("jd1 delete.");
        }
        if(redis.exists("jd2")){
            redis.del("jd2");
            System.out.println("jd2 delete.");
        }

        System.out.println(redis.hget("project_state","5-1"));
        redis.hset("project_state", "5-1", "die");
        System.out.println(redis.hget("project_state", "5-1"));
        redis.hset("project_executenum", "5-1", "0");
        redis.hset("project_cronstate", "5-1", "1");
        Set<String> allPages = redis.smembers("jd");
        Iterator it = allPages.iterator();
        while (it.hasNext()){
            String tempId = it.next().toString();
            System.out.println(tempId);
            redis.lpush("jd1", tempId);
        }

        redis.lpush("project_execute", "5-1");

    }

    public static void main(String[] args){
//        RedisUtil re = new RedisUtil();
//        Jedis redis = re.getJedisInstance();
//        Set<String> commentUrl = produceItemIdCommentLiks("1861111");
//        Iterator it = commentUrl.iterator();
//        while (it.hasNext()){
////            System.out.println(it.next().toString());
//            redis.sadd("Jd", it.next().toString());
//        }

        runJdPrepare();
    }

}
