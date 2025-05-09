package io.github.SenaUstun_Dev.library_management.dto.request;

public record UpdateUserRequest(
        String username,
        String email,
        String firstName,
        String lastName
) {
} 