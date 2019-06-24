package com.geek.okweb.form;

import lombok.Data;

import java.util.List;

@Data
public class FormItemData {

    private String formId;

    /**
     * 接受文本类型的数据,例如文本,文本域,下拉列表
     */
    private String[] id;
    private List< String> result;

    /**
     * 接受多选的数据
     */
    private String[] ids;
    private List<String> checkbox[];

    /**
     * 接受单选的数据
     */
    private String[] radioId;
    private List<String> radio[];

    String inputCode;
    String codeToUp;

}
