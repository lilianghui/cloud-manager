package com.lilianghui.application;


import com.google.common.collect.Lists;
import com.lilianghui.entity.CsdnBlog;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class CsdnBlogCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");


    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        System.err.println("------------>" + href);
        return !FILTERS.matcher(href).matches() && (href.startsWith("https://blog.csdn.net") || href.startsWith("http://blog.csdn.net"));
    }


    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.err.println(url);
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            Document doc = Jsoup.parse(html);
            Elements ele = doc.select(".article-item-box.csdn-tracking-statistics");
            Iterator<Element> it = ele.iterator();
//            List<CsdnBlog> csdnBlogs = Lists.newArrayList();
            while (it.hasNext()) {
                Element element = it.next();
                String articleid = element.attr("data-articleid");
                String a = element.select("h4 a").attr("href");
                String title = element.select("h4 a").text();
                String content = element.select("p.content a").text();
                String date = element.select("div.info-box p:eq(0)").text();
                Map<String, Object> map = new HashMap<>();
                map.put("articleid", articleid);
                map.put("a", a);
                map.put("title", title);
                map.put("content", content);
                map.put("date", date);
                System.out.println(map);
                try {
                    CsdnBlog csdnBlog = new CsdnBlog(Long.valueOf(articleid), a, title, content, DateUtils.parseDate(date, "yyyy-MM-dd HH:mm:ss"));
//                    csdnBlogs.add(csdnBlog);
                    rocketMQTemplate.syncSend("csdnBlog", csdnBlog);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
//            if (CollectionUtils.isNotEmpty(csdnBlogs)) {
//                rocketMQTemplate.syncSend("csdnBlogs", csdnBlogs);
//            }
        }
    }

}
