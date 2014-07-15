package org.kaliy.kfcrawler.data;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FileCrawlingWebsitesImporter implements CrawlingWebsitesImporter {

    private final static Logger logger = LoggerFactory.getLogger(FileCrawlingWebsitesImporter.class);

    @Override
    public List<URL> getWebsitesToCrawl() throws DataImportException {
        File websites = new File("websites");
        try {
            List<String> urlStrings = FileUtils.readLines(websites);
            return Lists.newArrayList(FluentIterable
                    .from(urlStrings)
                    .transform(new Function<String, URL>() {
                        @Override
                        public URL apply(String input) {
                            try {
                                return new URL(input);
                            } catch (MalformedURLException e) {
                                logger.warn("Can't parse {} as URL, skipping", input);
                                return null;
                            }
                        }
                    }).filter(Predicates.notNull())
                    .toList());
        } catch (IOException e) {
            logger.error("Can't read file {}", websites.getAbsolutePath());
            throw new DataImportException(e);
        }
    }
}
