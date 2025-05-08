package io.github.SenaUstun_Dev.library_management.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    private final HttpStatus status;
    private final String details;

    // Temel constructor: Sadece durum ve mesaj ile
    public BaseException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.details = null;
    }

    // Detaylı constructor: Durum, mesaj ve detaylar ile
    public BaseException(HttpStatus status, String message, String details) {
        super(message);
        this.status = status;
        this.details = details;
    }

    // ErrorMessages enum'unu kullanarak constructor (daha temiz kullanım için)
    public BaseException(HttpStatus status, ErrorMessages errorMessage) {
        super(errorMessage.getMessage());
        this.status = status;
        this.details = null;
    }

    public BaseException(HttpStatus status, ErrorMessages errorMessage, String details) {
        super(errorMessage.getMessage());
        this.status = status;
        this.details = details;
    }
}

