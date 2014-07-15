package org.kaliy.kfcrawler.crawler;

import com.google.common.util.concurrent.*;
import org.kaliy.kfcrawler.data.FileCrawledDataExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class BulkCrawler {

    private final static Logger logger = LoggerFactory.getLogger(BulkCrawler.class);

    ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(100));
    private Collection<URL> websitesToFetch;

    public BulkCrawler(Collection<URL> websitesToFetch) {
        this.websitesToFetch = websitesToFetch;
    }

    public Map<URL, Set<URL>> crawl() {
        final Map<URL, Set<URL>> crawlResult = new ConcurrentHashMap<>();
        final CountDownLatch latch = new CountDownLatch(websitesToFetch.size());
        for(final URL website: websitesToFetch) {
            ListenableFuture<Set<URL>> fetchFuture = service.submit(new Callable<Set<URL>>() {
                @Override
                public Set<URL> call() throws Exception {
                    logger.info("Starting crawling {}", website);
                    Set<URL> urls =  new WebsiteCrawler(website).crawlWebsite();
                    latch.countDown();
                    logger.info("Finished crawling {}", website);
                    return urls;
                }
            });
            Futures.addCallback(fetchFuture, new FutureCallback<Set<URL>>() {
                @Override
                public void onSuccess(Set<URL> result) {
                    crawlResult.put(website, result);
                }

                @Override
                public void onFailure(Throwable t) {
                    latch.countDown();
                    logger.error("azaza", t);
                }
            });
        }
        try {
            latch.await();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return crawlResult;
    }

}