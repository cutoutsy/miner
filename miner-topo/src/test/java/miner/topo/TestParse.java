package miner.topo;

import miner.parse.DocObject;
import miner.parse.DocType;
import miner.spider.httpclient.Crawl4HttpClient;
import miner.topo.platform.PlatformUtils;
import miner.topo.platform.QuartzManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import java.util.UUID;

/**
 * Created by cutoutsy on 8/12/15.
 */
public class TestParse {

//    private static MyLogger logger = new MyLogger(TestParse.class);

    public static void main(String[] args){
        String doc_str = Crawl4HttpClient.downLoadPage("http://dealer.xcar.com.cn/d1_475/?type=1&page=1");
        DocObject.testParse(doc_str);
    }
}
