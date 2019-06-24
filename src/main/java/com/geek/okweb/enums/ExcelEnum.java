package com.geek.okweb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExcelEnum {

    EXCEL_NOT_FOUND(1, "excel不存在"),
    EXCEL_INCORRECT_FORMAT(2, "excel格式不正确"),
    EXCEL_DATA_NOT_FOUND(3, "没有数据，请仔细检查"),
    ;

    private Integer code;
    private String message;
}
