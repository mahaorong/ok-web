package com.geek.okweb.form;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表单项表(接受前端数据)
 */
@Data
public class FormItemForm implements Serializable {
    //id
    private String id;
    //表单项名称
    private String name;
    //排序
    private String sort;
    //表单项类型
    private String type;
    //所属表单
    //@NotBlank(message = "所属表单不能为空")
    private String form;
    //下拉列表,单选,多选内容
    private List<String> option;
    //默认
    private List<String> defaultOption;

    private String operating;

    private String flag;

    private String remarks;
}
