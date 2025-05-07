package io.github.SenaUstun_Dev.library_management.config;

import io.github.SenaUstun_Dev.library_management.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e, WebRequest request) {
        // BaseException'dan gelen status bilgisini kullanarak HTTP durumunu al
        HttpStatus status = e.getStatus() != null ? e.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR; // Eğer status null ise varsayılan 500

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                e.getMessage(),
                e.getDetails(),
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                LocalDateTime.now(),
                null // Bu handler genellikle alan bazlı validasyon hataları için kullanılmaz, null kalabilir
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        // Validasyon hatalarını al ve ErrorResponse.ValidationError listesine dönüştür
        List<ErrorResponse.ValidationError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorResponse.ValidationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        HttpStatus status = HttpStatus.BAD_REQUEST; // Validasyon hataları genellikle 400 Bad Request'tir
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                "Validasyon hatası oluştu.", // Validasyon hataları için genel mesaj
                null, // Detay alanı bu senaryoda kullanılmayabilir
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                LocalDateTime.now(),
                errors
        );

        return new ResponseEntity<>(errorResponse, status);
    }

    // Genel RuntimeException veya diğer beklenmedik hatalar için (fallback) handler buraya yazılacak.
    // Bu handler beklenmeyen hataları yakalar ve genel bir 500 Internal Server Error döner.
    // Loglama yapmak bu handler için çok önemlidir.

    // İhtiyaç duyuldukça diğer özel exception handler metodları buraya eklenecek.
    // Örneğin: Güvenlik hataları, veritabanı hataları vb.
}
