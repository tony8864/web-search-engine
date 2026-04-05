package io.github.tony8864.parse;

import java.util.List;

public record ParsedPage(
        String sourceUrl,
        String title,
        String description,
        String bodyText,
        List<String> urls
) {
}
