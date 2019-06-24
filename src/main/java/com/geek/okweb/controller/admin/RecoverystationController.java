package com.geek.okweb.controller.admin;

import com.geek.okweb.domain.*;
import com.geek.okweb.service.CategoryService;
import com.geek.okweb.service.RecoverystationService;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/recovery")
public class RecoverystationController {
    @Autowired
    private RecoverystationService recoverystationService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/findAll")
    public String findAll(@RequestParam(value = "id", required = false) String id, @RequestParam("type") String type, Model model,
                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "pageSize", required = false, defaultValue = "5") Integer pageSize) {

        if ("recycle_article".equals(type)) {
            MyPage<Blog> blogPage = null;
            if (StringUtils.isNotBlank(id)) {
                Set<String> ids = new HashSet<>();
                ids.add(id);
                blogPage = recoverystationService.findByCategoryArticle(ids, page, pageSize);
                model.addAttribute("id", id);
            } else {
                blogPage = recoverystationService.findAllByArticle(1, page, pageSize);
            }
            List<String> blogIds = blogPage.getItems().stream().map(Blog::getId).collect(Collectors.toList());
            model.addAttribute("blogIds", blogIds);
            model.addAttribute("blogPage", blogPage);
        } else if ("recycle_image".equals(type)) {
            MyPage<Image> imagePage = null;
            if (StringUtils.isNotBlank(id)) {
                Set<String> ids = new HashSet<>();
                ids.add(id);
                imagePage = recoverystationService.findByCategoryImage(ids, page, pageSize);
                model.addAttribute("id", id);
            } else {
                imagePage = recoverystationService.findAllByImage(1, page, pageSize);
            }
            List<String> imageIds = imagePage.getItems().stream().map(Image::getId).collect(Collectors.toList());
            model.addAttribute("imageIds", imageIds);
            model.addAttribute("imagePage", imagePage);
        } else if ("recycle_form".equals(type)) {
            MyPage<Form> formPage = recoverystationService.findAllByForm(1, page, pageSize);
            List<String> formIds = formPage.getItems().stream().map(Form::getId).collect(Collectors.toList());
            model.addAttribute("formIds", formIds);
            model.addAttribute("formPage", formPage);
        } else if ("recycle_file".equals(type)) {
            MyPage<FileUpload> filePage = null;
            if (StringUtils.isNotBlank(id)) {
                Set<String> ids = new HashSet<>();
                ids.add(id);
                filePage = recoverystationService.findByCategoryFile(ids, page, pageSize);
                model.addAttribute("id", id);
            } else {
                filePage = recoverystationService.findAllByFile(1, page, pageSize);
            }
            List<String> fileIds = filePage.getItems().stream().map(FileUpload::getId).collect(Collectors.toList());
            model.addAttribute("fileIds", fileIds);
            model.addAttribute("filePage", filePage);
        } else if ("recycle_product".equals(type)) {
            MyPage<Worktable> workPage = null;
            if (StringUtils.isNotBlank(id)) {
                Set<String> ids = new HashSet<>();
                ids.add(id);
                workPage = recoverystationService.findByCategoryWork(ids, page, pageSize);
                model.addAttribute("id", id);
            } else {
                workPage = recoverystationService.findAllByWork(1, page, pageSize);
            }
            List<String> workIds = workPage.getItems().stream().map(Worktable::getId).collect(Collectors.toList());
            model.addAttribute("workPage", workPage);
            model.addAttribute("workIds", workIds);
        }
        Category category = categoryService.findOne();
        model.addAttribute("category", category);
        return "/admin/new_web/" + type;
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("type") String type, @RequestParam("id") List<String> id) {
        recoverystationService.delete(id, type);
        return "redirect:/recovery/findAll?type=" + type;
    }

    @GetMapping("/batchDelete")
    @ResponseBody
    public String batchDeleta(@RequestParam String type, @RequestParam List<String> ids) {
        recoverystationService.delete(ids, type);
        return "success";
    }

    @GetMapping("/restore")
    public String restore(@RequestParam("type") String type, @RequestParam("id") List<String> id) {
        recoverystationService.restore(id, type);
        return "redirect:/recovery/findAll?type=" + type;
    }

    @GetMapping("/batchRestore")
    @ResponseBody
    public String batchRestore(@RequestParam("type") String type, @RequestParam("ids") List<String> ids) {
        recoverystationService.restore(ids, type);
        return "success";
    }
}
