package io.github.tony8864.infrastructure.seed;

public class SeedLoadingException extends RuntimeException {
    public SeedLoadingException(String message) {
        super(message);
    }

    public SeedLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
