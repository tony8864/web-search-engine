package io.github.tony8864.application.seed;

import io.github.tony8864.domain.seed.SeedUrl;

import java.util.List;

public interface SeedUrlSource {
    List<SeedUrl> loadSeeds();
}
