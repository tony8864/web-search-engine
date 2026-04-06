package io.github.tony8864.application.fetch;

import io.github.tony8864.domain.fetch.FetchResult;

public interface PageFetcher {
    FetchResult fetch(String url);
}
