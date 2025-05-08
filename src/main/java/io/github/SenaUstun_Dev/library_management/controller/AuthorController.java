package io.github.SenaUstun_Dev.library_management.controller;

import io.github.SenaUstun_Dev.library_management.dto.request.CreateAuthorRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdateAuthorRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.AuthorResponse;
import io.github.SenaUstun_Dev.library_management.dto.response.ErrorResponse;
import io.github.SenaUstun_Dev.library_management.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Author Management",
        description = """
        This endpoint group manages authors. Admin users have full access to create, update, and delete authors.
        Public users can view authors by ID, search by criteria, and list all authors.
        """)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Create a new author (Admin)",
            description = "Creates a new author with pen name, first name, and second name. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload (validation error)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Author already exists (e.g., duplicate pen name)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/admin")
    public ResponseEntity<AuthorResponse> addAuthor(@Valid @RequestBody CreateAuthorRequest request) {
        AuthorResponse response = authorService.addAuthor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an author (Admin)",
            description = "Updates the details of an existing author by ID. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload (validation error)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/admin/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @Parameter(description = "ID of the author to update")
            @PathVariable Long id,
            @Valid @RequestBody UpdateAuthorRequest request) {
        AuthorResponse response = authorService.updateAuthor(request, id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an author (Admin)",
            description = "Deletes an author permanently by ID. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully (No Content)"),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @Parameter(description = "ID of the author to delete")
            @PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get author details by ID (Public)",
            description = "Retrieves the full details of an author by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author found",
                    content = @Content(schema = @Schema(implementation = AuthorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
            @Parameter(description = "ID of the author to retrieve")
            @PathVariable Long id) {
        AuthorResponse response = authorService.findAuthorById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all authors (Public)",
            description = "Retrieves a list of all registered authors.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authors listed successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        List<AuthorResponse> response = authorService.findAllAuthors();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search authors by criteria (Public)",
            description = """
            Lists authors matching the provided criteria: penName, firstName, or secondName.
            Each parameter is optional.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authors listed successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<AuthorResponse>> searchAuthors(
            @RequestParam(required = false) String penName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String secondName) {
        List<AuthorResponse> response = authorService.findAuthorsByCriteria(penName, firstName, secondName);
        return ResponseEntity.ok(response);
    }
}
