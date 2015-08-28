package miner.topo;

import miner.parse.DocType;
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
public class TestUuid {
//    private static MyLogger logger = new MyLogger(TestUuid.class);
    private static final Logger logger = LogManager.getLogger("TestUuid");

    public static void main(String[] args){
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();
            QuartzManager _qManager = new QuartzManager();
            _qManager.setScheduler(scheduler);
            PlatformUtils.initRegisterProject(_qManager);
            scheduler.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
