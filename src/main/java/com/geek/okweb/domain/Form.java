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
import java.util.Set;

/**
 * 表单实体
 */
@Entity
@Getter
@Setter
public class Form implements Serializable {

    @Id
    private String id;

    private String name;  //表单名称

    /**
     * 如果没有用户id，则是模板，有则是用户提交的数据
     */
    private String userId; //用户id

    private Integer status = 0; //判断是否放入回收站  0--正常状态  1---回收站状态

    private Integer shape = 0 ;//判断该表单是未处理还是已处理还是异常   0---未处理表单  1---已处理表单  2---异常表单  3--页面表单

    private String language = "zh_CN";

    @ManyToOne
    @JoinColumn(name = "pid")
    private Form form;

    @OneToMany(mappedBy = "form",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Form> forms;

    //一个表单里有多个表单项
    @OneToMany(mappedBy = "form",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @OrderBy("sort")
    private Set<FormItem> formItems;

    /**
     * 创建时间
     */
    @Column(nullable = false) // 值不能为空
    @CreationTimestamp  // 由数据库自动创建时间
    private Timestamp createTime;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> cateids = new ArrayList<String>();

}
