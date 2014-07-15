package org.kaliy.kfcrawler.crawler;

import java.net.URL;

public interface FetcherResultListener {
    public void onFetch(URL url, FetchResult result);
}
