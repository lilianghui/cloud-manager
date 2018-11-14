package com.lilianghui.application.config;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = CrawlConfigProperties.PREFIX)
public class CrawlConfigProperties extends CrawlConfig {
    public static final String PREFIX = "crawl";

    private String[] url;
    private int numberOfCrawlers = 4;
    private Class<? extends WebCrawler> webCrawler;
    private RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
}
