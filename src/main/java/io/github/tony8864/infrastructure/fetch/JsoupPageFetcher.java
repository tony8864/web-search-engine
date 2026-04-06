package io.github.tony8864.infrastructure.fetch;

import io.github.tony8864.application.fetch.PageFetcher;
import io.github.tony8864.domain.fetch.FetchResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupPageFetcher implements PageFetcher {

    @Override
    public FetchResult fetch(String url) {
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            return FetchResult.success(url, document.html());
        } catch (IOException e) {
            return FetchResult.fail(url, e.getMessage());
        }
    }
}
