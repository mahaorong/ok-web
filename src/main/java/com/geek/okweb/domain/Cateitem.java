package com.geek.okweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.geek.okweb.form.CheckCate;
import com.geek.okweb.service.CategoryService;
import com.geek.okweb.utils.ChineseCharacterUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Cateitem implements Serializable,Cloneable {

	@JsonIgnore
	private CategoryService categoryService;

	private static final long serialVersionUID = -7589676546571622474L;

	private String id;
	private String pid;
	private String tag;
	private String text;
	private String status; // 选中
	private CheckCate state;
	private String type = "test";
	private Boolean list = true;

	//TDK标签
	private String title;
	private String description;
	private String keywords;

	//超链接
	private String outlink;

//	private List<Cateitem> cateitems = new ArrayList<Cateitem>(0);
	private List<Cateitem> nodes = new ArrayList<Cateitem>(0);

	public void addCateitem(Cateitem cateitem) {
		this.nodes.add(cateitem);
	}

	public Cateitem(String id) {
		this.id = id;
	}

	public Cateitem(String id, String name) {
		this.id = id;
		this.text = name;
	}

	public Cateitem(String id, String pid, String name) {
		this.id = id;
		this.pid = pid;
		this.text = name;
	}

	public Cateitem(){ }

	@Override
	protected Cateitem clone() throws CloneNotSupportedException {
		Cateitem clone = null;
		try {
			clone = (Cateitem) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		return clone;
	}

	/**
	 * 获取首字母
	 * @return
	 */
	public String initials(String str){
		return ChineseCharacterUtil.getLowerCase(str, false);
	}

}
