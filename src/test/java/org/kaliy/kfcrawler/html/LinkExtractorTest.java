package org.kaliy.kfcrawler.html;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LinkExtractorTest {

    private LinkExtractor linkExtractor;

    private URL test1, test2, test3;
    private Set<URL> threeURLsSet;

    @Before
    public void setUp() throws MalformedURLException {
        linkExtractor = new JSoupLinkExtractor();
        test1 = new URL("http://test.ru/test/test");
        test2 = new URL("http://test.ru/test/test2");
        test3 = new URL("http://test.ru/test/test3");
        threeURLsSet = new HashSet<>(Arrays.asList(test1, test2, test3));
    }

    @Test
    public void testLinkExtractorExtractsThreeLinksFromThreeAHtml() throws MalformedURLException{
        String html = "<html><body><a href='http://test.ru/test/test'>test</a>" +
                "<a href='http://test.ru/test/test2'>test2</a>" +
                "<a href='http://test.ru/test/test3'>test3</a></html></body>";

        Collection<URL> urls = linkExtractor.extractFromHtml(html, new URL("http://test.ru"));

        assertEquals(threeURLsSet, urls);
    }

    @Test
    public void testLinkExtractorExtractsThreeLinksFromThreeHtmlWithRealTags() throws MalformedURLException{
        String html = "<html>" +
                "<head><title>test</title></head>" +
                "<body>" +
                "<ul>" +
                "<li><a href='http://test.ru/test/test'>test</a></li>" +
                "<li><a href='http://test.ru/test/test2'>test2</a></li>" +
                "<li><a href='http://test.ru/test/test3'>test3</a></li>" +
                "</body>" +
                "</html>";

        Collection<URL> urls = linkExtractor.extractFromHtml(html, new URL("http://test.ru"));

        assertEquals(threeURLsSet, urls);
    }

    @Test
    public void testLinkExtractorExtractsLinksWithRelativePathFromRootIfBaseURLIsPresent() throws MalformedURLException{
        String html = "<a href='/test/test'>test</a>";

        Collection<URL> urls = linkExtractor.extractFromHtml(html, new URL("http://test.ru/"));

        assertEquals(new HashSet<>(Arrays.asList(test1)), urls);
    }

    @Test
    public void testLinkExtractorExtractsLinksWithRelativePathIfBaseURLIsPresentAndHasTrailingSlash() throws MalformedURLException{
        String html = "<a href='test'>test</a>";

        Collection<URL> urls = linkExtractor.extractFromHtml(html, new URL("http://test.ru/test/"));

        assertEquals(new HashSet<>(Arrays.asList(test1)), urls);
    }

    @Test
    public void testLinkExtractorExtractsLinksWithRelativePathIfBaseURLIsPresentAndDoesntTrailingSlash() throws MalformedURLException{
        String html = "<a href='test'>test</a>";

        Collection<URL> urls = linkExtractor.extractFromHtml(html, new URL("http://test.ru/test/test123"));

        assertEquals(new HashSet<>(Arrays.asList(test1)), urls);
    }

}
