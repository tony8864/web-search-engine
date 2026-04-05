package io.github.tony8864.pageparser;

import java.util.List;

public record ParsedPage(
        String title,
        String description,
        String bodyText,
        List<String> urls
) {
}
