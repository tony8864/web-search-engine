package io.github.tony8864.orchestrator;

import io.github.tony8864.fetch.FetchResult;
import io.github.tony8864.fetch.PageFetcher;
import io.github.tony8864.parse.PageParser;
import io.github.tony8864.parse.ParsedPage;
import io.github.tony8864.repository.PageRepository;
import io.github.tony8864.seed.SeedUrl;
import io.github.tony8864.seed.ResourceSeedUrlSource;
import io.github.tony8864.seed.SeedUrlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CrawlOrchestrator {
    private static final Logger log = LoggerFactory.getLogger(CrawlOrchestrator.class);

    private final SeedUrlSource seedUrlSource;
    private final PageRepository repository;
    private final PageFetcher fetcher;
    private final PageParser parser;

    public CrawlOrchestrator(
            SeedUrlSource seedUrlSource,
            PageRepository repository,
            PageFetcher fetcher,
            PageParser parser
    ) {
        this.seedUrlSource = seedUrlSource;
        this.repository = repository;
        this.fetcher = fetcher;
        this.parser = parser;
    }

    public void crawlFromSeeds() {
        List<SeedUrl> seeds = seedUrlSource.loadSeeds();
        log.info("Loaded seeds");

        FetchResult result;
        ParsedPage parsedPage;

        for (SeedUrl seedUrl : seeds) {
            log.info("Fetching URL {}", seedUrl.url());
            result = fetcher.fetch(seedUrl.url());

            if (result.isSuccess()) {
                log.info("Successfully fetched {}", result.url());

                parsedPage = parser.parse(result);
                log.info("Parsed page: title = '{}'", parsedPage.title());

                repository.save(parsedPage);
                log.info("Saved page for URL {}", parsedPage.sourceUrl());
            } else {
                log.warn("Failed to fetch URL {}", seedUrl.url());
            }
        }

        log.info("Crawl completed");
    }
}
