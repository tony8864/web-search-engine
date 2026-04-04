package io.github.tony8864.seedurlprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SeedUrlProviderTest {

    private final SeedUrlProvider provider = new SeedUrlProvider();

    @Test
    void shouldLoadSeedsSuccessfully() {
        ObjectMapper mapper = new ObjectMapper();

        List<SeedUrl> result = provider.loadSeeds("/seed-urls.json", mapper);

        assertNotNull(result);
        assertEquals(2, result.size());

        System.out.println(result);

        assertEquals("https://example.com", result.getFirst().url());
        assertEquals("https://another-example.com", result.get(1).url());
    }

    @Test
    void shouldThrowSeedLoadingExceptionWhenResourceDoesNotExist() {
        ObjectMapper mapper = new ObjectMapper();
        String path = "/does not exist";

        SeedLoadingException ex = assertThrows(
                SeedLoadingException.class,
                () -> provider.loadSeeds(path, mapper)
        );

        assertEquals("Seed file was not found in resources: " + path, ex.getMessage());
    }

    @Test
    void shouldThrowSeedLoadingExceptionWhenObjectMapperFailsToRead() throws Exception {
        ObjectMapper mapper = Mockito.mock(ObjectMapper.class);

        Mockito.when(mapper.readTree(Mockito.any(InputStream.class)))
                .thenThrow(new IOException("Broken JSON") {});

        SeedLoadingException ex = assertThrows(
                SeedLoadingException.class,
                () -> provider.loadSeeds("/seed-urls.json", mapper)
        );

        assertTrue(ex.getMessage().contains("Could not load seed urls from resource: /seed-urls.json"));
        assertNotNull(ex.getCause());
        assertEquals("Broken JSON", ex.getCause().getMessage());
    }
}