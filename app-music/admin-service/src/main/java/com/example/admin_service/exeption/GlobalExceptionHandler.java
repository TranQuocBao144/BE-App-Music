package com.example.admin_service.exeption;

import com.example.admin_service.dto.ApiRespone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiRespone> handleRuntimeException(RuntimeException exception) {
        ApiRespone apiRespone = new ApiRespone();
        apiRespone.setCode(ErrolCode.USER_NO_RESPONSE.getCode());
        apiRespone.setMessage(ErrolCode.USER_NO_RESPONSE.getMessage());
        return ResponseEntity.badRequest().body(apiRespone);
    }



    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiRespone> handleAppException(AppException exception) {
        ErrolCode errolCode = exception.getErrolCode();
        ApiRespone apiRespone = new ApiRespone();
        apiRespone.setCode(errolCode.getCode());
        apiRespone.setMessage(errolCode.getMessage());
        return ResponseEntity.badRequest().body(apiRespone);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiRespone> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrolCode errolCode = ErrolCode.valueOf(enumKey);

        ApiRespone apiRespone = new ApiRespone();
        apiRespone.setCode(errolCode.getCode());
        apiRespone.setMessage(errolCode.getMessage());


        return ResponseEntity.badRequest().body(apiRespone);
    }
}
