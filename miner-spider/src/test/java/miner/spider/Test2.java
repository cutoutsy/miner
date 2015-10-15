package miner.spider;


import miner.utils.MySysLogger;

/**
 * Created by cutoutsy on 15/9/28.
 */
public class Test2 {

    private static MySysLogger log = new MySysLogger(Test2.class);

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
        try {
            while (true) {
                Thread.sleep(20);
                log.debug("debug---2015-09-29 13:42:01 Logger=miner.spider.utils.FileOperatorUtil Level=INFO Message=create single file /Users/cutoutsy/miner-2015-09-29.logsucceed!");
                log.info("info--2015-09-29 13:42:01 Logger=miner.spider.utils.FileOperatorUtil Level=INFO Message=create single file /Users/cutoutsy/miner-2015-09-29.logsucceed!");
                log.warn("warn--2015-09-29 13:42:01 Logger=miner.spider.utils.FileOperatorUtil Level=INFO Message=create single file /Users/cutoutsy/miner-2015-09-29.logsucceed!");
                log.error("error--2015-09-29 13:42:01 Logger=miner.spider.utils.FileOperatorUtil Level=INFO Message=create single file /Users/cutoutsy/miner-2015-09-29.logsucceed!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        System.out.println(new Date());


    }
}
