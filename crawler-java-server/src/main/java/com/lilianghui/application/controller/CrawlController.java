package com.lilianghui.application.controller;

import com.lilianghui.application.config.CrawlConfigProperties;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class CrawlController {

    @RequestMapping("/")
    public String index(@RequestBody CrawlConfigProperties crawlConfigProperties) throws Exception {
        PageFetcher pageFetcher = new PageFetcher(crawlConfigProperties);
        RobotstxtServer robotstxtServer = new RobotstxtServer(crawlConfigProperties.getRobotstxtConfig(), pageFetcher);
        edu.uci.ics.crawler4j.crawler.CrawlController crawlController = new edu.uci.ics.crawler4j.crawler.CrawlController(crawlConfigProperties, pageFetcher, robotstxtServer);
        if (ArrayUtils.isNotEmpty(crawlConfigProperties.getUrl())) {
            Arrays.asList(crawlConfigProperties.getUrl()).forEach(url -> {
                crawlController.addSeed(url);
            });
        }
        crawlController.start(crawlConfigProperties.getWebCrawler(), crawlConfigProperties.getNumberOfCrawlers());
        return "success";
    }

}
