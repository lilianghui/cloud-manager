package com.lilianghui.hadoop;

import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.hadoop.fs.FsShell;

@SpringBootApplication
public class HadoopApplication implements CommandLineRunner {

    @Autowired
    private FsShell fsShell;  // 用于执行hdfs shell命令的对象

    @Override
    public void run(String... strings) throws Exception {
//        fsShell.mkdir("/spring-hadoop");
        // 查看根目录下的所有文件
        for (FileStatus fileStatus : fsShell.ls("/")) {
            System.out.println("> " + fileStatus.getPath());
        }
        fsShell.put("C:\\Users\\Administrator\\Desktop\\application.properties","hdfs://192.168.1.106:8020/spring-hadoop");
    }

    public static void main(String[] args) {
        SpringApplication.run(HadoopApplication.class, args);
    }

}
