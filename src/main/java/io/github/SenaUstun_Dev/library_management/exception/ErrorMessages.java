package io.github.SenaUstun_Dev.library_management.exception;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    PRODUCT_NOT_FOUND("Product Not Found"); //örnektir, hata vermesin diye silinmemiştir

    private final String message;

    private ErrorMessages(String message) {
        this.message = message;
    }

}
