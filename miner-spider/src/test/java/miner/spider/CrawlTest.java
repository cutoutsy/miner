package miner.spider;

import junit.framework.TestCase;
import miner.spider.httpclient.Crawl4HttpClient;

/**
 * 测试Httpclient网页下载
 */
public class CrawlTest extends TestCase{

    public void testDownLoadPage(){
        String url = "https://account.wandoujia.com/v4/api/simple/profile?uid=87";
        String proxy = "101.96.10.32:94";
        String re = "";
        try {

            re = Crawl4HttpClient.downLoadPage(url, proxy);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        if(re == null){
            System.out.println("请求返回为空");
        }else if(re.equals("")){
            System.out.println("请求不成功");
        }
    }
}
