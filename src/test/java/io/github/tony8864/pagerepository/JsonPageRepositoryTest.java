package io.github.tony8864.pagerepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tony8864.pageparser.ParsedPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonPageRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldSaveParsedPageToJsonFile() throws IOException {
        Path outputFile = tempDir.resolve("page.json");
        ObjectMapper mapper = new ObjectMapper();

        JsonPageRepository repository = new JsonPageRepository(outputFile.toString(), mapper);

        ParsedPage parsedPage = new ParsedPage(
                "Example title",
                "Example description",
                "Example body text",
                List.of("https://example.com/about", "https://example.com/contact")
        );

        repository.save(parsedPage);

        assertTrue(Files.exists(outputFile));

        String content = Files.readString(outputFile);
        assertTrue(content.contains("\"title\":\"Example title\""));
        assertTrue(content.contains("\"description\":\"Example description\""));
        assertTrue(content.contains("\"bodyText\":\"Example body text\""));
        assertTrue(content.contains("https://example.com/about"));
        assertTrue(content.contains("https://example.com/contact"));
    }

    @Test
    void shouldThrowPageRepositoryExceptionWhenMapperFails() throws Exception {
        Path outputFile = tempDir.resolve("page.json");

        ObjectMapper mapper = mock(ObjectMapper.class);
        doThrow(new IOException("Disk write failed"))
                .when(mapper)
                .writeValue(any(java.io.Writer.class), any(ParsedPage.class));

        JsonPageRepository repository = new JsonPageRepository(outputFile.toString(), mapper);

        ParsedPage parsedPage = new ParsedPage(
                "Example title",
                "Example description",
                "Example body text",
                List.of("https://example.com/about")
        );

        PageRepositoryException ex = assertThrows(
                PageRepositoryException.class,
                () -> repository.save(parsedPage)
        );

        assertTrue(ex.getMessage().contains("Failed to save parsed page"));
        assertTrue(ex.getMessage().contains("Example title"));
        assertTrue(ex.getMessage().contains(outputFile.toString()));
    }

    @Test
    void shouldCreateOrOverwriteExistingFile() throws IOException {
        Path outputFile = tempDir.resolve("page.json");
        Files.writeString(outputFile, "old content");

        ObjectMapper mapper = new ObjectMapper();
        JsonPageRepository repository = new JsonPageRepository(outputFile.toString(), mapper);

        ParsedPage parsedPage = new ParsedPage(
                "New title",
                "New description",
                "New body",
                List.of()
        );

        repository.save(parsedPage);

        String content = Files.readString(outputFile);
        assertFalse(content.contains("old content"));
        assertTrue(content.contains("\"title\":\"New title\""));
    }
}