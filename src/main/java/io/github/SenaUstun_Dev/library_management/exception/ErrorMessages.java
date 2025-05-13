package io.github.SenaUstun_Dev.library_management.exception;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    AUTHOR_NOT_FOUND("Author not found"),
    PUBLISHER_NOT_FOUND("Publisher not found"),
    BOOKGENRE_NOT_FOUND("Book-genre not found"),
    BOOK_NOT_FOUND("Book not found"),
    RESOURCE_NOT_FOUND("Resource Not Found Exception"),
    BORROWING_NOT_ALLOWED("Borrowing not allowed"),
    MAX_BOOKS_EXCEEDED("Maximum books limit exceeded"),
    BOOK_NOT_AVAILABLE("Book is not available"),
    UNEXPECTED_ERROR("Unexpected error occurred");

    private final String message;

    private ErrorMessages(String message) {
        this.message = message;
    }

}
