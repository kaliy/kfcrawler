package org.kaliy.kfcrawler.html;


import java.net.URL;
import java.util.Collection;
import java.util.Set;

public interface LinkExtractor {
    Set<URL> extractFromHtml(String html, URL baseUrl);
}
