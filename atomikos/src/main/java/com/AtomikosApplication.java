package com;

import com.lilianghui.spring.starter.MultiDataSourceAutoConfiguration;
import com.lilianghui.spring.starter.MybatisExAutoConfiguration;
import com.lilianghui.spring.starter.OpenReplicatorAutoConfiguration;
import com.lilianghui.spring.starter.RedissonAutoConfiguration;
import com.lilianghui.spring.starter.netty.rpc.server.NettyRpcServerRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import(MultiDataSourceAutoConfiguration.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MybatisExAutoConfiguration.class,
        RedissonAutoConfiguration.class, OpenReplicatorAutoConfiguration.class, NettyRpcServerRegistrar.class})
public class AtomikosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtomikosApplication.class, args);
    }

}
