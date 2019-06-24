package com.geek.okweb.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
public class TemplateForm {

    private String templateName;

    private String requestUrl;

    private String keywords;

    private String description;

    private Integer position;

    private String language;

    private Integer tmpGroup;
}
