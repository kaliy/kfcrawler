package org.kaliy.kfcrawler.data;

import java.net.URL;
import java.util.Map;
import java.util.Set;

public interface CrawledDataExporter {
    void export(URL site, Set<URL> data);

    void export(Map<URL, Set<URL>> crawledData);
}
