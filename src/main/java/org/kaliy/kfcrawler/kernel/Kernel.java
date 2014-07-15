package org.kaliy.kfcrawler.kernel;

import org.kaliy.kfcrawler.crawler.BulkCrawler;
import org.kaliy.kfcrawler.data.FileCrawledDataExporter;
import org.kaliy.kfcrawler.data.FileCrawlingWebsitesImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Kernel {

    private final static Logger logger = LoggerFactory.getLogger(Kernel.class);

    public static void main(String... args) throws MalformedURLException {
        logger.info("KFCrawler is starting");
        List<URL> urlsToCrawl = new FileCrawlingWebsitesImporter().getWebsitesToCrawl();
        new FileCrawledDataExporter().export(new BulkCrawler(urlsToCrawl).crawl());
    }

}
