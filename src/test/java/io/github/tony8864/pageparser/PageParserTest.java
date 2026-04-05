package io.github.tony8864.pageparser;

import io.github.tony8864.pagefetcher.FetchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageParserTest {

    private final PageParser pageParser = new PageParser();

    @Test
    void shouldParseSuccessfulFetchResult() {
        String url = "https://example.com";

        String html = """
                <html>
                    <head>
                        <title>Example Page</title>
                        <meta name="description" content="This is a test description">
                    </head>
                    <body>
                        <h1>Hello World</h1>
                        <p>This is the page content.</p>
                        <a href="https://example.com/about">About</a>
                        <a href="/contact">Contact</a>
                    </body>
                </html>
                """;

        Document document = Jsoup.parse(html, url);
        FetchResult fetchResult = FetchResult.success(url, document);

        ParsedPage parsedPage = pageParser.parse(fetchResult);

        assertEquals("Example Page", parsedPage.title());
        assertEquals("This is a test description", parsedPage.description());
        assertTrue(parsedPage.bodyText().contains("Hello World"));
        assertTrue(parsedPage.bodyText().contains("This is the page content."));

        assertEquals(
                List.of("https://example.com/about", "https://example.com/contact"),
                parsedPage.urls()
        );
    }

    @Test
    void shouldReturnEmptyDescriptionWhenMetaDescriptionIsMissing() {
        String url = "https://example.com";

        String html = """
                <html>
                    <head>
                        <title>No Description Page</title>
                    </head>
                    <body>
                        <p>Some content here.</p>
                    </body>
                </html>
                """;

        Document document = Jsoup.parse(html, url);
        FetchResult fetchResult = FetchResult.success(url, document);

        ParsedPage parsedPage = pageParser.parse(fetchResult);

        assertEquals("No Description Page", parsedPage.title());
        assertEquals("", parsedPage.description());
        assertTrue(parsedPage.bodyText().contains("Some content here."));
        assertEquals(List.of(), parsedPage.urls());
    }

    @Test
    void shouldThrowExceptionWhenFetchResultIsNotSuccessful() {
        String url = "https://example.com";
        FetchResult failedResult = FetchResult.fail(url, "Timeout");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> pageParser.parse(failedResult)
        );

        assertEquals(
                "Cannot parse page because fetch result was not successful.",
                ex.getMessage()
        );
    }
}