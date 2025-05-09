package io.github.SenaUstun_Dev.library_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.SenaUstun_Dev.library_management.dto.request.UpdateUserRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.AppUserResponse;
import io.github.SenaUstun_Dev.library_management.entity.AppUser;
import io.github.SenaUstun_Dev.library_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Kullanıcı Yönetimi", description = "Kullanıcı bilgilerini yönetmek için API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Tüm kullanıcıları listeleme", description = "Sistemdeki tüm kullanıcıları listeler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcılar başarıyla listelendi",
                    content = @Content(schema = @Schema(implementation = AppUserResponse.class)))
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<AppUserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @Operation(summary = "ID ile kullanıcı bulma", description = "Belirtilen ID'ye sahip kullanıcıyı getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla bulundu",
                    content = @Content(schema = @Schema(implementation = AppUserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AppUserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Operation(summary = "Kullanıcı adı ile kullanıcı bulma", description = "Belirtilen kullanıcı adına sahip kullanıcıyı getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla bulundu",
                    content = @Content(schema = @Schema(implementation = AppUserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AppUserResponse> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    @Operation(summary = "E-posta ile kullanıcı bulma", description = "Belirtilen e-postaya sahip kullanıcıyı getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla bulundu",
                    content = @Content(schema = @Schema(implementation = AppUserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AppUserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @Operation(summary = "İsme göre kullanıcı arama", description = "Belirtilen isme sahip kullanıcıları getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcılar başarıyla listelendi",
                    content = @Content(schema = @Schema(implementation = AppUserResponse.class)))
    })
    @GetMapping("/firstName/{firstName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<AppUserResponse>> getUsersByFirstName(@PathVariable String firstName) {
        return ResponseEntity.ok(userService.findUsersByFirstName(firstName));
    }

    @Operation(summary = "Soyada göre kullanıcı arama", description = "Belirtilen soyada sahip kullanıcıları getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcılar başarıyla listelendi",
                    content = @Content(schema = @Schema(implementation = AppUserResponse.class)))
    })
    @GetMapping("/lastName/{lastName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<AppUserResponse>> getUsersByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(userService.findUsersByLastName(lastName));
    }

    @Operation(summary = "Kullanıcı bilgilerini güncelleme", description = "Belirtilen ID'ye sahip kullanıcının bilgilerini günceller")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla güncellendi",
                    content = @Content(schema = @Schema(implementation = AppUserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AppUserResponse> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
    }

    @Operation(summary = "Kullanıcı silme", description = "Belirtilen ID'ye sahip kullanıcıyı siler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kullanıcı başarıyla silindi"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    // Yeni admin endpoint'leri
    @Operation(summary = "[ADMIN] ID ile kullanıcı entity'sini getirme", description = "Belirtilen ID'ye sahip kullanıcının tüm entity bilgilerini getirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı başarıyla bulundu",
                    content = @Content(schema = @Schema(implementation = AppUser.class))),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AppUser> getAppUserEntityById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserEntityById(id));
    }
    
    @Operation(summary = "[ADMIN] Tüm kullanıcı entity'lerini getirme", description = "Sistemdeki tüm kullanıcıların entity bilgilerini listeler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcılar başarıyla listelendi",
                    content = @Content(schema = @Schema(implementation = AppUser.class)))
    })
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppUser>> getAllAppUserEntities() {
        return ResponseEntity.ok(userService.findAllUserEntities());
    }
} 