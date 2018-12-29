package com.lilianghui.spring.starter.annotation;

import com.lilianghui.spring.starter.netty.rpc.server.NettyRpcClientsRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({NettyRpcClientsRegistrar.class})
public @interface EnableNettyRpcClients {
    String[] basePackages() default {};
}
