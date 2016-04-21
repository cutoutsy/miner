package mailuntai;

import miner.spider.httpclient.Crawl4HttpClient;
import miner.utils.RedisUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * MaiLuntai class
 * User: cutoutsy
 * Date: 4/21/2016
 */
public class MaiLunTai {

    //获得麦轮胎所有品牌的id
    public static Set<String> getTireBrandUrl(){
        Set<String> brandSet = new HashSet<>();
        String productUrl = "http://mailuntai.cn/products/";
        String pageSource = Crawl4HttpClient.downLoadPage(productUrl);
        Document doc = Jsoup.parse(pageSource);
        Element productsFilter = doc.getElementById("products-filter");
        Elements brandlis = productsFilter.getElementsByTag("li");
        for(Element brand : brandlis){
            brandSet.add("http://mailuntai.cn"+brand.select("a[href]").attr("href"));
        }
        return brandSet;
    }

    //存储麦轮胎所有品牌下五种属性的所有url, redis里数据库为mailuntai
    public static void saveAllSortsTireUrl(Set<String> brandSet){
        Set<String> allSortsUrls = new HashSet<>();
        Iterator it = brandSet.iterator();
        while (it.hasNext()){
            String tempUrl = it.next().toString();
            for(int i = 1; i < 6; i++){
                allSortsUrls.add(tempUrl+"&order=sales&sort=desc&prop="+i);
            }
        }

        RedisUtil re = new RedisUtil();
        Jedis redis = re.getJedisInstance();

        Iterator allSorts = allSortsUrls.iterator();
        while (allSorts.hasNext()){
            redis.sadd("mailuntai", allSorts.next().toString());
        }
        System.out.println("save success");
    }

    public static void main(String[] args){
        saveAllSortsTireUrl(getTireBrandUrl());
    }
}

