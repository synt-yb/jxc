package com.example.demo.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.demo.entity.ResponseInfo;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 @RequestBody 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseInfo handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return buildValidationErrorResponse(ex.getBindingResult());
    }

    /**
     * 处理表单绑定参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseInfo handleBindException(BindException ex) {
        return buildValidationErrorResponse(ex.getBindingResult());
    }

    /**
     * 统一构建校验错误响应
     */
    private ResponseInfo buildValidationErrorResponse(BindingResult bindingResult) {
        String errorMessage = bindingResult.getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("参数校验失败");

        return ResponseInfo.error(400, errorMessage);
    }
}