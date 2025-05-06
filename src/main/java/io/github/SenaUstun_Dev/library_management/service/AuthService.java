// AuthService.java
package io.github.SenaUstun_Dev.library_management.service;

import io.github.SenaUstun_Dev.library_management.dto.request.LoginRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.RegisterRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.LoginResponse;
import io.github.SenaUstun_Dev.library_management.jwt_filter.JwtService;
import io.github.SenaUstun_Dev.library_management.entity.AppUser;
import io.github.SenaUstun_Dev.library_management.entity.Role;
import io.github.SenaUstun_Dev.library_management.repository.AppUserRepository;
import io.github.SenaUstun_Dev.library_management.repository.RoleRepository;
import io.github.SenaUstun_Dev.library_management.entity.UserDetailsImpl; // UserDetailsImpl
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);

        return new LoginResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities());
    }

    public AppUser register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {

            throw new IllegalArgumentException("Kullanıcı adı zaten kullanımda: " + registerRequest.username());
        }

        AppUser newUser = new AppUser();
        newUser.setUsername(registerRequest.username());
        newUser.setPassword(passwordEncoder.encode(registerRequest.password()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("'ROLE_USER' rolü veritabanında bulunamadı. Lütfen başlangıç verilerinizi kontrol edin."));

        roles.add(userRole);
        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }

}