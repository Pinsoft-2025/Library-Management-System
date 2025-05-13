package io.github.SenaUstun_Dev.library_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.SenaUstun_Dev.library_management.auth.UserDetailsImpl;
import io.github.SenaUstun_Dev.library_management.dto.request.BorrowBookRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.BorrowedBookResponse;
import io.github.SenaUstun_Dev.library_management.service.BorrowedBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/borrowed-books")
@RequiredArgsConstructor
@Tag(name = "Ödünç Kitap Yönetimi", description = "Ödünç kitap işlemlerini yönetmek için API")
public class BorrowedBookController {

    private final BorrowedBookService borrowedBookService;

    @Operation(summary = "Kitap ödünç alma", description = "Kullanıcının kitabı ödünç almasını sağlar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kitap başarıyla ödünç alındı"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek (maksimum kitap sınırı aşıldı veya kitap ödünç alınamaz)"),
            @ApiResponse(responseCode = "403", description = "Ödünç alma izni yok"),
            @ApiResponse(responseCode = "404", description = "Kitap bulunamadı")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<BorrowedBookResponse> borrowBook(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody BorrowBookRequest request) {
        BorrowedBookResponse response = borrowedBookService.borrowBook(userDetails.getUser(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Kullanıcının aktif ödünç kitapları", description = "Kullanıcının şu anda ödünç aldığı kitapları listeler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitaplar başarıyla listelendi")
    })
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<BorrowedBookResponse>> getCurrentlyBorrowedBooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BorrowedBookResponse> response = borrowedBookService.getCurrentlyBorrowedBooks(userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Kullanıcının ödünç kitap geçmişi", description = "Kullanıcının daha önce ödünç aldığı kitapları listeler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitaplar başarıyla listelendi")
    })
    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<BorrowedBookResponse>> getBorrowedBookHistory(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BorrowedBookResponse> response = borrowedBookService.getBorrowedBookHistory(userDetails.getUser());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[ADMIN] Kullanıcının aktif ödünç kitapları", description = "Belirtilen kullanıcının şu anda ödünç aldığı kitapları listeler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitaplar başarıyla listelendi"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @GetMapping("/admin/users/{userId}/current")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowedBookResponse>> getUserCurrentlyBorrowedBooks(@PathVariable Long userId) {
        List<BorrowedBookResponse> response = borrowedBookService.getUserCurrentlyBorrowedBooks(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[ADMIN] Kullanıcının ödünç kitap geçmişi", description = "Belirtilen kullanıcının daha önce ödünç aldığı kitapları listeler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitaplar başarıyla listelendi"),
            @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    })
    @GetMapping("/admin/users/{userId}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowedBookResponse>> getUserBorrowedBookHistory(@PathVariable Long userId) {
        List<BorrowedBookResponse> response = borrowedBookService.getUserBorrowedBookHistory(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "[ADMIN] Kayıp kitapları görüntüleme", description = "Kayıp olarak işaretlenen tüm kitapları listeler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kayıp kitaplar başarıyla listelendi")
    })
    @GetMapping("/admin/lost")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BorrowedBookResponse>> getAllLostBooks() {
        List<BorrowedBookResponse> response = borrowedBookService.getAllLostBooks();
        return ResponseEntity.ok(response);
    }
} 