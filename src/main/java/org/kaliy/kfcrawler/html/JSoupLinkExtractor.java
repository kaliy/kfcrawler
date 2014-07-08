package org.kaliy.kfcrawler.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class JSoupLinkExtractor implements LinkExtractor {

    private final static Logger logger = LoggerFactory.getLogger(JSoupLinkExtractor.class);

    @Override
    public Set<URL> extractFromHtml(String html, URL baseUrl) {
        Elements linkElements = Jsoup.parse(html, baseUrl.toString()).select("a[href]");
        Set<URL> urls = new HashSet<>();
        for(Element element: linkElements) {
            String url = element.attr("abs:href");
            try {
                urls.add(new URL(url));
            } catch (MalformedURLException e) {
                logger.warn("Malformed URL: \"{}\", skipping", url);
            }
        }
        return urls;
    }
}
