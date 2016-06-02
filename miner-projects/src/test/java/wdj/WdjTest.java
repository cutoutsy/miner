package wdj;

import junit.framework.TestCase;
import miner.spider.httpclient.Crawl4HttpClient;

/**
 * 关于豌豆荚的测试
 */
public class WdjTest extends TestCase{

    public void testFullText(){
        String url = "https://account.wandoujia.com/v4/api/simple/profile?uid=87";
        String re = "";
        try {

            re = Crawl4HttpClient.downLoadPage(url);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
