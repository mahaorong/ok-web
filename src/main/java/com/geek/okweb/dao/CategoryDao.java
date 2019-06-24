package com.geek.okweb.dao;

import com.geek.okweb.domain.Category;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component("categoryDao")
@Transactional
public class CategoryDao extends BaseDao<Category> {
	
	public void detach(Category category) {
		log.debug("detach article instance");
		try {
			getSession().detach(category);
			log.debug("detach category successful");
		} catch (RuntimeException re) {
			log.error("detach category failed", re);
			throw re;
		}
	}

	public void save(Category category) {
		log.debug("saving article instance");
		try {
			getSession().save(category);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public Category findOne() {
		DetachedCriteria dc = DetachedCriteria.forClass(Category.class);
		Criteria criteria = dc.getExecutableCriteria(getSession());
		List<Category> list = criteria.list();
		if (null != list && list.size() > 0)
			return list.get(0);
		return null;
	}
	
	
	public Category merge(Category instance) {
		log.debug("merging User instance");
		try {
			Category result = (Category) getSession().merge(instance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	
	public List<Category> findByCategoryId(String id) {

		DetachedCriteria dc = DetachedCriteria.forClass(Category.class);
		Disjunction dis = Restrictions.disjunction();
		dis.add(Property.forName("category.id").eq(id));
		dc.add(dis);
		List<Category> list = findAllByCriteria(dc);
		return list;

	}
	
	

	
	public Category findById(String id) {
		return getSession().get(Category.class, id);
	}
	
	public void DelectCategory(String id) {
		Category category = findById(id);
		getSession().delete(category);

	}
	
	

}
