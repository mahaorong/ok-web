package com.geek.okweb.exception;

import com.geek.okweb.enums.BlogEnum;

public class BlogException extends RuntimeException {

    private Integer code;


    public BlogException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BlogException(BlogEnum blogEnum) {
        super(blogEnum.getMessage());
        this.code = blogEnum.getCode();
    }


}
