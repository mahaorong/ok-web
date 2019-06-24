package com.geek.okweb.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by Gai on 2018/12/13 21:14
 */
@Entity
@Slf4j
@Data
public class Worktable {
    @Id
    private String id;

    private String fileName;

    private Integer status = 0; //状态。0代表未放入回收站，1代表放入回收站

    @UpdateTimestamp
    private Date updateTime;

    @Column(nullable = false) // 值不能为空
    @CreationTimestamp  // 由数据库自动创建时间
    private Date createTime;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> cateids = new ArrayList<String>();
}
