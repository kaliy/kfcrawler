package org.kaliy.kfcrawler.crawler;

import org.kaliy.kfcrawler.data.ApacheHttpClientFluentDataFetcher;
import org.kaliy.kfcrawler.data.DataFetcher;
import org.kaliy.kfcrawler.html.JSoupLinkExtractor;
import org.kaliy.kfcrawler.html.LinkExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebsiteCrawler implements FetcherResultListener {

    private final static Logger logger = LoggerFactory.getLogger(WebsiteCrawler.class);

    private URL website;
    private DataFetcher dataFetcher;
    private LinkExtractor linkExtractor;
    private Set<URL> visitedURLs = new CopyOnWriteArraySet<>();
    private final Object monitor = new Object();
    private Set<URL> urls = new CopyOnWriteArraySet<>();
    private Set<URL> currentlyFetchingUrls = new CopyOnWriteArraySet<>();

    private boolean shouldStop = false;

    public WebsiteCrawler(URL website) {
        this.website = website;
        dataFetcher = new ApacheHttpClientFluentDataFetcher();
        linkExtractor = new JSoupLinkExtractor();
    }

    private void addUrlToFetching(URL url) {
        logger.debug("Adding to fetching queue {}", url);
        dataFetcher.addToFetchingQueue(url, this);
        visitedURLs.add(url);
        currentlyFetchingUrls.add(url);
    }

    public Set<URL> crawlWebsite() {
        addUrlToFetching(website);
        synchronized (monitor) {
            while (!currentlyFetchingUrls.isEmpty()) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return urls;
    }

    @Override
    public void onFetch(URL url, FetchResult result) {
        if (result.isSuccessful()) {
            logger.debug("Successfully fetched {}, processing {} bytes", url, result.getHtml().getBytes().length);
            Set<URL> extractedUrls = linkExtractor.extractFromHtml(result.getHtml(), url);
            for (URL urlFromPage : extractedUrls) {
                if (urlFromPage.getHost().equals(website.getHost())) {
                    urls.add(urlFromPage);
                    if (shouldCrawl(urlFromPage)) {
                        addUrlToFetching(urlFromPage);
                    }
                }
            }
        } else {
            logger.debug("ololo fail {}", url);
        }
        currentlyFetchingUrls.remove(url);
        synchronized (monitor) {
            monitor.notifyAll();
        }
    }

    private boolean shouldCrawl(URL url) {
        return isMoreURLsToCrawlNeeded() && !visitedURLs.contains(url) && !currentlyFetchingUrls.contains(url);
    }

    private boolean isURLBelongsToWebsite(URL url) {
        return url.getHost().equalsIgnoreCase(website.getHost());
    }

    private boolean isMoreURLsToCrawlNeeded() {
        return visitedURLs.size()  < 100;
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
