package com.example.demo.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.demo.entity.ResponseInfo;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseInfo handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        BindingResult bindingResult = ex.getBindingResult();
//        Map<String, String> errors = new HashMap<>();
//        for (FieldError fieldError : bindingResult.getFieldErrors()) {
//            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//        // 构建错误信息，将所有字段的错误信息拼接起来
//        StringBuilder errorMessage = new StringBuilder();
//        for (Map.Entry<String, String> entry : errors.entrySet()) {
//            errorMessage.append(entry.getKey()).append(": ").append(entry.getValue()).append("; ");
//        }
//        if (errorMessage.length() > 0) {
//            // 去掉最后的分号和空格
//            errorMessage.delete(errorMessage.length() - 2, errorMessage.length());
//        }
//        return ResponseInfo.error(400, errorMessage.toString());
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        // 获取第一个字段的错误信息
        String firstFieldError = errors.entrySet().iterator().next().getValue();
        String errorMessage = String.format(firstFieldError);
        return ResponseInfo.error(400, errorMessage);
    }
}