package org.kaliy.kfcrawler.data;

import org.apache.http.client.fluent.Request;
import org.kaliy.kfcrawler.crawler.FetchResult;
import org.kaliy.kfcrawler.crawler.FetcherResultListener;
import org.kaliy.kfcrawler.util.PriorityCallable;
import org.kaliy.kfcrawler.util.PriorityTask;
import org.kaliy.kfcrawler.util.PriorityThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ApacheHttpClientFluentDataFetcher implements DataFetcher {
    private final static Logger logger = LoggerFactory.getLogger(ApacheHttpClientFluentDataFetcher.class);
    private final static ExecutorService executor = new PriorityThreadPoolExecutor(
            35,
            35,
            0L,
            TimeUnit.MILLISECONDS,
            new PriorityBlockingQueue<Runnable>());
    private Map<String, Long> fetchingTimesMap = Collections.synchronizedMap(new HashMap<String, Long>());//new ConcurrentHashMap<>();
    private Map<String, Object> locks = new ConcurrentHashMap<>();
    private Map<String, AtomicInteger> priorities = new ConcurrentHashMap<>();

    public ApacheHttpClientFluentDataFetcher() {
    }

    @Override
    public String getHtml(URL url) throws DataFetchingException {
        logger.debug("Fetching {}", url);
        try {
            return Request.Get(url.toURI())
                    .connectTimeout(30000)
                    .socketTimeout(30000)
                    .execute().returnContent().asString();
        } catch (Exception e) {
            throw new DataFetchingException(e);
        }
    }

    @Override
    public Future<FetchResult> addToFetchingQueue(final URL url, final FetcherResultListener listener) {
        if (null == priorities.get(url.getHost())) {
            priorities.put(url.getHost(), new AtomicInteger(100));
        }
        return executor.submit(new PriorityCallable<>(new Callable<FetchResult>() {
            @Override
            public FetchResult call() {
                try {
                    if (null == locks.get(url.getHost())) {
                        locks.put(url.getHost(), new Object());
                    }
                    Object lock = locks.get(url.getHost());
                    if (fetchingTimesMap.get(url.getHost()) != null) {
                        if (System.currentTimeMillis() - fetchingTimesMap.get(url.getHost()) < 1000) {
                            synchronized (lock) {
                                lock.wait();
                                while (System.currentTimeMillis() - fetchingTimesMap.get(url.getHost()) < 1000) {
                                    logger.debug("sleep {}", System.currentTimeMillis() - fetchingTimesMap.get(url.getHost()));
                                    Thread.sleep(1000 - (System.currentTimeMillis() - fetchingTimesMap.get(url.getHost())));
                                }
                            }
                        }
                    }
                    synchronized (lock) {
                        fetchingTimesMap.put(url.getHost(), System.currentTimeMillis());
                        lock.notify();
                    }
                    FetchResult result = new FetchResult(getHtml(url));
                    listener.onFetch(url, result);
                    return result;
                } catch (Exception e) {
                    logger.info("Unable to retrieve {}: {}", url, e);
                    listener.onFetch(url, FetchResult.FAILED);
                    return FetchResult.FAILED;
                }
            }
        }, priorities.get(url.getHost()).getAndDecrement()));
    }
}
