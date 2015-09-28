package miner.spider;


import miner.spider.utils.MySysLogger;
import miner.spider.utils.MysqlUtil;


/**
 * Created by cutoutsy on 15/9/28.
 */
public class Test2 {

    private static MySysLogger log = new MySysLogger(MysqlUtil.class);

//    public static ConfigurationSource source;
//    private static Logger log = LogManager.getLogger(Test2.class);
//    static {
//        try {
//            String config = System.getProperty("user.dir");
//            source = new ConfigurationSource(new FileInputStream(config + "/log4j2/log4j2_test.xml"));
//            Configurator.initialize(null, source);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }


//    private static Logger log = LogManager.getLogger(Test2.class);

    public static void main(String[] args){
        //log.trace("trace");
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error...");
        //log.fatal("fatal...");

        //log.exit();
    }
}
