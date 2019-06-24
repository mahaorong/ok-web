package com.geek.okweb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@ControllerAdvice
public class BlogAdvice {


    /**
     * bean校验未通过异常
     * @param be
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BindException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> validExceptionHandler(BindException be){

        Map<String, Object> map = new HashMap<String, Object>();
        List<FieldError> fieldErrors = be.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            log.info(fieldError.getField()+"======================"+fieldError.getDefaultMessage());
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return map;
    }


    @ExceptionHandler(FileNotFoundException.class)
    public String validExceptionHandler(FileNotFoundException exception){
        return "error";
    }
}
