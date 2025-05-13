package io.github.SenaUstun_Dev.library_management.dto.response;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record BorrowedBookResponse(
    Long id,
    LocalDate borrowDate,
    LocalDate dueDate,
    LocalDate actualReturnDate,
    boolean lost,
    AppUserResponse user,
    BookResponse book
) {
} 