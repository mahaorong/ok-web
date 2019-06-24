package com.geek.okweb.domain;

import com.geek.okweb.utils.KeyUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Comment 评论实体
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id; //唯一标识

	@NotEmpty(message = "评论内容不能为空")
	@Size(min=2, max=500)
	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	@CreationTimestamp  // 由数据库自动创建时间
	@OrderBy(value = "DESC")
	private Timestamp createTime;


	@ManyToOne
	@JoinColumn(name = "blog_id",nullable = false)
	private Blog blog;

	/*@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid",referencedColumnName = "id")
	private List<Comment> commentList;*/

	@ManyToOne
	@JoinColumn(name = "user_id",nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "pid")
	private Comment comment;

	@OneToMany(mappedBy = "comment" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy(value = "createTime DESC")
	private List<Comment> comments;

	private Integer start = 0;

	//默认为未通过审核，不放到页面
	private String state = "NotPass";

	public void init(){
	  this.id= KeyUtil.UUID();
	}
	public Comment(@NotEmpty(message = "评论内容不能为空") @Size(min = 2, max = 500) String content, Blog blog) {
		this.content = content;
		this.blog = blog;
	}
	public Comment() {
	}


}
