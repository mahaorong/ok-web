package com.geek.okweb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
//网址对象
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
@Entity
public class Website {

    @Id
    private String id;

    private String url;  //网址url

    private String description; //网址描述

    private String type; //网址类型

    private Integer operateable; //网址是否可以操作  0(不可以) 1（可以）

}
