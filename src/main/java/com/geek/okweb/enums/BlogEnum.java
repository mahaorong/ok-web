package com.geek.okweb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public enum BlogEnum {
    BLOG_NOT_FOUND(1,"博客不存在"),
    FORMITEM_NOT_FOUND(2,"表单项不存在"),
    VOTE_EXIST(3,"已点赞"),
    USER_NOT_FOUND(4,"用户未登陆或不存在"),
    FORMSTRUCTURE_NOT_FOUND(5,"表单结构不正确");

    private Integer code;
    private String message;
}
