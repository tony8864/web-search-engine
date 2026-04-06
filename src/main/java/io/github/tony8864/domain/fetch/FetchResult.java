package io.github.tony8864.domain.fetch;

import java.time.Instant;

public record FetchResult(
        String url,
        String rawHtml,
        Status status,
        String errorMessage,
        Instant fetchAt
) {
    public enum Status { SUCCESS, FAILURE }

    public static FetchResult success(String requestedUrl, String rawHtml) {
        return new FetchResult(requestedUrl, rawHtml, Status.SUCCESS, null, Instant.now());
    }

    public static FetchResult fail(String requestedUrl, String errorMessage) {
        return new FetchResult(requestedUrl, null, Status.FAILURE, errorMessage, Instant.now());
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }
}
