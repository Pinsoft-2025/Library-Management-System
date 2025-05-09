package io.github.SenaUstun_Dev.library_management.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Book Management APIs")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "List all books", description = "Returns a list of all available books.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
    })
    public ResponseEntity<List<BookResponse>> findAllBooks() {
        List<BookResponse> response = bookService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves the book with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
    })
    public ResponseEntity<BookResponse> findBookById(@PathVariable Long id) {
        BookResponse response = bookService.findBookById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/name")
    @Operation(summary = "Get book by name", description = "Retrieves the book with the specified name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid name parameter"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
    })
    public ResponseEntity<BookResponse> findBookByName(@RequestParam String name) {
        BookResponse response = bookService.findBookByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/author")
    @Operation(summary = "List books by author", description = "Retrieves books matching the author criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
    })
    public ResponseEntity<List<BookResponse>> findBookByAuthor(@RequestParam String authorCriteria) {
        List<BookResponse> response = bookService.findBookByAuthor(authorCriteria);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/genre")
    @Operation(summary = "List books by genre name", description = "Retrieves books belonging to the specified genre name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book genre not found"),
    })
    public ResponseEntity<List<BookResponse>> findBookByGenre(@RequestParam String genreName) {
        List<BookResponse> response = bookService.findBookByGenre(genreName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/publisher")
    @Operation(summary = "List books by publisher name", description = "Retrieves books published by the specified publisher name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Publisher not found"),
    })
    public ResponseEntity<List<BookResponse>> findBookByPublisher(@RequestParam String publisherName) {
        List<BookResponse> response = bookService.findBookByPublisher(publisherName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    @Operation(summary = "Create a new book", description = "Creates a new book. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload (e.g., missing name, validation error)"),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)"),
            @ApiResponse(responseCode = "409", description = "Book already exists with the same name"),
    })
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody CreateBookRequest request) {
        BookResponse response = bookService.addBook(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    @Operation(summary = "Update an existing book", description = "Updates the book with the given ID. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload (validation error)"),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
    })
    public ResponseEntity<BookResponse> updateBook(@Valid @RequestBody UpdateBookRequest request,
                                                   @PathVariable Long id) {
        BookResponse response = bookService.updateBook(request, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/{id}")
    @Operation(summary = "Delete a book", description = "Deletes the book with the given ID. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
    })
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}