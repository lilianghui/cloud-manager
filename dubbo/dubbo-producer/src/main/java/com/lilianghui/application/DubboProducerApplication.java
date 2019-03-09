package com.lilianghui.application;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.alibaba.fescar.spring.annotation.GlobalTransactionScanner;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDubboConfiguration
@EnableDistributedTransaction
@MapperScan("com.lilianghui.application.mapper")
public class DubboProducerApplication {

    /**
     * init global transaction scanner
     *
     * @Return: GlobalTransactionScanner
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner(){
        return new GlobalTransactionScanner("order-gts-fescar-example", "my_test_tx_group");
    }


    public static void main(String[] args) {
        SpringApplication.run(DubboProducerApplication.class, args);
    }

}
