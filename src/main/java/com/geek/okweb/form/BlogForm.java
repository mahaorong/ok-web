package com.geek.okweb.form;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.sql.Timestamp;

@Slf4j
@Data
public class BlogForm implements Serializable {

    private static final long serialVersionUID = -4665821874308807225L;

    private String id;

    private String title;

    private String summary;

    private String content;

    private String htmlContent; // 将 md 转为 html

    private Timestamp createTime;

    private Integer readCount = 0; // 访问量、阅读量

    private String tags;  //标签





}
