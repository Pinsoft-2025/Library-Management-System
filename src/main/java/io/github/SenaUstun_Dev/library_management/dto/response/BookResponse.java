package io.github.SenaUstun_Dev.library_management.dto.response;

import java.util.Set;

import io.github.SenaUstun_Dev.library_management.entity.enums.BookStatus;
import lombok.Builder;

@Builder
public record BookResponse(
        Long id,
        String name,
        BookStatus status,
        Set<AuthorResponse> authors,
        Set<PublisherResponse> publishers,
        Set<BookGenreResponse> genres
) {
}
