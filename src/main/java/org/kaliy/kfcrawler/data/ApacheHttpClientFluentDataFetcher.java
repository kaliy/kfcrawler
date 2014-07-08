package org.kaliy.kfcrawler.data;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class ApacheHttpClientFluentDataFetcher implements DataFetcher {
    private final static Logger logger = LoggerFactory.getLogger(ApacheHttpClientFluentDataFetcher.class);
    @Override
    public String getHtml(URL url) throws DataFetchingException {
        logger.debug("Fetching {}", url);
        try {
            return Request.Get(url.toURI())
                    .connectTimeout(1000)
                    .socketTimeout(1000)
                    .execute().returnContent().asString();
        } catch (Exception e) {
            throw new DataFetchingException(e);
        }
    }
}
