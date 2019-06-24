package com.geek.okweb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Image {

    @Id
    private String id;

    private String imageName;

    private String imageUrl;

    private String hrefUrl;

    private String description;

    private Integer status = 0;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> cateids = new ArrayList<String>();

    @CreationTimestamp
    private Date createTime;

}
