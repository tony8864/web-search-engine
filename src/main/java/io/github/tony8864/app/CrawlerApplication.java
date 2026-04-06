package io.github.tony8864.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tony8864.fetch.PageFetcher;
import io.github.tony8864.orchestrator.CrawlOrchestrator;
import io.github.tony8864.parse.PageParser;
import io.github.tony8864.repository.JsonPageRepository;
import io.github.tony8864.repository.PageRepository;
import io.github.tony8864.seed.ResourceSeedUrlSource;

public class CrawlerApplication {
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);

        ResourceSeedUrlSource provider = new ResourceSeedUrlSource("/seed-urls.json", mapper);
        PageFetcher fetcher = new PageFetcher();
        PageParser parser = new PageParser();
        PageRepository repository = new JsonPageRepository("crawled_pages", mapper);

        CrawlOrchestrator orchestrator = new CrawlOrchestrator(
                provider,
                repository,
                fetcher,
                parser
        );

        orchestrator.crawlFromSeeds();
    }

    public static void main(String[] args) {
        (new CrawlerApplication()).run();
    }
}
