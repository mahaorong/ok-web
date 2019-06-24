package com.geek.okweb.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryForm implements Serializable{

	private static final long serialVersionUID = 9078845108735783476L;

	@NotNull(message = "请填写分类名称")
	private String text;

	private String pid;
	
	private String id;

	//TDK标签
	private String title;
	private String description;
	private String keywords;

	//超链接
	private String outlink;
}
