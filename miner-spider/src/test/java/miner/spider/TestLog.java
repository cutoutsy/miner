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
        String kk = "html0.body0.div8.div0.div0.ul0.li_0_9_.dl0.dt0.a0:title";

        System.out.println(kk.split(":")[0]+"===="+kk.split(":")[1]);
    }
}
