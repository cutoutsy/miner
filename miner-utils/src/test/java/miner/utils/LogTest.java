package miner.utils;

import junit.framework.TestCase;

/**
 * 日志测试类
 */
public class LogTest extends TestCase{

    public void testWriteLog2TowLogServers(){
        MySysLogger logger = new MySysLogger(LogTest.class);
        StringBuffer sb = new StringBuffer();
        try{

            int k = 2/0;
        }catch (Exception ex){


            logger.error("error:"+MySysLogger.formatException(ex));
        }

    }

}
