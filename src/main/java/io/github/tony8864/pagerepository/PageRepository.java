package io.github.tony8864.pagerepository;

import io.github.tony8864.pageparser.ParsedPage;

public interface PageRepository {
    void save(ParsedPage parsedPage);
}
