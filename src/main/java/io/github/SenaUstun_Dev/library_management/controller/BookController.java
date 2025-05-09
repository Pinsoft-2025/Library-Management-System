package io.github.SenaUstun_Dev.library_management.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.SenaUstun_Dev.library_management.dto.request.CreateBookRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdateBookRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.BookResponse;
import io.github.SenaUstun_Dev.library_management.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Kitap Yönetim API'leri")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Tüm kitapları listele", description = "Mevcut tüm kitapların listesini döndürür.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitaplar başarıyla getirildi"),
    })
    public ResponseEntity<List<BookResponse>> findAllBooks() {
        List<BookResponse> response = bookService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID'ye göre kitap getir", description = "Belirtilen ID'ye sahip kitabı getirir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitap başarıyla getirildi"),
            @ApiResponse(responseCode = "404", description = "Kitap bulunamadı"),
    })
    public ResponseEntity<BookResponse> findBookById(@PathVariable Long id) {
        BookResponse response = bookService.findBookById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/name")
    @Operation(summary = "İsme göre kitap getir", description = "Belirtilen isme sahip kitabı getirir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitap başarıyla getirildi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz isim parametresi"),
            @ApiResponse(responseCode = "404", description = "Kitap bulunamadı"),
    })
    public ResponseEntity<BookResponse> findBookByName(@RequestParam String name) {
        BookResponse response = bookService.findBookByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/author")
    @Operation(summary = "Yazara göre kitapları listele", description = "Belirtilen yazar kriterlerine uyan kitapları getirir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitaplar başarıyla getirildi"),
    })
    public ResponseEntity<List<BookResponse>> findBookByAuthor(@RequestParam String authorCriteria) {
        List<BookResponse> response = bookService.findBookByAuthor(authorCriteria);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/genre")
    @Operation(summary = "Türe göre kitapları listele", description = "Belirtilen türe ait kitapları getirir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitaplar başarıyla getirildi"),
            @ApiResponse(responseCode = "404", description = "Kitap türü bulunamadı"),
    })
    public ResponseEntity<List<BookResponse>> findBookByGenre(@RequestParam String genreName) {
        List<BookResponse> response = bookService.findBookByGenre(genreName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/publisher")
    @Operation(summary = "Yayıncıya göre kitapları listele", description = "Belirtilen yayıncı tarafından yayınlanan kitapları getirir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitaplar başarıyla getirildi"),
            @ApiResponse(responseCode = "404", description = "Yayıncı bulunamadı"),
    })
    public ResponseEntity<List<BookResponse>> findBookByPublisher(@RequestParam String publisherName) {
        List<BookResponse> response = bookService.findBookByPublisher(publisherName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    @Operation(
            summary = "Yeni bir kitap oluştur", 
            description = "Yeni bir kitap oluşturur. Admin rolü gerektirir. Kitap bilgileri ile birlikte yazar, yayıncı ve tür ID'lerini içermelidir."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kitap başarıyla oluşturuldu"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek yapısı (örn. eksik isim, doğrulama hatası)"),
            @ApiResponse(responseCode = "403", description = "Erişim reddedildi (admin rolü gerekli)"),
            @ApiResponse(responseCode = "404", description = "Bir veya daha fazla ilişkili kaynak (yazar, tür, yayıncı) bulunamadı"),
            @ApiResponse(responseCode = "409", description = "Aynı isimde kitap zaten mevcut"),
    })
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody CreateBookRequest request) {
        BookResponse response = bookService.addBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    @Operation(
            summary = "Mevcut bir kitabı güncelle", 
            description = "Belirtilen ID'ye sahip kitabı günceller. Admin rolü gerektirir. Yazar, yayıncı ve tür ID'leri ile güncellemeler yapılabilir."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kitap başarıyla güncellendi"),
            @ApiResponse(responseCode = "400", description = "Geçersiz istek yapısı (doğrulama hatası)"),
            @ApiResponse(responseCode = "403", description = "Erişim reddedildi (admin rolü gerekli)"),
            @ApiResponse(responseCode = "404", description = "Kitap veya ilişkili kaynaklar bulunamadı"),
    })
    public ResponseEntity<BookResponse> updateBook(@Valid @RequestBody UpdateBookRequest request,
                                                   @PathVariable Long id) {
        BookResponse response = bookService.updateBook(request, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/{id}")
    @Operation(summary = "Bir kitabı sil", description = "Belirtilen ID'ye sahip kitabı siler. Admin rolü gerektirir.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kitap başarıyla silindi"),
            @ApiResponse(responseCode = "403", description = "Erişim reddedildi (admin rolü gerekli)"),
            @ApiResponse(responseCode = "404", description = "Kitap bulunamadı"),
    })
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}