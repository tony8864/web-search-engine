package io.github.tony8864.application.repository;

import io.github.tony8864.domain.parse.ParsedPage;

public interface PageRepository {
    void save(ParsedPage parsedPage);
}
