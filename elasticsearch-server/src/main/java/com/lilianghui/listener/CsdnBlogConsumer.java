package com.lilianghui.listener;

import com.lilianghui.entity.CsdnBlog;
import com.lilianghui.repositories.CsdnBlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.starter.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.starter.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(topic = "csdnBlog", consumerGroup = "${spring.application.name}-message-ext-consumer")
public class CsdnBlogConsumer implements RocketMQListener<CsdnBlog> {

    @Resource
    private CsdnBlogRepository csdnBlogRepository;

    @Override
    public void onMessage(CsdnBlog message) {
        csdnBlogRepository.save(message);
    }

}