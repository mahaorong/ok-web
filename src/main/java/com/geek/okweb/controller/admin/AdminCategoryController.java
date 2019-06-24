package com.geek.okweb.controller.admin;

import com.geek.okweb.dao.CategoryDao;
import com.geek.okweb.domain.Category;
import com.geek.okweb.domain.Cateitem;
import com.geek.okweb.form.CategoryForm;
import com.geek.okweb.service.CategoryService;
import com.geek.okweb.utils.KeyUtil;
import com.geek.okweb.utils.Result;
import com.geek.okweb.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
@Slf4j
@Controller
public class AdminCategoryController {
	@Resource
	private CategoryDao categoryDao;

	@Autowired
	private CategoryService categoryService;

	@ResponseBody
	@PostMapping("/admin/create_category")
	public Result create_category(@Valid CategoryForm cateForm) {
		Category category = categoryService.findOne();
		if (category != null) {
			Cateitem result = category.findCateitemByName(cateForm.getText());
			if (result != null) {
				return ResultUtil.fail();
			}
		}
		categoryService.save(cateForm);
		return ResultUtil.success(cateForm);
	}

	
	@RequestMapping("/admin/categorys")
	public String categorys(HttpSession httpSession, Model model) {
		Category category = categoryDao.findOne();
		if (null != category && StringUtils.isNotBlank(category.getId())) {
		} else {
			category = new Category();
			category.setId(KeyUtil.UUID());
			category.setName("分类");
			categoryDao.save(category);
		}
		model.addAttribute("category", category);
	
		return "admin/new_web/category";
	}
	@ResponseBody
	@RequestMapping(value = "/admin/category/change", method = RequestMethod.POST)
	public String changeCategory(CategoryForm categoryForm) throws Exception {
		categoryService.changeupload(categoryForm);
		log.info(categoryForm.toString());
//		return new ModelAndView("redirect:/admin/categorys");
		return "success";
	}

	@ResponseBody
	@GetMapping("/admin/deletecategory")
	public String deleteCategory(@RequestParam("id") String id) {
		log.info("进入=======");
		System.out.print("============================================"+id);
		categoryService.deleteCategory(id);
		return "success";
	}

	@ResponseBody
	@GetMapping("/admin/findCategoryName")
	public Cateitem findCategoryName(@RequestParam("id") String id) {
		Category category = categoryService.findOne();
		Cateitem cateitem = category.findCateitem(id);
		return cateitem;
	}
}
	

