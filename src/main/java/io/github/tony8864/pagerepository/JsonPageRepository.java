package io.github.tony8864.pagerepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tony8864.pageparser.ParsedPage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonPageRepository implements PageRepository {

    private final String path;
    private final ObjectMapper mapper;

    public JsonPageRepository(String path, ObjectMapper mapper) {
        this.path = path;
        this.mapper = mapper;
    }

    @Override
    public void save(ParsedPage parsedPage) {
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
            mapper.writeValue(writer, parsedPage);
        } catch (IOException e) {
            throw new PageRepositoryException("Failed to save parsed page (title: " + parsedPage.title() + ") at file path: " + path, e);
        }
    }
}
