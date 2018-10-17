package com.lilianghui.framework.core.entity;


public abstract class Constant {
    // 日期格式
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATE_TIME_FORMAT_HOUR = "yyyy-MM-dd HH";
    public final static String DATE_TIME_FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_FORMAT_SLASH = "yyyy/MM/dd";
    public final static String DATE_TIME_FORMAT_SLASH_SECOND = "yyyy/MM/dd HH/mm/ss";
    public final static String[] DATE_FORMAT_ARRAY = {Constant.DATE_FORMAT, Constant.DATE_TIME_FORMAT, Constant.DATE_TIME_FORMAT_HOUR, Constant.DATE_TIME_FORMAT_MINUTE, DATE_TIME_FORMAT_SLASH, DATE_TIME_FORMAT_SLASH_SECOND};

    public final static String TOKEN = "token";
    public final static String CAPTCHA = "captcha";
    public final static String BILL = "bill";
    public final static String ERROR_ATTRIBUTE = "error";

    public final static String WE_CHAT_SESSION = "WE_CHAT_SESSION";

    public static final String KEY_PAIR = "KEY_PAIR";

    public static final String UPLOAD_PATH = "upload.path";
    public static final String UPLOAD_RENAME = "upload.rename";

}
