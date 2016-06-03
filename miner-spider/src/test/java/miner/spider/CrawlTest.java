package miner.spider;

import junit.framework.TestCase;
import miner.spider.httpclient.Crawl4HttpClient;

/**
 * 测试Httpclient网页下载
 */
public class CrawlTest extends TestCase{

    public void testDownLoadPage(){
        String url = "https://account.wandoujia.com/v4/api/simple/profile?uid=87";
        String proxy = "120.52.73.142:8090";
        String re = "";
        try {

            re = Crawl4HttpClient.downLoadPage(url, proxy);
        }catch (Exception ex){
            ex.printStackTrace();

        }

        System.out.println(re+"==");

    }
}
