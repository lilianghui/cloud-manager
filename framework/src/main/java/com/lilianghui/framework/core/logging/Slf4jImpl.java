package com.lilianghui.framework.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jImpl extends Log{
    private Logger logger;

    public Slf4jImpl(String clazz) {
        super(clazz);
        logger = LoggerFactory.getLogger(clazz);
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
        logger.trace(s, params);
    }

    @Override
    public void debug(String s, Object... params) {
        logger.debug(s, params);
    }

    @Override
    public void warn(String s, Object... params) {
        logger.warn(s, params);
    }

    @Override
    public void info(String s, Object... params) {
        logger.info(s, params);
    }

    @Override
    public void error(String s, Object... params) {
        logger.error(s, params);
    }
}
