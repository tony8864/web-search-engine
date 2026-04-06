package io.github.tony8864.application;

import io.github.tony8864.domain.fetch.FetchResult;
import io.github.tony8864.domain.parse.ParsedPage;

public interface PageParser {
    ParsedPage parse(FetchResult result);
}
