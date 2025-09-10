package com.example.chat_service.exception;

public enum ErrolCode {
    USER_NO_RESPONSE(501, "Server not implemented"),
    USER_EXISTED(401, "User existed"),
    PASSWORD_ERROR(402, "Password must be between 8 and 16 characters"),
    PHONE_ERROR(403, "Phone number must be between 10 and 11 characters"),
    EMAIL_ERROR(404, "Not in the correct format abc@gmail.com"),
    USER_NOT_EXISTED(405, "User not existed"),
    UNAUTHENTICATED(406, "Unauthenticated"),
    ACCOUNT_NOT_APPROVED(406, "Account not approved"),
    INVALID_ROLE(407, "Invalid role for conversation"),
    INVALID_CHAT_PARTNERS(408, "Invalid chat partners"),
    MESSAGE_CAN_NOT_EMPTY(409, "Message cannot be empty"),
    MESSAGE_CAN_NOT_DELETE(410, "Message cannot be empty"),
    MESAGE_TOO_LONG(411, "Message too long"),
    MESSAGE_SENDER(412,"Sender and recipient IDs are required"),
    MESSAGE_NOT_SENT(413, "Message not sent"),
    MESSAGE_ARTIS_SENT(414, "Artists can only send messages to Admins"),
    MESSAGE_ADMIN_SENT(415, "Admins can only send messages to Admins"),
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
