package org.kaliy.kfcrawler.data;

public interface DataFetcher {

    String getHtml(String url) throws DataFetchingException;

}
