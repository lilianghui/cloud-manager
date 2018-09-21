package com.lilianghui.framework.core.logging;

public abstract class Log {

    public Log(String logger) {
    }

    public abstract boolean isDebugEnabled();

    public abstract boolean isTraceEnabled();

    public abstract void trace(String s);

    public abstract void trace(String s, Object... params);

    public abstract void debug(String s);

    public abstract void debug(String s, Object... params);

    public abstract void warn(String s);

    public abstract void warn(String s, Object... params);

    public abstract void info(String s);

    public abstract void info(String s, Object... params);

    public abstract void error(String s, Throwable e);

    public abstract void error(String s);

    public abstract void error(String s, Object... params);

}
