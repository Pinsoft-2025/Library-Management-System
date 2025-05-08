package io.github.SenaUstun_Dev.library_management.controller;

import io.github.SenaUstun_Dev.library_management.dto.request.CreatePublisherRequest;
import io.github.SenaUstun_Dev.library_management.dto.request.UpdatePublisherRequest;
import io.github.SenaUstun_Dev.library_management.dto.response.PublisherResponse;
import io.github.SenaUstun_Dev.library_management.dto.response.ErrorResponse;
import io.github.SenaUstun_Dev.library_management.service.PublisherService;
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

@Tag(name = "Publisher Management",
        description = """
        This endpoint group manages publishers. Admin users have full access to create, update, and delete publishers.
        Public users can view publishers by ID and list all publishers.
        """)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @Operation(summary = "Create a new publisher (Admin)",
            description = "Creates a new publisher with a name. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Publisher created successfully",
                    content = @Content(schema = @Schema(implementation = PublisherResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload (validation error)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Publisher already exists (e.g., duplicate name)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/admin")
    public ResponseEntity<PublisherResponse> addPublisher(@Valid @RequestBody CreatePublisherRequest request) {
        PublisherResponse response = publisherService.addPublisher(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a publisher (Admin)",
            description = "Updates the details of an existing publisher by ID. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher updated successfully",
                    content = @Content(schema = @Schema(implementation = PublisherResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request payload (validation error)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Publisher not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/admin/{id}")
    public ResponseEntity<PublisherResponse> updatePublisher(
            @Parameter(description = "ID of the publisher to update")
            @PathVariable Long id,
            @Valid @RequestBody UpdatePublisherRequest request) {
        PublisherResponse response = publisherService.updatePublisher(request, id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a publisher (Admin)",
            description = "Deletes a publisher permanently by ID. Requires Admin role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Publisher deleted successfully (No Content)"),
            @ApiResponse(responseCode = "403", description = "Access denied (requires admin role)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Publisher not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deletePublisher(
            @Parameter(description = "ID of the publisher to delete")
            @PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get publisher details by ID (Public)",
            description = "Retrieves the full details of a publisher by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher found",
                    content = @Content(schema = @Schema(implementation = PublisherResponse.class))),
            @ApiResponse(responseCode = "404", description = "Publisher not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getPublisherById(
            @Parameter(description = "ID of the publisher to retrieve")
            @PathVariable Long id) {
        PublisherResponse response = publisherService.findPublisherById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all publishers (Public)",
            description = "Retrieves a list of all registered publishers.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publishers listed successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PublisherResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<PublisherResponse>> getAllPublishers() {
        List<PublisherResponse> response = publisherService.findAllPublishers();
        return ResponseEntity.ok(response);
    }
}
