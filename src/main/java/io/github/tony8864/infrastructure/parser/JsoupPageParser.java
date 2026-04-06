package io.github.tony8864.infrastructure.parser;

import io.github.tony8864.application.PageParser;
import io.github.tony8864.domain.fetch.FetchResult;
import io.github.tony8864.domain.parse.ParsedPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;


public class JsoupPageParser implements PageParser {

    @Override
    public ParsedPage parse(FetchResult result) {

        if (!result.isSuccess()) {
            throw new IllegalArgumentException("Cannot parse page because fetch result was not successful.");
        }

        String html = result.rawHtml();

        Document document = Jsoup.parse(html, result.url());

        Element meta = document.selectFirst("meta[name=description]");
        String description = (meta != null) ? meta.attr("content") : "";

        Elements links = document.select("a[href]");
        List<String> urls = links.stream()
                .map(link -> link.attr("abs:href"))
                .filter(url -> !url.isEmpty())
                .toList();

        return new ParsedPage(
                result.url(),
                document.title(),
                description,
                document.text(),
                urls
        );
    }
}
