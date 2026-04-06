package io.github.tony8864.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ResourceSeedUrlSource implements SeedUrlSource {

    private static final Logger log = LoggerFactory.getLogger(ResourceSeedUrlSource.class);

    private final ObjectMapper mapper;
    private final String seedUrlsPath;

    public ResourceSeedUrlSource(String seedUrlsPath, ObjectMapper mapper) {
        this.seedUrlsPath = seedUrlsPath;
        this.mapper = mapper;
    }

    public List<SeedUrl> loadSeeds() {
        try (InputStream inputStream = ResourceSeedUrlSource.class.getResourceAsStream(seedUrlsPath)) {
            if (inputStream == null) {
                log.error("Resource file not found: {}", seedUrlsPath);
                throw new SeedLoadingException("Seed file was not found in resources: " + seedUrlsPath);
            }

            JsonNode root = mapper.readTree(inputStream);

            log.info("Loading seed URLs from resource {}", seedUrlsPath);
            return mapper.convertValue(
                    root.get("seed-urls"),
                    new TypeReference<List<SeedUrl>>() {}
            );
        } catch (IOException e) {
            log.error("IO error while reading seed urls from resource {}", seedUrlsPath, e);
            throw new SeedLoadingException("Could not load seed urls from resource: " + seedUrlsPath, e);
        }
    }
}
