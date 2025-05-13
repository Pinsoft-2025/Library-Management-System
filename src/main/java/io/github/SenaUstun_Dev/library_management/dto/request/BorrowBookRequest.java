package io.github.SenaUstun_Dev.library_management.dto.request;

import lombok.Builder;

@Builder
public record BorrowBookRequest(
    Long bookId
) {
} 