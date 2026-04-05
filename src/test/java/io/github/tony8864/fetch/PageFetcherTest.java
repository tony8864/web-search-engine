package io.github.tony8864.fetch;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PageFetcherTest {

    private final PageFetcher pageFetcher = new PageFetcher();

    @Test
    void shouldReturnSuccessWhenPageIsFetched() throws IOException {
        String url = "https://example.com";

        Document document = mock(Document.class);
        Connection connection = mock(Connection.class);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(connection);

            when(connection.userAgent("Mozilla/5.0")).thenReturn(connection);
            when(connection.timeout(5000)).thenReturn(connection);
            when(connection.get()).thenReturn(document);

            FetchResult result = pageFetcher.fetch(url);

            assertNotNull(result);

            // adjust these assertions to match your FetchResult API
            assertTrue(result.isSuccess());
            assertEquals(url, result.url());
            assertEquals(document, result.document());
            assertNull(result.errorMessage());

            jsoupMock.verify(() -> Jsoup.connect(url));
            verify(connection).userAgent("Mozilla/5.0");
            verify(connection).timeout(5000);
            verify(connection).get();
        }
    }

    @Test
    void shouldReturnFailureWhenJsoupThrowsIOException() throws IOException {
        String url = "https://example.com";

        Connection connection = mock(Connection.class);

        try (MockedStatic<Jsoup> jsoupMock = mockStatic(Jsoup.class)) {
            jsoupMock.when(() -> Jsoup.connect(url)).thenReturn(connection);

            when(connection.userAgent("Mozilla/5.0")).thenReturn(connection);
            when(connection.timeout(5000)).thenReturn(connection);
            when(connection.get()).thenThrow(new IOException("Connection timed out"));

            FetchResult result = pageFetcher.fetch(url);

            assertNotNull(result);

            // adjust these assertions to match your FetchResult API
            assertFalse(result.isSuccess());
            assertEquals(url, result.url());
            assertNull(result.document());
            assertEquals("Connection timed out", result.errorMessage());

            jsoupMock.verify(() -> Jsoup.connect(url));
            verify(connection).userAgent("Mozilla/5.0");
            verify(connection).timeout(5000);
            verify(connection).get();
        }
    }
}