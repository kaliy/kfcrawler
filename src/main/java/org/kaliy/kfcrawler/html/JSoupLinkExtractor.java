package org.kaliy.kfcrawler.html;

import com.google.common.base.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

public class JSoupLinkExtractor implements LinkExtractor {

    private final static Logger logger = LoggerFactory.getLogger(JSoupLinkExtractor.class);

    @Override
    public Set<URL> extractFromHtml(String html, URL baseUrl) {
        Document document =  Jsoup.parse(html, baseUrl.toString());
        document.outputSettings().escapeMode(Entities.EscapeMode.extended);
        Elements linkElements = document.select("a[href]");
        Set<URL> urls = new HashSet<>();
        for(Element element: linkElements) {
            String urlString = element.attr("abs:href");
            if (!Strings.isNullOrEmpty(urlString)) {
                try {
                    URL url = new URL(urlString);
                    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
                    url = uri.toURL();
                    urls.add(url);
                } catch (Exception e) {
                    logger.warn("Malformed URL: \"{}\", skipping", urlString);
                }
            }
        }
        return urls;
    }
}
