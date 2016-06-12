package miner.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.FileInputStream;

/**
 * Log Class
 *
 */

public class MySysLogger {
    private static ConfigurationSource source;
    private Logger logger = null;
    static {
        try {
            //source = new ConfigurationSource(new FileInputStream("/usr/local/storm/conf/log4j2.xml"));

            String config = System.getProperty("user.dir");
            source = new ConfigurationSource(new FileInputStream(config + "/log4j2/log4j2.xml"));

            Configurator.initialize(null, source);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public MySysLogger(Class logClass) {
        this.logger = LogManager.getLogger(logClass.getName());
    }

    private Logger getLogger(Class logClass) {
        return LogManager.getLogger(logClass.getName());
    }

    public void info(Object obj) {
        this.logger.info(obj);
    }

    public void warn(Object obj) {
        this.logger.warn(obj);
    }

    public void error(Object obj) {
        this.logger.error(obj);
    }

    public void debug(Object obj) {
        this.logger.debug(obj);
    }

    //格式化异常,以便在日志里面输入堆栈信息
    public static StringBuffer formatException(Exception ex){
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        for(int i = 0;  i < stackTrace.length; i++){
            sb.append(stackTrace[i].toString()+"\n\t\t");
        }

        return sb;
    }
}
