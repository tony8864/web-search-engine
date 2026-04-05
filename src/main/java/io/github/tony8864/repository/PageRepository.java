package io.github.tony8864.repository;

import io.github.tony8864.parse.ParsedPage;

public interface PageRepository {
    void save(ParsedPage parsedPage);
}
