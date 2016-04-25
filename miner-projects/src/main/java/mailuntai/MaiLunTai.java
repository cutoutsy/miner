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
        Set<String> brandSet = new HashSet<String>();
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
    public static Set<String> saveAllSortsTireUrl(Set<String> brandSet){
        Set<String> allSortsUrls = new HashSet<String>();
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
        return allSortsUrls;
    }

    //得到麦轮胎所有的页面链接
    public static Set<String> allPageUrls(Set<String> allSortsUrls){
        Set<String> allPagesUrls = new HashSet<String>();
        Iterator allSorts = allSortsUrls.iterator();
        while (allSorts.hasNext()){
            String tempUrl = allSorts.next().toString();
            String tempSource = Crawl4HttpClient.downLoadPage(tempUrl);
            Document doc = Jsoup.parse(tempSource);
            Elements paperLinks = doc.getElementsByClass("pager").first().getElementsByTag("a");
            int maxnum = 1;
            RedisUtil re = new RedisUtil();
            Jedis redis = re.getJedisInstance();
            if(paperLinks.size() <= 2){
                if(paperLinks.size() != 0){
                    maxnum = paperLinks.size();
                }
            }else {
                String maxPage = paperLinks.get(paperLinks.size() - 2).select("a[href]").attr("href").toString();
                maxnum = Integer.valueOf(String.valueOf(maxPage.charAt(maxPage.length() - 1)));
            }

            for (int i = 1; i <= maxnum; i++){
                System.out.println(tempUrl+"&page="+i);
                allPagesUrls.add(tempUrl+"&page="+i);
                redis.sadd("mailuntaipages", tempUrl+"&page="+i);
            }
            try {
                Thread.sleep(2000);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return allPagesUrls;
    }


    //取得所有的轮胎id
    public static void getAllTiresId(){
        RedisUtil re = new RedisUtil();
        Jedis redis = re.getJedisInstance();
        Set<String> allPages = redis.smembers("mailuntaipages");
        Iterator it = allPages.iterator();
        while (it.hasNext()){
            String tempUrl = it.next().toString();
            String pageSource = Crawl4HttpClient.downLoadPage(tempUrl);
            Document doc = Jsoup.parse(pageSource);
            Elements onePageTires = doc.select(".products").select(".clearfix");
            for (Element oneTire : onePageTires){
                Element tirea = oneTire.getElementsByClass("pic").first();
                String href = tirea.select("a[href]").attr("href");
                redis.sadd("mailuntaiid", href.substring(9, 13));
            }
            try {
                Thread.sleep(1000);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
//        Set<String> allSortsUrls = new HashSet<String>();
//        allSortsUrls = saveAllSortsTireUrl(getTireBrandUrl());
//        allSortsUrls.add("http://mailuntai.cn/products/?type=2&auto_brand=&vehicle=&emission=&year=&style=&brand=287&order=sales&sort=desc&prop=1");
//        allPageUrls(allSortsUrls);
        getAllTiresId();
    }
}

