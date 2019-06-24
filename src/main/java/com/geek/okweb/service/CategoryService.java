package com.geek.okweb.service;

import com.geek.okweb.dao.CategoryDao;
import com.geek.okweb.domain.Category;
import com.geek.okweb.domain.Cateitem;
import com.geek.okweb.form.CategoryForm;
import com.geek.okweb.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@Transactional
public class CategoryService {
	@Autowired
	private CategoryDao categoryDao;

	public Category findOne() {
		return categoryDao.findOne();
	}

	public Cateitem findByName(String name) {
		Category category = categoryDao.findOne();
		return category.findCateitemByName(name);
	}
	
	public Set<String> findIdsByName(String name) {
		Category category = categoryDao.findOne();
		return category.findIdsByName(name);
	}

	public void save(CategoryForm categoryForm) {
		Category category = categoryDao.findOne();
		if (null != category && StringUtils.isNotBlank(category.getId())) {
		} else {
			category = new Category();
			category.setId(KeyUtil.UUID());
			category.setName("分类");
			categoryDao.save(category);
		}
		Cateitem cateitem = new Cateitem();
		categoryForm.setId(KeyUtil.UUID());

		BeanUtils.copyProperties(categoryForm, cateitem);


		if (StringUtils.isNotBlank(categoryForm.getPid()))
			category.addCateitem(categoryForm.getPid(), cateitem);
		else
			category.addCateitem(cateitem);

		categoryDao.merge(category);
	}

	public void changeupload(CategoryForm categorysFrom) throws Exception {
		System.out.println(categorysFrom.toString());
		log.info("【分类ID】 = {}", categorysFrom.getId());
		log.info("【分类父亲ID】 = {}", categorysFrom.getPid());
		Category category = categoryDao.findOne();
		Cateitem cateitem = category.findCateitem(categorysFrom.getId());
		if (null != category && StringUtils.isNotBlank(category.getId())) {
			if (StringUtils.isNotBlank(categorysFrom.getText()))
				cateitem.setText(categorysFrom.getText());
			cateitem.setTitle(categorysFrom.getTitle());
			if (StringUtils.isNotBlank(categorysFrom.getDescription()))
				cateitem.setDescription(categorysFrom.getDescription());
			if (StringUtils.isNotBlank(categorysFrom.getKeywords()))
				cateitem.setKeywords(categorysFrom.getKeywords());
			cateitem.setOutlink(categorysFrom.getOutlink());

			categoryDao.merge(category);
		} else {
			category = new Category();
			category.setId(KeyUtil.UUID());
			category.setName("分类");
			categoryDao.save(category);
		}
		/*// 循环页面传过来的 list数据
		for (Cateitem ci : categorysFrom.getCateitem()) {
			category.setCateitemName(ci.getId(), ci.getText());
		}*/

	}

	public void deleteCategory(String id) {
		Category category = categoryDao.findOne();
		category.deleteCateitem(id);
		categoryDao.merge(category);
	}

}
