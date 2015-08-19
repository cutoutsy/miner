package miner.topo;

import miner.parse.DocType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.UUID;

/**
 * Created by cutoutsy on 8/12/15.
 */
public class TestUuid {
//    private static MyLogger logger = new MyLogger(TestUuid.class);
    private static final Logger logger = LogManager.getLogger("TestUuid");
    public static void main(String[] args){
        PropertyConfigurator.configure("/home/cutoutsy/log4j.properties");
        logger.error("=============");
//        System.out.println("========================");
    }
}
