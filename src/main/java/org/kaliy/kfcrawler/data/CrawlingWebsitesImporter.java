package org.kaliy.kfcrawler.data;

import java.net.URL;
import java.util.List;

public interface CrawlingWebsitesImporter {

    List<URL> getWebsitesToCrawl() throws DataImportException;

}
