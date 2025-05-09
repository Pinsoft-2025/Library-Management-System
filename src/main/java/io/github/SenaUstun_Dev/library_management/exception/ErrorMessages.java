package io.github.SenaUstun_Dev.library_management.exception;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    AUTHOR_NOT_FOUND("Author not found"),
    PUBLISHER_NOT_FOUND("Publisher not found"),
    BOOKGENRE_NOT_FOUND("Book-genre not found"),
    BOOK_NOT_FOUND("Book not found");

    private final String message;

    private ErrorMessages(String message) {
        this.message = message;
    }

}
