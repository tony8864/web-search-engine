package io.github.tony8864.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tony8864.fetch.PageFetcher;
import io.github.tony8864.orchestrator.CrawlOrchestrator;
import io.github.tony8864.parse.PageParser;
import io.github.tony8864.repository.JsonPageRepository;
import io.github.tony8864.repository.PageRepository;
import io.github.tony8864.seed.SeedUrlProvider;

public class CrawlerApplication {
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        SeedUrlProvider provider = new SeedUrlProvider(mapper);
        PageFetcher fetcher = new PageFetcher();
        PageParser parser = new PageParser();
        PageRepository repository = new JsonPageRepository("crawled_pages", mapper);
        CrawlOrchestrator orchestrator = new CrawlOrchestrator(
                repository,
                provider,
                fetcher,
                parser
        );

        orchestrator.crawlFromSeeds("/seed-urls.json");
    }

    public static void main(String[] args) {
        (new CrawlerApplication()).run();
    }
}
