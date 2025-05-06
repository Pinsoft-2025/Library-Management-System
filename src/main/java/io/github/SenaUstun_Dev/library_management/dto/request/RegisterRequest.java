package io.github.SenaUstun_Dev.library_management.dto.request;

public record RegisterRequest(
        String username,
        String password,
        int age
) {
}
