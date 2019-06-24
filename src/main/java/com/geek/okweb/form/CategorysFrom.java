package com.geek.okweb.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategorysFrom implements Serializable{
	private static final long serialVersionUID = 3300953501870906592L;
	//	private List<Cateitem> cateitem = new ArrayList<Cateitem>();
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
