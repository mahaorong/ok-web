package com.geek.okweb.exception;

import com.geek.okweb.enums.ExcelEnum;

public class ExcelException extends RuntimeException {
    private Integer code;

    public ExcelException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public ExcelException(ExcelEnum excelEnum) {
        super(excelEnum.getMessage());
        this.code = excelEnum.getCode();
    }
}
