package io.github.tony8864.pagerepository;

import java.io.IOException;

public class PageRepositoryException extends RuntimeException {
    public PageRepositoryException(String message, Throwable e) {
        super(message);
    }
}
