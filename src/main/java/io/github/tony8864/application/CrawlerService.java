package io.github.tony8864.application;

import io.github.tony8864.application.fetch.PageFetcher;
import io.github.tony8864.application.repository.PageRepository;
import io.github.tony8864.application.seed.SeedUrlSource;

import io.github.tony8864.domain.fetch.FetchResult;
import io.github.tony8864.domain.parse.ParsedPage;
import io.github.tony8864.domain.seed.SeedUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CrawlerService {
    private static final Logger log = LoggerFactory.getLogger(CrawlerService.class);

    private final SeedUrlSource seedUrlSource;
    private final PageRepository repository;
    private final PageFetcher fetcher;
    private final PageParser parser;

    public CrawlerService(
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
