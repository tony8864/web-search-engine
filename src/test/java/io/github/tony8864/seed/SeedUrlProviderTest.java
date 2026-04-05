package io.github.tony8864.seed;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeedUrlProviderTest {

    @Test
    void shouldLoadSeedsSuccessfully() {
        ObjectMapper mapper = new ObjectMapper();
        SeedUrlProvider provider = new SeedUrlProvider(mapper);

        List<SeedUrl> result = provider.loadSeeds("/seed-urls.json");

        assertNotNull(result);
        assertEquals(2, result.size());

        System.out.println(result);

        assertEquals("https://example.com", result.getFirst().url());
        assertEquals("https://another-example.com", result.get(1).url());
    }

    @Test
    void shouldThrowSeedLoadingExceptionWhenResourceDoesNotExist() {
        ObjectMapper mapper = new ObjectMapper();
        SeedUrlProvider provider = new SeedUrlProvider(mapper);
        String path = "/does not exist";

        SeedLoadingException ex = assertThrows(
                SeedLoadingException.class,
                () -> provider.loadSeeds(path)
        );

        assertEquals("Seed file was not found in resources: " + path, ex.getMessage());
    }

    @Test
    void shouldThrowSeedLoadingExceptionWhenObjectMapperFailsToRead() throws Exception {
        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);
        SeedUrlProvider provider = new SeedUrlProvider(mapper);

        Mockito.when(mapper.readTree(Mockito.any(InputStream.class)))
                .thenThrow(new IOException("Broken JSON") {});

        SeedLoadingException ex = assertThrows(
                SeedLoadingException.class,
                () -> provider.loadSeeds("/seed-urls.json")
        );

        assertTrue(ex.getMessage().contains("Could not load seed urls from resource: /seed-urls.json"));
        assertNotNull(ex.getCause());
        assertEquals("Broken JSON", ex.getCause().getMessage());
    }
}