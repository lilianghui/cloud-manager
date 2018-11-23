package com.lilianghui.application.config;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@Configuration
@EnableConfigurationProperties(CrawlConfigProperties.class)
public class CrawlAutoConfiguration {
    @Resource
    private CrawlConfigProperties crawlConfigProperties;
    @Resource
    private ApplicationContext applicationContext;

    @Bean
    public CrawlController crawlController() throws Exception {
        PageFetcher pageFetcher = new PageFetcher(crawlConfigProperties);
        RobotstxtServer robotstxtServer = new RobotstxtServer(crawlConfigProperties.getRobotstxtConfig(), pageFetcher);
        CrawlController crawlController = new CrawlController(crawlConfigProperties, pageFetcher, robotstxtServer);
        if (ArrayUtils.isNotEmpty(crawlConfigProperties.getUrl())) {
            Arrays.asList(crawlConfigProperties.getUrl()).forEach(url -> {
                crawlController.addSeed(url);
            });
        }
        crawlController.start(new CrawlController.WebCrawlerFactory() {

            @Override
            public WebCrawler newInstance() throws Exception {
                WebCrawler webCrawler = null;
                try {
                    webCrawler = applicationContext.getBean(crawlConfigProperties.getWebCrawler());
                } catch (NoSuchBeanDefinitionException e) {
                    try {
                        webCrawler = (WebCrawler) applicationContext.getBean(crawlConfigProperties.getWebCrawler().getSimpleName());
                    } catch (NoSuchBeanDefinitionException e1) {
                        if (webCrawler == null) {
                            webCrawler = registerBeanDefinition(applicationContext, crawlConfigProperties.getWebCrawler());
                        }
                    }
                }
                return webCrawler;
            }
        }, crawlConfigProperties.getNumberOfCrawlers());
        return crawlController;
    }

    private <T> T registerBeanDefinition(ApplicationContext applicationContext, Class<T> clazz) {
        String name = clazz.getSimpleName();
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
        return (T) beanFactory.getBean(name);
    }
}
