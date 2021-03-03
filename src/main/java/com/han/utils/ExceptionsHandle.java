package com.han.utils;

import com.han.dto.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandle {

    @ExceptionHandler(Exception.class)
    public Result exceptionHandle(){
        return new Result("10001",false,"系统繁忙，请稍后再试！");
    }
    
}
