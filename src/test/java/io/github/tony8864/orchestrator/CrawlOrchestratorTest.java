package io.github.tony8864.orchestrator;

import io.github.tony8864.application.CrawlerService;
import io.github.tony8864.domain.fetch.FetchResult;
import io.github.tony8864.infrastructure.fetch.JsoupPageFetcher;
import io.github.tony8864.infrastructure.parser.JsoupPageParser;
import io.github.tony8864.domain.parse.ParsedPage;
import io.github.tony8864.application.repository.PageRepository;
import io.github.tony8864.domain.seed.SeedUrl;
import io.github.tony8864.infrastructure.seed.ResourceSeedUrlSource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class CrawlOrchestratorTest {

    @Test
    void shouldFetchParseAndSaveSuccessfulSeedPage() {
        PageRepository repository = mock(PageRepository.class);
        ResourceSeedUrlSource provider = mock(ResourceSeedUrlSource.class);
        JsoupPageFetcher fetcher = mock(JsoupPageFetcher.class);
        JsoupPageParser parser = mock(JsoupPageParser.class);

        CrawlerService orchestrator = new CrawlerService(
                provider,
                repository,
                fetcher,
                parser
        );

        SeedUrl seed = new SeedUrl(1L, "", "https://example.com");
        FetchResult fetchResult = mock(FetchResult.class);
        ParsedPage parsedPage = mock(ParsedPage.class);

        when(provider.loadSeeds()).thenReturn(List.of(seed));
        when(fetcher.fetch("https://example.com")).thenReturn(fetchResult);
        when(fetchResult.isSuccess()).thenReturn(true);
        when(parser.parse(fetchResult)).thenReturn(parsedPage);

        orchestrator.crawlFromSeeds();

        verify(provider).loadSeeds();
        verify(fetcher).fetch("https://example.com");
        verify(parser).parse(fetchResult);
        verify(repository).save(parsedPage);
    }

    @Test
    void shouldSkipParseAndSaveWhenFetchFails() {
        PageRepository repository = mock(PageRepository.class);
        ResourceSeedUrlSource provider = mock(ResourceSeedUrlSource.class);
        JsoupPageFetcher fetcher = mock(JsoupPageFetcher.class);
        JsoupPageParser parser = mock(JsoupPageParser.class);

        CrawlerService orchestrator = new CrawlerService(
                provider,
                repository,
                fetcher,
                parser
        );

        SeedUrl seed = new SeedUrl(1L, "", "https://example.com");
        FetchResult fetchResult = mock(FetchResult.class);

        when(provider.loadSeeds()).thenReturn(List.of(seed));
        when(fetcher.fetch("https://example.com")).thenReturn(fetchResult);
        when(fetchResult.isSuccess()).thenReturn(false);

        orchestrator.crawlFromSeeds();

        verify(provider).loadSeeds();
        verify(fetcher).fetch("https://example.com");
        verify(parser, never()).parse(any());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldProcessMultipleSeeds() {
        PageRepository repository = mock(PageRepository.class);
        ResourceSeedUrlSource provider = mock(ResourceSeedUrlSource.class);
        JsoupPageFetcher fetcher = mock(JsoupPageFetcher.class);
        JsoupPageParser parser = mock(JsoupPageParser.class);

        CrawlerService orchestrator = new CrawlerService(
                provider,
                repository,
                fetcher,
                parser
        );

        SeedUrl firstSeed = new SeedUrl(1L, "", "https://example.com/1");
        SeedUrl secondSeed = new SeedUrl(1L, "", "https://example.com/2");

        FetchResult firstFetchResult = mock(FetchResult.class);
        FetchResult secondFetchResult = mock(FetchResult.class);

        ParsedPage firstParsedPage = mock(ParsedPage.class);
        ParsedPage secondParsedPage = mock(ParsedPage.class);

        when(provider.loadSeeds()).thenReturn(List.of(firstSeed, secondSeed));

        when(fetcher.fetch("https://example.com/1")).thenReturn(firstFetchResult);
        when(fetcher.fetch("https://example.com/2")).thenReturn(secondFetchResult);

        when(firstFetchResult.isSuccess()).thenReturn(true);
        when(secondFetchResult.isSuccess()).thenReturn(true);

        when(parser.parse(firstFetchResult)).thenReturn(firstParsedPage);
        when(parser.parse(secondFetchResult)).thenReturn(secondParsedPage);

        orchestrator.crawlFromSeeds();

        verify(fetcher).fetch("https://example.com/1");
        verify(fetcher).fetch("https://example.com/2");
        verify(parser).parse(firstFetchResult);
        verify(parser).parse(secondFetchResult);
        verify(repository).save(firstParsedPage);
        verify(repository).save(secondParsedPage);
    }

    @Test
    void shouldDoNothingWhenNoSeedsAreReturned() {
        PageRepository repository = mock(PageRepository.class);
        ResourceSeedUrlSource provider = mock(ResourceSeedUrlSource.class);
        JsoupPageFetcher fetcher = mock(JsoupPageFetcher.class);
        JsoupPageParser parser = mock(JsoupPageParser.class);

        CrawlerService orchestrator = new CrawlerService(
                provider,
                repository,
                fetcher,
                parser
        );

        when(provider.loadSeeds()).thenReturn(List.of());

        orchestrator.crawlFromSeeds();

        verify(provider).loadSeeds();
        verifyNoInteractions(fetcher, parser, repository);
    }
}