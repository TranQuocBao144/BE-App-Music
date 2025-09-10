package com.example.admin_service.exeption;

public class AppException extends RuntimeException {

    public AppException(ErrolCode errolCode) {
        super(errolCode.getMessage());
        this.errolCode = errolCode;
    }

    private ErrolCode errolCode;

    public ErrolCode getErrolCode() {
        return errolCode;
    }

    public void setErrolCode(ErrolCode errolCode) {
        this.errolCode = errolCode;
    }
}
