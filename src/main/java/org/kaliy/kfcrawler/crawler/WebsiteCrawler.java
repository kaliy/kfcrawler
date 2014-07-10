package org.kaliy.kfcrawler.crawler;

import org.kaliy.kfcrawler.data.ApacheHttpClientFluentDataFetcher;
import org.kaliy.kfcrawler.data.DataFetcher;
import org.kaliy.kfcrawler.data.DataFetchingException;
import org.kaliy.kfcrawler.html.JSoupLinkExtractor;
import org.kaliy.kfcrawler.html.LinkExtractor;
import org.kaliy.kfcrawler.util.LinkedHashSetQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class WebsiteCrawler {

    private final static Logger logger = LoggerFactory.getLogger(WebsiteCrawler.class);

    private URL website;
    private DataFetcher dataFetcher;
    private LinkExtractor linkExtractor;
    private Set<URL> visitedURLs = new HashSet<>();
    private Queue<URL> urlsToVisit = new LinkedHashSetQueue<>();
    private Set<URL> urls = new HashSet<>();

    public WebsiteCrawler(URL website) {
        this.website = website;
        dataFetcher = new ApacheHttpClientFluentDataFetcher();
        linkExtractor = new JSoupLinkExtractor();
    }

    public Set<URL> crawlWebsite() {
        urls.add(website);
        urlsToVisit.add(website);
        int index = 1;
        while (!urlsToVisit.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //TODO: dump info on interruption
                e.printStackTrace();
            }
            URL url = urlsToVisit.poll();

            try {
                String html = dataFetcher.getHtml(url);
                Set<URL> fetchedURLs = linkExtractor.extractFromHtml(html, url);
                for(URL fetchedURL: fetchedURLs) {
                    if (isMoreURLsToCrawlNeeded() &&
                            !visitedURLs.contains(fetchedURL) &&
                            isURLBelongsToWebsite(fetchedURL)) {
                        urlsToVisit.add(fetchedURL);
                        urls.add(fetchedURL);
                    }
                }
                visitedURLs.add(url);
                logger.debug("Processed {}/{}", index++, 100);
            } catch (DataFetchingException e) {
                logger.warn("Failed to retrieve page {}", url, e.getMessage());
            }
        }
        return urls;
    }

    private boolean isURLBelongsToWebsite(URL url) {
        return url.getHost().equalsIgnoreCase(website.getHost());
    }

    private boolean isMoreURLsToCrawlNeeded() {
        return visitedURLs.size() + urlsToVisit.size() < 100;
    }

    //<editor-fold desc="Getters and setters">
    public DataFetcher getDataFetcher() {
        return dataFetcher;
    }

    public WebsiteCrawler setDataFetcher(DataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
        return this;
    }

    public LinkExtractor getLinkExtractor() {
        return linkExtractor;
    }

    public WebsiteCrawler setLinkExtractor(LinkExtractor linkExtractor) {
        this.linkExtractor = linkExtractor;
        return this;
    }
    //</editor-fold>
}
