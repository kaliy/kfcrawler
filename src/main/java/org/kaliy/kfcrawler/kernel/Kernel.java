package org.kaliy.kfcrawler.kernel;

import org.kaliy.kfcrawler.crawler.WebsiteCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class Kernel {

    private final static Logger logger = LoggerFactory.getLogger(Kernel.class);

    public static void main(String... args) throws MalformedURLException {
        logger.info("KFCrawler is starting");
        logger.info("LOL {}", new WebsiteCrawler(new URL("http://lenta.ru")).crawlWebsite());
    }

}
