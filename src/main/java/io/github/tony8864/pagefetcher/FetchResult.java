package io.github.tony8864.pagefetcher;

import org.jsoup.nodes.Document;

import java.time.Instant;

public record FetchResult(
        Document document,
        Status status,
        String url,
        String errorMessage,
        Instant timestamp
) {

    public enum Status { SUCCESS, FAILURE }

    public static FetchResult success(String url, Document document) {
        return new FetchResult(document, Status.SUCCESS, url, null, Instant.now());
    }

    public static FetchResult fail(String url, String errorMessage) {
        return new FetchResult(null, Status.FAILURE, url, errorMessage, Instant.now());
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }
}
