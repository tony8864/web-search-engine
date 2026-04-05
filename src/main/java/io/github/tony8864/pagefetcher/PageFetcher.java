package io.github.tony8864.pagefetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PageFetcher {

    public FetchResult fetch(String url) {
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            return FetchResult.success(url, document);
        } catch (IOException e) {
            return FetchResult.fail(url, e.getMessage());
        }
    }
}
