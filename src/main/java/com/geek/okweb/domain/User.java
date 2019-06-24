package com.geek.okweb.domain;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements Serializable {

	@Id
	@Column(name = "user_id")
//	@NotBlank(message = "账号不能为空")
	private String id;

	@NotBlank(message = "用户名不能为空")
	@Size(min = 5,max = 16)
	@Column(nullable = false)
	private String username;

	@NotBlank(message = "密码不能为空")
//	@Size(min = 6,max = 15)
	@Pattern(regexp = "(?!^\\\\\\\\d+$)(?!^[a-zA-Z]+$).{6,}",message = "密码格式不正确")
	@Column(nullable = false,length = 255)
	private String password;

	private Integer usable; //是否可用  0 可用  1不可用


	/**
	 *
	 * 一个用户可以有多个博客
	 */
	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Blog> blogs = new HashSet<Blog>();


	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private Set<Authority> authorities = new HashSet<>();


}
