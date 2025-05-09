package io.github.SenaUstun_Dev.library_management.dto.request;

import java.util.Set;

import io.github.SenaUstun_Dev.library_management.entity.enums.BookStatus;

public record UpdateBookRequest(
        String name,
        BookStatus status,
        Set<Long> authorIds,
        Set<Long> publisherIds,
        Set<Long> genreIds
) {
}
