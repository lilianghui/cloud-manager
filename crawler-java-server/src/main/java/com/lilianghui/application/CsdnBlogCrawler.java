package com.lilianghui.application;


import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class CsdnBlogCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches() && (href.startsWith("http://blog.csdn.net/bolg_hero"));
    }


    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            Document doc = Jsoup.parse(html);
            Elements ele = doc.select(".article-item-box.csdn-tracking-statistics");
            Iterator<Element> it = ele.iterator();
            while (it.hasNext()) {
                Element element = it.next();
                String a = element.select("h4 a").attr("href");
                String title = element.select("h4 a").text();
                String content = element.select("p.content a").text();
                String date = element.select("div.info-box p:eq(0)").text();
                Map<String, Object> map = new HashMap<>();
                map.put("a", a);
                map.put("title", title);
                map.put("content", content);
                map.put("date", date);
                System.out.println(map);
            }
        }
    }

}
