package org.kaliy.kfcrawler.data;

import org.apache.http.client.fluent.Request;

public class ApacheHttpClientFluentDataFetcher implements DataFetcher {
    @Override
    public String getHtml(String url) throws DataFetchingException {
        try {
            return Request.Get(url)
                    .connectTimeout(1000)
                    .socketTimeout(1000)
                    .execute().returnContent().asString();
        } catch (Exception e) {
            throw new DataFetchingException(e);
        }
    }
}
