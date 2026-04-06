package io.github.tony8864.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tony8864.infrastructure.fetch.JsoupPageFetcher;
import io.github.tony8864.application.CrawlerService;
import io.github.tony8864.infrastructure.parser.JsoupPageParser;
import io.github.tony8864.infrastructure.repository.JsonPageRepository;
import io.github.tony8864.application.repository.PageRepository;
import io.github.tony8864.infrastructure.seed.ResourceSeedUrlSource;

public class WebSearchEngineApplication {
    public void run() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);

        ResourceSeedUrlSource provider = new ResourceSeedUrlSource("/seed-urls.json", mapper);
        JsoupPageFetcher fetcher = new JsoupPageFetcher();
        JsoupPageParser parser = new JsoupPageParser();
        PageRepository repository = new JsonPageRepository("crawled_pages", mapper);

        CrawlerService orchestrator = new CrawlerService(
                provider,
                repository,
                fetcher,
                parser
        );

        orchestrator.crawlFromSeeds();
    }

    public static void main(String[] args) {
        (new WebSearchEngineApplication()).run();
    }
}
