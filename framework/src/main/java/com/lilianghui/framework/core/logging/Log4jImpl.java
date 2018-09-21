package com.lilianghui.framework.core.logging;

import org.apache.log4j.Logger;

public class Log4jImpl extends Log {
    private Logger logger;

    public Log4jImpl(String clazz) {
        super(clazz);
        logger = Logger.getLogger(clazz);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        logger.error(s, e);
    }

    @Override
    public void error(String s) {
        logger.error(s);
    }

    @Override
    public void debug(String s) {
        logger.debug(s);
    }

    @Override
    public void trace(String s) {
        logger.trace(s);
    }

    @Override
    public void warn(String s) {
        logger.warn(s);
    }

    @Override
    public void info(String s) {
        logger.info(s);
    }

    @Override
    public void trace(String s, Object... params) {
        logger.trace(writeText(s, params));
    }

    @Override
    public void debug(String s, Object... params) {
        logger.debug(writeText(s, params));
    }

    @Override
    public void warn(String s, Object... params) {
        logger.warn(writeText(s, params));

    }

    @Override
    public void info(String s, Object... params) {
        logger.info(writeText(s, params));
    }

    @Override
    public void error(String s, Object... params) {
        logger.error(writeText(s, params));
    }

    private String writeText(String s, Object... params) {
        if (s == null) {
            return null;
        }
        s = s.replace("{}", "%s");
        return String.format(s, params);
    }
}
