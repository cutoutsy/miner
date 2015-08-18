package miner.spider.utils;

import org.apache.log4j.Logger;

/**
 * Log Class
 */

public class MyLogger {

    private Logger logger = null;

    public MyLogger(Class logClass) {
        this.logger = getLogger(logClass);
    }

    public Logger getLogger(Class logClass) {
        return Logger.getLogger(logClass);
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

}
