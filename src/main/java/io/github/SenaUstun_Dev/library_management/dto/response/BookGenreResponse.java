package io.github.SenaUstun_Dev.library_management.dto.response;

import lombok.Builder;

@Builder
public record BookGenreResponse(
        Long id,
        String name
) {
}
