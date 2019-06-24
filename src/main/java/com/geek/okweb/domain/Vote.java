package com.geek.okweb.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 点赞 实体
 *
 */
@Entity // 实体
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vote implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id; //唯一标识

	@ManyToOne()
	@JoinColumn(name = "userId")
	private User user;

	/**
	 * 创建时间
	 */
	@Column(nullable = false) // 值不能为空
	@CreationTimestamp  // 由数据库自动创建时间
	private Timestamp createTime;

	@ManyToOne
	@JoinColumn(name = "blog_id",nullable = false)
	private Blog blog;

}
