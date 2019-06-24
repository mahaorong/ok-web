package com.geek.okweb.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geek.okweb.form.CheckCate;
import com.geek.okweb.form.DiffArticle;
import com.geek.okweb.utils.ChineseCharacterUtil;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Blog 博客实体
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Blog implements Serializable {

	private static final long serialVersionUID = -4664821874308807225L;

	@Id
	@Column(name = "blog_id")
	private String id;
	
	@NotBlank(message = "标题不能为空")
	@Column(nullable = false)
	private String title;
	
	/*@NotBlank(message = "摘要不能为空")
	@Size(min=2, max=300)
	@Column(nullable = false)*/
	private String summary;

	@Lob  // 大文本对象，映射数据库 的 Long Text 类型
	@NotBlank(message = "内容不能为空")
	@Size(min=10,message = "不小于10位")
	@Column(nullable = false)
	private String content;
	
	@Lob  // 大文本对象，映射数据库 的 Long Text 类型
	//	//@Size(min=10,message = "不小于10位")
	@Column(nullable = false)
	private String htmlContent; // 将 md 转为 html
	/**
	 * 创建时间
	 */
	@JSONField(format="yyyy-MM-dd")
	@Column(nullable = false)
	//@CreationTimestamp  // 由数据库自动创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createTime;

	private Integer readCount = 0; // 访问量、阅读量

	private Integer commentCount = 0;  // 评论量

	private String tags; //标签

	private Integer voteCount=0; //点赞量

	private Integer status = 0; //状态。0代表未放入回收站，1代表放入回收站

	private Integer review = 0; //审核  0代表未审核，1代表已审核通过,2代表审核未通过

	private Integer isTop = 0; //是否置顶   0代表不置顶，1代表置顶

	private String keywords;  //关键词

	private String description; //描述

	private String cateName;  //分类名称

	private String language = "zh_CN";  //语言 en-US zh_CN zh_TW

	private Integer isHidden = 0;  // 默认 0显示，1隐藏

	private String sort = "desc"; //排序 asc 升序 desc 降序

	private Integer number;

	@Type(type = "json")
	@Column(columnDefinition = "json")
	private List<DiffArticle> diffLangArticles = new ArrayList<>();

	@NotEmpty(message = "请选择文章栏目")
	@Type(type = "json")
	@Column(columnDefinition = "json")
	private List<String> cateids = new ArrayList<String>();

	/**
	 * 一篇博客可以有多个评论
	 */
	@OneToMany(mappedBy = "blog",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy(value = "createTime DESC")
	private Set<Comment> comments = new HashSet<Comment>();

	@ManyToOne
	@JoinColumn(name = "user_id",nullable = false)
	private User user;


	/**
	 * 一篇博客可以有多个点赞
	 */
	@OneToMany(mappedBy = "blog",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Vote> votes;

	/*@Column(nullable = false)*/
	private String imageUrl;

	@Override
	public String toString() {
		return "id="+id+",title="+title+",summary="+summary;
	}

	@Transient
	private List<Cateitem> cateitems = new ArrayList<Cateitem>();

	public void setCateitems(String json) {
		ObjectMapper mapper = new ObjectMapper();
		JavaType type = mapper.getTypeFactory().constructCollectionType(
				ArrayList.class, Cateitem.class);
		try {
			this.cateitems =  mapper.readValue(json, type);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (null != this.cateitems && this.cateitems.size() > 0
				&& null != cateids && cateids.size() > 0) {
			for (String itemid : cateids) {
				setStatusCateitem(this.cateitems, itemid);
			}
		}
	}

	private void setStatusCateitem(List<Cateitem> cateitems, String itemid) {
		for (Cateitem cateitem : cateitems) {
			if (null != cateitem
					&& StringUtils.equals(cateitem.getId(), itemid)) {
				cateitem.setState(new CheckCate(true));
				return;
			} else if (null != cateitem.getNodes()
					&& cateitem.getNodes().size() > 0) {
				setStatusCateitem(cateitem.getNodes(), itemid);
			}
		}
	}

	@Transient
	private List<Cateitem> tree = new ArrayList<Cateitem>();

	public List<Cateitem> getTree() {
		tree = new ArrayList<Cateitem>();
		if (null != this.getCateitems() && this.getCateitems().size() > 0) {
			for (Cateitem ci : this.getCateitems()) {
				subtree(tree, ci, 1);
			}
		}
		return tree;
	}

	public void setTree(List<Cateitem> tree) {
		this.tree = tree;
	}

	private List<Cateitem> subtree(List<Cateitem> tree, Cateitem cateitem,
								   Integer level) {

		String str = "";
		for (int i = 1; i <= level; i++) {
			str += "-";
		}
		cateitem.setTag(str);
		tree.add(cateitem);
		if (null != cateitem.getNodes()
				&& cateitem.getNodes().size() > 0) {
			for (Cateitem ci : cateitem.getNodes()) {
				subtree(tree, ci, level + 1);
			}
		}
		return tree;
	}


	public String getCategoryName() {
		return ChineseCharacterUtil.getLowerCase(cateName, false);
	}

	/**
	 * 获取首字母
	 * @return
	 */
	public String initials(String str){
		return ChineseCharacterUtil.getLowerCase(str, false);
	}

}
