package com.geek.okweb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Template {

    /*
    * id    模板id
	name	模板名字
	url  	模板地址
	List<T> data  模板数据   （主页解决方案文章模板 主页新品推广文章模板）
    * */
    @Id
    @Column(length = 32)
    private String id;

    //模板名称
    private String name;

    //栏目地址
    private String url;

    //模板地址
    private String path;

    private String keywords;  //关键词

    private String description; //描述

    private String title; //标题

    private Integer position; //栏目位置   0代表内部栏目 ， 1代表外部栏目

    private String language;  //en-US zh_CN zh_TW

    private Integer tmpGroup; // 0.公共部分 1.网站首页 2.关于我们 3.产品中心 4.方案设计 5.下载中心 6.技术支持 7.客户服务 8.联系我们 9.技术论坛

    //模板数据
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<TemplateData> data = new ArrayList<>();


}
