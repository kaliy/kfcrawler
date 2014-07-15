package org.kaliy.kfcrawler.data;

import org.kaliy.kfcrawler.crawler.FetchResult;
import org.kaliy.kfcrawler.crawler.FetcherResultListener;

import java.net.URL;
import java.util.concurrent.Future;

public interface DataFetcher {

    String getHtml(URL url) throws DataFetchingException;

    Future<FetchResult> addToFetchingQueue(URL url, FetcherResultListener listener);

}
