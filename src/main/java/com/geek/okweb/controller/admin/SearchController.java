package com.geek.okweb.controller.admin;

import com.geek.okweb.domain.*;
import com.geek.okweb.service.*;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WorktableService worktableService;

    @Autowired
    private TemplateService templateService;

    /**
     * 搜索
     *
     * @param page     当前页
     * @param pageSize 每页显示的数量
     * @param type     返回地址
     * @param keyword  搜索关键字
     * @return
     */
    @GetMapping("/searchAll")
    public String Search(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                         @RequestParam String type, @RequestParam String keyword,
                         @RequestParam(required = false) String id,
                         @RequestParam(name = "templateId",required = false)String templateId,
                         @RequestParam(name = "key",required = false) String key,Model model) {

        log.info("type={},search={},id={}", type, keyword, id);
        //初始化变量
        MyPage myPage = new MyPage(0, 0, 0);
        List list = new ArrayList();
        //判断是否需要分页
        if ("productFileData".equals(type)) {
            list = (List) searchService.search(null, null, type, keyword, id);
        } else {
            myPage = (MyPage) searchService.search(page, pageSize, type, keyword, id);
        }

        //根据跳转地址，选择不同的传输数据
        if ("article_review".equals(type) || "article_refust".equals(type) || "article_adopt".equals(type) || "article_all".equals(type) || "manageBlog".equals(type) || "recycle_article".equals(type) || "addProductBlog".equals(type)) {
            model.addAttribute("blogPage", myPage);
        } else if ("form_admin".equals(type) || "form_untreated".equals(type) || "form_all".equals(type) || "form_processed".equals(type) || "form_abnormal".equals(type) || "recycle_form".equals(type)) {
            model.addAttribute("formPage", myPage);
        } else if ("recycle_image".equals(type) || "image_admin".equals(type)) {
            model.addAttribute("imagePage", myPage);
        } else if ("product_form".equals(type) || "recycle_product".equals(type)) {
            model.addAttribute("workPage", myPage);
        } else if ("file_management".equals(type) || "recycle_file".equals(type) || "addProductFile".equals(type)) {
            model.addAttribute("filePage", myPage);
        } else if ("productDetail".equals(type)) {
            List<String> productName = productService.findProductName(myPage);
            model.addAttribute("productName", productName);
            model.addAttribute("proPage", myPage);
        } else if ("model_dataType_article".equals(type)) {
            Template template = templateService.findById(templateId);
            for (TemplateData data : template.getData()){
                if (StringUtils.equals(key,data.getKey())){
                    Set<String> ids = data.getIds();
                    model.addAttribute("ids",ids);
                    model.addAttribute("td",data);
                }
            }
            model.addAttribute("pages", myPage);
            model.addAttribute("templateId", templateId);
            model.addAttribute("key", key);
        }
        Category category = categoryService.findOne();
        model.addAttribute("category", category);
        if (StringUtils.isNotBlank(id)) {
            Product product = productService.findById(id);
            if (product != null) {
                Worktable worktable = worktableService.findById(product.getWid());
                model.addAttribute("product", product);
                model.addAttribute("worktable", worktable);
                model.addAttribute("wid", worktable.getId());
            }
        }

        //放需要回显的数据
        model.addAttribute("keyword", keyword);
        model.addAttribute("type", type);
        //搜索全选
        if ("article_adopt".equals(type)) {
            MyPage<Blog> myPage1 = myPage;
            List<String> ids = myPage1.getItems().stream().map(Blog::getId).collect(Collectors.toList());
            model.addAttribute("articleIds", ids);
        }
        model.addAttribute("id", id);
        return "admin/new_web/" + type;

    }
}
