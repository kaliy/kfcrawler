package org.kaliy.kfcrawler.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kaliy.kfcrawler.data.Url;

import java.util.HashSet;
import java.util.Set;

public class JSoupLinkExtractor implements LinkExtractor {

    @Override
    public Set<Url> extractFromHtml(String html, String baseUrl) {
        Elements linkElements = Jsoup.parse(html, baseUrl).select("a[href]");
        Set<Url> urls = new HashSet<>();
        for(Element element: linkElements) {
            urls.add(Url.fromString(element.attr("abs:href")));
        }
        return urls;
    }
}
