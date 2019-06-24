package com.geek.okweb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
@Entity
public class Authority {

    @Id
    private String id;

    private String wid;  //网址id

    private Integer operateable; //用户是否有对这个网址的操作权限

    @ManyToOne
    @JoinColumn(name = "uid",nullable = false)
    private User user;
}
