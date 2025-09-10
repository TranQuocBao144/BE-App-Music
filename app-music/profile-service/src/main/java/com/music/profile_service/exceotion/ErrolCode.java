package com.music.profile_service.exceotion;

import lombok.Getter;

@Getter
public enum ErrolCode {
    USER_NO_RESPONSE(501, "Server not implemented"),
    USER_EXISTED(401, "User existed"),
    PASSWORD_ERROR(402, "Password must be between 8 and 16 characters"),
    PHONE_ERROR(403, "Phone number must be between 10 and 11 characters"),
    EMAIL_ERROR(404, "Not in the correct format abc@gmail.com"),
    USER_NOT_EXISTED(405, "User not existed"),
    UNAUTHENTICATED(406, "Unauthenticated"),
    ACCOUNT_NOT_APPROVED(406, "Account not approved"),
    ;

    private int code;
    private String message;

    ErrolCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
