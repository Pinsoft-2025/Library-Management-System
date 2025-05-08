package io.github.SenaUstun_Dev.library_management.controller;

import io.github.SenaUstun_Dev.library_management.dto.request.CreateBookGenreRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdateBookGenreRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.BookGenreResponse;
import io.github.SenaUstun_Dev.library_management.service.BookGenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
@Tag(name = "Book Genres", description = "Book Genre Management APIs")
public class BookGenreController {

    private final BookGenreService bookGenreService;

    @GetMapping
    @Operation(summary = "List all book genres", description = "Returns a list of all available book genres.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genres retrieved successfully"),
    })
    public ResponseEntity<List<BookGenreResponse>> findAllBookGenres() {
        List<BookGenreResponse> response = bookGenreService.findAllBookGenres();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book genre by ID", description = "Retrieves the book genre with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book genre retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book genre not found"),
    })
    public ResponseEntity<BookGenreResponse> findBookGenreById(@PathVariable Long id) {
        BookGenreResponse response = bookGenreService.findBookGenreById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Get book genre by name", description = "Retrieves the book genre with the specified name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book genre retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book genre not found"),
            @ApiResponse(responseCode = "400", description = "Invalid name parameter"),
    })
    public ResponseEntity<BookGenreResponse> findBookGenreByName(@RequestParam String name) {
        BookGenreResponse response = bookGenreService.findBookGenreByName(name);
        return ResponseEntity.ok(response);
    }


    // Admin endpoints
    @PostMapping("/admin") // POST /api/genres/admin -> Yeni tür ekle (Admin)
    @Operation(summary = "Create a new book genre", description = "Creates a new book genre. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book genre created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload (e.g., missing name)"),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)"),
            @ApiResponse(responseCode = "409", description = "Book genre already exists"),
    })
    public ResponseEntity<BookGenreResponse> addBookGenre(@Valid @RequestBody CreateBookGenreRequest request) {
        BookGenreResponse response = bookGenreService.addBookGenre(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    @Operation(summary = "Update an existing book genre", description = "Updates the book genre with the given ID. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book genre updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"), // @Valid ile tetiklenir
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)"), // Yetkilendirme hatası
            @ApiResponse(responseCode = "404", description = "Book genre not found"), // Service 404 fırlatmalı
    })
    public ResponseEntity<BookGenreResponse> updateBookGenre(@Valid @RequestBody UpdateBookGenreRequest request,
                                                             @PathVariable Long id) {
        BookGenreResponse response = bookGenreService.updateBookGenre(request, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/{id}")
    @Operation(summary = "Delete a book genre", description = "Deletes the book genre with the given ID. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book genre deleted successfully (No Content)"), // Başarılı silme için 204 kullanılır
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)"), // Yetkilendirme hatası
            @ApiResponse(responseCode = "404", description = "Book genre not found"), // Service 404 fırlatmalı
    })
    public ResponseEntity<Void> deleteBookGenre(@PathVariable Long id) {
        bookGenreService.deleteBookGenre(id);
        return ResponseEntity.noContent().build();
    }

}