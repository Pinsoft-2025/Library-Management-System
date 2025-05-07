package io.github.SenaUstun_Dev.library_management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status, // HTTP Durum Kodu (örn: 404, 400, 500)
        String error,
        String message,
        String details,
        String path,
        LocalDateTime timestamp,
        List<ValidationError> validationErrors // Alan bazlı validasyon hataları için
) {
    // Validasyon hataları için iç içe sınıf veya record
    public record ValidationError(
            String field, // Hatanın olduğu alan adı
            String message
    ) {}
}
