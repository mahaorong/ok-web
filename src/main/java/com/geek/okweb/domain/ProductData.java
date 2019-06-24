package com.geek.okweb.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * Create by Gai on 2018/12/11 20:23
 */
@Data
public class ProductData implements Serializable {

    private String id;

    private String proauctName;

    private String productMsg;
}
