package com.lilianghui.application.config;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
@EnableConfigurationProperties(CrawlConfigProperties.class)
public class CrawlAutoConfiguration {
    @Resource
    private CrawlConfigProperties crawlConfigProperties;

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
        crawlController.start(crawlConfigProperties.getWebCrawler(), crawlConfigProperties.getNumberOfCrawlers());
        return crawlController;
    }

}
