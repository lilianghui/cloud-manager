package com.lilianghui.application;

import com.alibaba.fescar.server.Server;
import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTransactionManagerServer
public class TxManagerApplication {

    public static void main(String[] args) throws Exception {
//        访问 http://127.0.0.1:7970/admin/index.html进入管理后台，默认密码时codingapi。指定密码
        SpringApplication.run(TxManagerApplication.class, args);

        Server.main(args);
    }

}
