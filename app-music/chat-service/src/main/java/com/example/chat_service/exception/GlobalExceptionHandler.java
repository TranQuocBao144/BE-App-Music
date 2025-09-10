package com.example.chat_service.exception;


import com.example.chat_service.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException exception) {
        ApiResponse apiRespone = new ApiResponse();
        apiRespone.setCode(ErrolCode.USER_NO_RESPONSE.getCode());
        apiRespone.setMessage(ErrolCode.USER_NO_RESPONSE.getMessage());
        return ResponseEntity.badRequest().body(apiRespone);
    }



    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrolCode errolCode = exception.getErrolCode();
        ApiResponse apiRespone = new ApiResponse();
        apiRespone.setCode(errolCode.getCode());
        apiRespone.setMessage(errolCode.getMessage());
        return ResponseEntity.badRequest().body(apiRespone);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrolCode errolCode = ErrolCode.valueOf(enumKey);

        ApiResponse apiRespone = new ApiResponse();
        apiRespone.setCode(errolCode.getCode());
        apiRespone.setMessage(errolCode.getMessage());


        return ResponseEntity.badRequest().body(apiRespone);
    }
}