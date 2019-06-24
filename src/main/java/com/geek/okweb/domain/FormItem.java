package com.geek.okweb.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 表单项实体
 */
@Entity
@Getter
@Setter
public class FormItem implements Serializable {

    private static final long serialVersionUID = -4664821884308807225L;
    @Id
    private String id;

    private String userId;  //用户id

    private String name;  //表单项名称

    private String type; //表单项类型

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> option;  //可选项(json)

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> result; //表单结果

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> verify = new ArrayList<>(); // 存放校验规则,不选默认无校验规则

    private Integer sort; //排序

    private String remarks;

    @Transient
    private Integer keyValue;

    @ManyToOne
    @JoinColumn(name = "form_id",nullable = false)
    private Form form;

    @ManyToOne
    @JoinColumn(name = "pid")
    private FormItem formItem;

    //自关联
    @OneToMany(mappedBy = "formItem",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<FormItem> formItems;

    /*创建时间*/
    @Column(nullable = false) // 值不能为空
    @CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createTime;
}
