package io.github.tony8864.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tony8864.parse.ParsedPage;
import io.github.tony8864.repository.JsonPageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JsonPageRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldSaveParsedPageToHashedJsonFile() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonPageRepository repository = new JsonPageRepository(tempDir.toString(), mapper);

        ParsedPage parsedPage = new ParsedPage(
                "https://example.com/page1",
                "Example title",
                "Example description",
                "Example body text",
                List.of("https://example.com/about")
        );

        repository.save(parsedPage);

        String expectedFileName = md5(parsedPage.sourceUrl()) + ".json";
        Path expectedFile = tempDir.resolve(expectedFileName);

        assertTrue(Files.exists(expectedFile));

        String content = Files.readString(expectedFile);
        assertTrue(content.contains("\"sourceUrl\":\"https://example.com/page1\""));
        assertTrue(content.contains("\"title\":\"Example title\""));
        assertTrue(content.contains("\"description\":\"Example description\""));
        assertTrue(content.contains("\"bodyText\":\"Example body text\""));
        assertTrue(content.contains("https://example.com/about"));
    }

    @Test
    void shouldThrowPageRepositoryExceptionWhenWriteFails() throws Exception {
        ObjectMapper mapper = mock(ObjectMapper.class);

        doThrow(new IOException("Disk write failed"))
                .when(mapper)
                .writeValue(any(java.io.File.class), any(ParsedPage.class));

        JsonPageRepository repository = new JsonPageRepository(tempDir.toString(), mapper);

        ParsedPage parsedPage = new ParsedPage(
                "https://example.com/page1",
                "Example title",
                "Example description",
                "Example body text",
                List.of()
        );

        PageRepositoryException ex = assertThrows(
                PageRepositoryException.class,
                () -> repository.save(parsedPage)
        );

        assertTrue(ex.getMessage().contains("Failed to save parsed page"));
        assertTrue(ex.getMessage().contains("Example title"));
        assertNotNull(ex.getCause());
        assertInstanceOf(IOException.class, ex.getCause());
        assertEquals("Disk write failed", ex.getCause().getMessage());
    }

    @Test
    void shouldOverwriteFileWhenSavingSameSourceUrlAgain() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonPageRepository repository = new JsonPageRepository(tempDir.toString(), mapper);

        ParsedPage first = new ParsedPage(
                "https://example.com/page1",
                "Title 1",
                "Desc 1",
                "Body 1",
                List.of()
        );

        ParsedPage second = new ParsedPage(
                "https://example.com/page1",
                "Title 2",
                "Desc 2",
                "Body 2",
                List.of()
        );

        repository.save(first);
        repository.save(second);

        String expectedFileName = md5("https://example.com/page1") + ".json";
        Path expectedFile = tempDir.resolve(expectedFileName);

        assertTrue(Files.exists(expectedFile));

        String content = Files.readString(expectedFile);
        assertTrue(content.contains("\"title\":\"Title 2\""));
        assertFalse(content.contains("\"title\":\"Title 1\""));
    }

    private String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }
}