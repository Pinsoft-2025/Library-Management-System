package io.github.SenaUstun_Dev.library_management.dto.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record LoginResponse(
        String token,
        String username,
        Collection<? extends GrantedAuthority> authorities
) {
}
