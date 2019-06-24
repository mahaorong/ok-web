package com.geek.okweb.domain;

import com.geek.okweb.utils.ChineseCharacterUtil;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Entity
@Table(name = "category")
public class Category {
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	private String id;
	private String name;

	@Type(type = "json")
	@Column(columnDefinition = "json")
	private List<Cateitem> cateitems = new ArrayList<Cateitem>(0);

	@Transient
	private List<String> ids = new ArrayList<>();


	public void addCateitem(Cateitem cateitem) {
		if (null != this.cateitems && this.cateitems.size() > 0) {
		} else
			this.cateitems = new ArrayList<Cateitem>();
		this.cateitems.add(cateitem);
	}

	public void addCateitem(String pid, Cateitem cateitem) {
		add(cateitems, pid, cateitem);
	}

	private void add(List<Cateitem> cateitems, String pid, Cateitem cateitem) {
		if (null != cateitems && cateitems.size() > 0) {
			for (Cateitem ci : cateitems) {
				if (null != ci && StringUtils.equals(ci.getId(), pid)) {
					cateitem.setPid(pid);
					ci.addCateitem(cateitem);
					return;
				} else {
					add(ci.getNodes(), pid, cateitem);
				}
			}
		}
	}
	
	//id
	public Cateitem findCateitem(String id) {
		if (null != cateitems && cateitems.size() > 0) {
			return findCateitem(cateitems, id);
		} else
			return null;
	}

	/**
	 * 根据ids查询分类
	 * @param id
	 * @return
	 */
	public List<Cateitem> findCateitemList(String id){
		tree = new ArrayList<>();
		try {
//			String id = ids.iterator().next();
			Cateitem cateitem = this.findCateitem(id);
			if (null != cateitem.getNodes() && cateitem.getNodes().size() > 0) {
				for (Cateitem ci : cateitem.getNodes()) {
					subtree(tree, ci, 1);
				}
			}
		} catch (Exception e) {

		}
		return tree;
	}
	@Transient
	private Cateitem cateitemResult =null;

	private Cateitem findCateitem(List<Cateitem> cateitems, String id) {
		for (Cateitem cateitem : cateitems) {
			if (null != cateitem && StringUtils.equals(cateitem.getId(), id)) {
				cateitemResult = cateitem;
				return cateitemResult;
			} else if (null != cateitem.getNodes()
					&& cateitem.getNodes().size() > 0) {
				findCateitem(cateitem.getNodes(), id);
			}
		}
		return cateitemResult;
	}
	
	//name
	public Cateitem findCateitemByName(String name) {
		if (null != cateitems && cateitems.size() > 0) {
			return findCateitemByName(cateitems, name);
		} else
			return null;
	}

	private Cateitem findCateitemByName(List<Cateitem> cateitems, String name) {
		for (Cateitem cateitem : cateitems) {
			if (null != cateitem && StringUtils.equals(cateitem.getText(), name)) {
				return cateitem;
			} else if (null != cateitem.getNodes()
					&& cateitem.getNodes().size() > 0) {
				findCateitem(cateitem.getNodes(), name);
			}
		}
		return null;
	}

	/*
	分类名称首字母进行查找
	 */
	public void findCateitemByLowName(String name) {
		if (null != cateitems && cateitems.size() > 0) {
//			return findCateitemByLowName(cateitems, name);
			 findCateitemByLowName(cateitems, name);
		} /*else
			return null;*/
	}


	/*
	分类名称首字母进行查找
	 */
	private void findCateitemByLowName(List<Cateitem> cateitems, String name) {
		for (Cateitem cateitem : cateitems) {
			try {
//				log.info("【转换】={}", URLDecoder.decode(name, "UTF-8"));
				name = URLDecoder.decode(name, "UTF-8");
			}catch (Exception e){
				log.error("【错误】");
			}
//			log.info("【分类首字符】={}, = {}",ChineseCharacterUtil.getLowerCase(cateitem.getText(),false), name);
			if (null != cateitem && StringUtils.equals(ChineseCharacterUtil.getLowerCase(cateitem.getText(),false), name)) {
				ids.add(cateitem.getId());
			} else if (null != cateitem.getNodes()
					&& cateitem.getNodes().size() > 0) {
				findCateitemByLowName(cateitem.getNodes(), name);
			}
		}
//		return null;
	}

	//name by id
	public Set<String> findIdsByName(String name) {
		Cateitem root = findCateitemByName(name);
		Set<String> ids = new HashSet<String>();
		ids.add(root.getId());
		if (null != root.getNodes() && root.getNodes().size() > 0) {
			for (Cateitem cateitem : root.getNodes()) {
				
				findIdsByName(ids, cateitem, cateitem.getText());
			}
		}
		return ids;
	}

	//name by id
	public List<String> findIdsByLowName(String name) {
//		Set<String> ids = new HashSet<String>();
		findCateitemByLowName(name);
//		ids.add(root.getId());
		/*if (null != root.getNodes() && root.getNodes().size() > 0) {
			for (Cateitem cateitem : root.getNodes()) {

				findIdsByLowName(ids, cateitem, cateitem.getText());
			}
		}*/
		return ids;
	}

	private Set<String> findIdsByLowName(Set<String> ids, Cateitem cateitem,
									  String name) {
		if (null != cateitem && StringUtils.equals(cateitem.getText(), name)) {
			ids.add(cateitem.getId());
		}
		if (null != cateitem.getNodes()
				&& cateitem.getNodes().size() > 0) {
			for (Cateitem ci : cateitem.getNodes()) {
				findIdsByLowName(ids, ci, name);
			}
		}
		return ids;

	}
	
	private Set<String> findIdsByName(Set<String> ids, Cateitem cateitem,
			String name) {
		if (null != cateitem && StringUtils.equals(cateitem.getText(), name)) {
			ids.add(cateitem.getId());
		}
		if (null != cateitem.getNodes()
				&& cateitem.getNodes().size() > 0) {
			for (Cateitem ci : cateitem.getNodes()) {
				findIdsByName(ids, ci, name);
			}
		}
		return ids;

	}

	public void deleteCateitem(String id) {
		if (null != cateitems && cateitems.size() > 0) {
			deleteCateitem(cateitems, id);
		}
	}

	private void deleteCateitem(List<Cateitem> cateitems, String id) {
		for (Cateitem cateitem : cateitems) {
			if (null != cateitem && StringUtils.equals(cateitem.getId(), id)) {
				cateitems.remove(cateitem);
				return;
			} else if (null != cateitem.getNodes()
					&& cateitem.getNodes().size() > 0) {
				deleteCateitem(cateitem.getNodes(), id);
			}
		}
	}

	public void setCateitemName(String id, String name) {
		if (null != cateitems && cateitems.size() > 0) {
			setCateitemSubName(cateitems, id, name);
		}
	}

	private void setCateitemSubName(List<Cateitem> cateitems, String id,
			String name) {
		for (Cateitem cateitem : cateitems) {
			if (null != cateitem && StringUtils.equals(cateitem.getId(), id)) {
				cateitem.setText(name);
				return;
			} else if (null != cateitem.getNodes()
					&& cateitem.getNodes().size() > 0) {
				setCateitemSubName(cateitem.getNodes(), id, name);
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
		// System.out.println("level:" + level);
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

}
