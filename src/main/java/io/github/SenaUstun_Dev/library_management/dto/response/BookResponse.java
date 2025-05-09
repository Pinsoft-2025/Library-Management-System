package io.github.SenaUstun_Dev.library_management.dto.response;

import io.github.SenaUstun_Dev.library_management.entity.Author;
import io.github.SenaUstun_Dev.library_management.entity.BookGenre;
import io.github.SenaUstun_Dev.library_management.entity.Publisher;
import io.github.SenaUstun_Dev.library_management.entity.enums.BookStatus;
import lombok.Builder;

import java.util.Set;

@Builder
public record BookResponse(
        String name,
        BookStatus status,
        Set<Author> authors,
        Set<Publisher> publisher,
        Set<BookGenre> genres
) {
}
