package org.kaliy.kfcrawler.html;

import org.junit.Before;
import org.junit.Test;
import org.kaliy.kfcrawler.data.Url;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LinkExtractorTest {

    private LinkExtractor linkExtractor;

    private Url test1, test2, test3;
    private Set<Url> threeUrlsSet;

    @Before
    public void setUp() {
        linkExtractor = new JSoupLinkExtractor();
        test1 = Url.fromString("http://test.ru/test/test");
        test2 = Url.fromString("http://test.ru/test/test2");
        test3 = Url.fromString("http://test.ru/test/test3");
        threeUrlsSet = new HashSet<>(Arrays.asList(test1, test2, test3));
    }

    @Test
    public void testLinkExtractorExtractsThreeLinksFromThreeAHtml() {
        String html = "<html><body><a href='http://test.ru/test/test'>test</a>" +
                "<a href='http://test.ru/test/test2'>test2</a>" +
                "<a href='http://test.ru/test/test3'>test3</a></html></body>";

        Collection<Url> urls = linkExtractor.extractFromHtml(html, "");

        assertEquals(threeUrlsSet, urls);
    }

    @Test
    public void testLinkExtractorExtractsThreeLinksFromThreeHtmlWithRealTags() {
        String html = "<html>" +
                "<head><title>test</title></head>" +
                "<body>" +
                "<ul>" +
                "<li><a href='http://test.ru/test/test'>test</a></li>" +
                "<li><a href='http://test.ru/test/test2'>test2</a></li>" +
                "<li><a href='http://test.ru/test/test3'>test3</a></li>" +
                "</body>" +
                "</html>";

        Collection<Url> urls = linkExtractor.extractFromHtml(html, "");

        assertEquals(threeUrlsSet, urls);
    }

    @Test
    public void testLinkExtractorExtractsLinksWithRelativePathFromRootIfBaseUrlIsPresent() {
        String html = "<a href='/test/test'>test</a>";

        Collection<Url> urls = linkExtractor.extractFromHtml(html, "http://test.ru/");

        assertEquals(new HashSet<>(Arrays.asList(test1)), urls);
    }

    @Test
    public void testLinkExtractorExtractsLinksWithRelativePathIfBaseUrlIsPresentAndHasTrailingSlash() {
        String html = "<a href='test'>test</a>";

        Collection<Url> urls = linkExtractor.extractFromHtml(html, "http://test.ru/test/");

        assertEquals(new HashSet<>(Arrays.asList(test1)), urls);
    }

    @Test
    public void testLinkExtractorExtractsLinksWithRelativePathIfBaseUrlIsPresentAndDoesntTrailingSlash() {
        String html = "<a href='test'>test</a>";

        Collection<Url> urls = linkExtractor.extractFromHtml(html, "http://test.ru/test/test123");

        assertEquals(new HashSet<>(Arrays.asList(test1)), urls);
    }

}
