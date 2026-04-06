package io.github.tony8864.domain.fetch;

public record FetchedPage(
        String requestedUrl,
        String rawHtml
) {
}
