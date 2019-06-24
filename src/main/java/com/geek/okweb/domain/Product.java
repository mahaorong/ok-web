package com.geek.okweb.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by Gai on 2018/12/11 09:04
 */
@Entity
@Data
public class Product implements Serializable {

    private static final long serialVersionUID = -4128256417072333012L;
    @Id
    private String id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    @CreationTimestamp  // 由数据库自动创建时间
    private Date createTime;

    private String blogId;

    /**
     * 引用案例标签
     */
    private String tag;

    /**
     * 修改时间
     */
    @Column(nullable = false)
    @UpdateTimestamp
    private Date updateTime;

    @Transient
    private String type = "pdf";

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<ProductData> json;

    private String wid;

    private String language = "zh_CN";

    private String articleTitle;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> imgs = new ArrayList<>(); //轮播图片

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> devImgs = new ArrayList<>(); //研发工具图片

    /*
      多个下载地址
     */
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> files = new ArrayList<>();



    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> cateids = new ArrayList<String>();

}
