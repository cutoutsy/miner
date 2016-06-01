package miner.utils;

import junit.framework.TestCase;

/**
 * 日志测试类
 */
public class LogTest extends TestCase{

    public void testWriteLog2TowLogServers(){
        MySysLogger logger = new MySysLogger(LogTest.class);

        MySysLogger1 logger1 = new MySysLogger1(LogTest.class);

        logger.info("11111111111111");
        logger1.warn("2222222222");
    }

}
