package org.kaliy.kfcrawler.data;

import java.net.URL;

public interface DataFetcher {

    String getHtml(URL url) throws DataFetchingException;

}
