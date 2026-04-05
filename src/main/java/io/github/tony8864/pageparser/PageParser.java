package io.github.tony8864.pageparser;

import io.github.tony8864.pagefetcher.FetchResult;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;


public class PageParser {

    public ParsedPage parse(FetchResult result) {

        if (!result.isSuccess()) {
            throw new IllegalArgumentException("Cannot parse page because fetch result was not successful.");
        }

        Document document = result.document();

        Element meta = document.selectFirst("meta[name=description]");
        String description = (meta != null) ? meta.attr("content") : "";

        Elements links = document.select("a[href]");
        List<String> urls = links.stream()
                .map(link -> link.attr("abs:href"))
                .filter(url -> !url.isEmpty())
                .toList();

        return new ParsedPage(
                document.title(),
                description,
                document.text(),
                urls
        );
    }
}
