package io.github.tony8864.repository;

public class PageRepositoryException extends RuntimeException {
    public PageRepositoryException(String message) {
        super(message);
    }

    public PageRepositoryException(String message, Throwable e) {
        super(message, e);
    }
}
