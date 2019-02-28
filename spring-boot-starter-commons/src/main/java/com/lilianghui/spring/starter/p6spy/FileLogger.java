package com.lilianghui.spring.starter.p6spy;

import com.p6spy.engine.spy.P6SpyOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/21 0021.
 */
@Slf4j
public class FileLogger extends com.p6spy.engine.spy.appender.FileLogger {

    private String fileName = null;
    private String originalFileName = null;
    private PrintStream printStream = null;
    private long currentTime = 0;

    private void init() {
        if (originalFileName == null) {
            throw new IllegalStateException("setLogfile() must be called before init()");
        }
        try {
            currentTime = DateUtils.truncate(new Date(), Calendar.DATE).getTime();
            int index = originalFileName.lastIndexOf(".");
            if (index >= 0) {
                fileName = originalFileName.substring(0, index) + "-" + DateFormatUtils.format(new Date(), "yyyyMMdd") + originalFileName.substring(index);
            }
            int pathIndex = originalFileName.lastIndexOf("/");
            if (pathIndex > 0) {
                String path = originalFileName.substring(0, pathIndex);
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            printStream = new PrintStream(new FileOutputStream(fileName, P6SpyOptions.getActiveInstance().getAppend()));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    protected PrintStream getStream() {
        // Lazy init to allow for the appender to be changed at Runtime without creating an empty log file (assuming
        // that no log message has been written yet)
        long time = DateUtils.truncate(new Date(), Calendar.DATE).getTime();
        if (printStream == null || currentTime < time) {
            synchronized (this) {
                if (printStream == null || currentTime < time) {
                    init();
                }
            }
        }
        return printStream;
    }

    @Override
    public void setLogfile(String fileName) {
//        String file = BasicPropertyPlaceholderConfigurer.getStringProperty(fileName);
//        originalFileName = StringUtils.isNotBlank(file) ? file : fileName;
        this.originalFileName = fileName;
        this.fileName = fileName;
    }

    @Override
    public void logText(String text) {
        try {
            if (StringUtils.isNotBlank(text)) {
                System.out.println(text);
                getStream().println(text);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
