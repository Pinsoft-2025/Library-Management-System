package io.github.SenaUstun_Dev.library_management.dto.response;

import lombok.Builder;

@Builder
public record PublisherResponse(
        Long id,
        String name
) {
}
