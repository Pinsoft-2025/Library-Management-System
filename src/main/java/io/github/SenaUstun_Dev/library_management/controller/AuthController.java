package io.github.SenaUstun_Dev.library_management.controller;

import io.github.SenaUstun_Dev.library_management.dto.request.LoginRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.RegisterRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.LoginResponse;
import io.github.SenaUstun_Dev.library_management.entity.AppUser;
import io.github.SenaUstun_Dev.library_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Kullanıcı kimlik doğrulama ve kayıt işlemleri")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Yeni kullanıcı kaydı", description = "Sisteme yeni bir kullanıcı kaydı yapar ve varsayılan 'USER' rolünü atar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kullanıcı başarıyla kaydedildi",
                    content = @Content(schema = @Schema(implementation = AppUser.class))),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek veya kullanıcı adı zaten kullanımda",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "'ROLE_USER' rolü veritabanında bulunamadı veya beklenmedik bir hata oluştu",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            AppUser registeredUser = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Kullanıcı girişi", description = "Kullanıcı adı ve şifre ile kimlik doğrulaması yapar ve başarılı olursa JWT tokenı döndürür.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Giriş başarılı, JWT tokenı döndürüldü",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Geçersiz kullanıcı adı veya şifre",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "Giriş sırasında beklenmedik bir hata oluştu",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = authService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz kullanıcı adı veya şifre.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Giriş sırasında bir hata oluştu.");
        }
    }
}
