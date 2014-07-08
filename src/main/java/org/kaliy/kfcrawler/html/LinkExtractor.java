package org.kaliy.kfcrawler.html;

import org.kaliy.kfcrawler.data.Url;

import java.util.Collection;
import java.util.Set;

public interface LinkExtractor {
    Set<Url> extractFromHtml(String html, String baseUrl);
}
