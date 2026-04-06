package io.github.tony8864.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tony8864.domain.parse.ParsedPage;
import io.github.tony8864.application.repository.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class JsonPageRepository implements PageRepository {

    private static final Logger log = LoggerFactory.getLogger(JsonPageRepository.class);

    private final Path directory;
    private final ObjectMapper mapper;

    public JsonPageRepository(String directoryPath, ObjectMapper mapper) {
        this.directory = Paths.get(directoryPath);
        this.mapper = mapper;
    }

    @Override
    public void save(ParsedPage parsedPage) {
        try {
            String fileName = generateUniqueName(parsedPage.sourceUrl()) + ".json";
            Path filePath = directory.resolve(fileName);
            Files.createDirectories(directory);
            mapper.writeValue(filePath.toFile(), parsedPage);
        } catch (IOException e) {
            log.error("Failed to save parsed page");
            throw new PageRepositoryException("Failed to save parsed page (title: " + parsedPage.title() + ")", e);
        }
    }

    private String generateUniqueName(String url) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(url.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available", e);
        }
    }
}
