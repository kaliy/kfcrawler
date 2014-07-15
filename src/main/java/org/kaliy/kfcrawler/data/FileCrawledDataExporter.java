package org.kaliy.kfcrawler.data;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class FileCrawledDataExporter implements CrawledDataExporter {

    @Override
    public void export(URL site, Set<URL> data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (URL url: data) {
            stringBuilder.append(url.toString()).append("\n");
        }
        try {
            FileUtils.writeStringToFile(new File("data/" + site.getHost()), stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void export(Map<URL, Set<URL>> crawledData) {
        for(Map.Entry<URL, Set<URL>> siteData: crawledData.entrySet()) {
            export(siteData.getKey(), siteData.getValue());
        }
    }
}
