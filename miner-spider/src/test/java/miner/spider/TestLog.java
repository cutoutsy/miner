package miner.spider;



import miner.spider.utils.MySysLogger;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;

/**
 * Created by cutoutsy on 8/18/15.
 */
public class TestLog {
//    private static MyLogger logger = new MyLogger(TestLog.class);
//    private static Logger LOG = MySysLogger.getInstance();
    private static MySysLogger  logger = new MySysLogger(TestLog.class);
    public static void main(String[] args){
        for(int i = 0; i < 100; i++) {
            logger.info("============");
            logger.error("iiiiiiiiiii");
        }
    }
}
