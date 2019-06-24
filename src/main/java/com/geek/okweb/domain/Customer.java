package com.geek.okweb.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Customer {

    @Id
    private String id;

    private String tencent1;

    private String tencent2;

    private String wechat;

    private String alibaba;

    private String email;

    private String phone;

    private String company = "英锐恩科技有限公司";
}
