package com.geek.okweb.controller.admin;

import com.alibaba.fastjson.JSON;
import com.geek.okweb.domain.*;
import com.geek.okweb.service.*;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Create by Gai on 2018/12/11 09:53
 */
@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private WorktableService worktableService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ImageService imageService;


    @GetMapping("/findByLang")
    @ResponseBody
    public String findByLang(@RequestParam String proId){
        Product product = productService.findById(proId);
        return product.getLanguage();
    }

    @GetMapping("/saveLang")
    public String saveLang(@RequestParam String proId, @RequestParam String lang) {
        Product product = productService.findById(proId);
        product.setLanguage(lang);
        productService.update(product);
        return "redirect:/admin/findProductDetail?wid=" + product.getWid();
    }

    /**
     * 保存产品名称
     *
     * @param id
     * @param name
     * @return
     */
    @PostMapping("/addName")
    @ResponseBody
    public String saveName(String id, String name, @RequestParam(value = "proDataId", required = false) String proDataId) {
        if (StringUtils.isBlank(proDataId)){
            productService.saveName(id, name);
        }else {
            Product product = productService.findById(id);
            List<ProductData> productData = product.getJson();
            productData.stream().forEach(x -> {
                if (StringUtils.equals(x.getId(),proDataId)){
                    x.setProductMsg(name);
                }
            });
            productService.update(product);
        }
        return "success";
    }

    /*
     *删除产品
     */
    @ResponseBody
    @PostMapping("/deleteProduct")
    public String deleteProduct(String id) {
        productService.delete(id);
        return "success";
    }


    /**
     * 产品查找工作簿
     * @param page
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/findWorktable")
    public String findWorktable(@RequestParam(value = "page",defaultValue = "1",required = false)int page,
                                @RequestParam(value = "pageSize",defaultValue = "5",required = false)int pageSize,
                                Model model){
        MyPage<Worktable> workPage = worktableService.findByWorkTablePage(0,page,pageSize);
        Category category = categoryService.findOne();
        model.addAttribute("workPage", workPage);
        model.addAttribute("category",category);
        return "admin/new_web/product_form";
    }

    /**
     * 删除工作簿进回收站
     * @param wid
     * @return
     */
    @GetMapping("/deleteWorktable")
    public String deleteWorktable(String wid){
        Worktable worktable = worktableService.findById(wid);
        log.info("【work】={}，wid={}",worktable,wid);
        worktable.setStatus(1);
        worktableService.update(worktable);
        return "redirect:/product/findWorktable";
    }

    @PostMapping("/updataProductData")
    public String updataProductData(String proId, String proDataId, String jsonText) {
        Product product = productService.findById(proId);
        List<ProductData> productData = product.getJson();
        productData.stream().forEach(x -> {
            if (StringUtils.equals(x.getId(),proDataId)){
                x.setProauctName(jsonText);
            }
        });
        productService.update(product);
        return "success";
    }

    /**
     * 全选权限
     * @param fileIds
     * @param flag
     * @param proId
     * @return
     */
    @PostMapping("/addPackageDatas")
    @ResponseBody
    public String addPackageDatas(@RequestParam(value ="fileId") String fileIds,@RequestParam String flag,@RequestParam String proId){
        List<String> fileIdList = JSON.parseArray(fileIds, String.class);
        log.info("fileId={},flag={},proId={}", fileIds, flag, proId);
        Product product = productService.findById(proId);
        List<String> productFiles = product.getFiles();
        for (int i = 0; i < fileIdList.size(); i++) {
            String fileId = fileIdList.get(i);
            log.info("fileId=={}",fileId);
            if (StringUtils.equals("save", flag)) {
                if (!productFiles.contains(fileId)){
                    productFiles.add(fileId);
                }
            } else {
                for (int j = 0; j < productFiles.size(); j++) {
                    if (productFiles.get(j).equals(fileId)) {
                        productFiles.remove(j);
                    }
                }
            }
        }
        productService.merge(product);
        return "success";
    }



    /**
     * 添加产品数据包
     *
     * @param fileId
     * @return
     */
    @PostMapping("/addPackageData")
    @ResponseBody
    public String addPackageData(@RequestParam String fileId, @RequestParam String flag, @RequestParam String ids) {

        Product product = productService.findById(ids);
        List<String> files = product.getFiles();
        if (StringUtils.equals(flag,"save")){
            files.add(fileId);
        }else {
            for (int i = 0 ; i < files.size() ; i ++){
                if (files.get(i).equals(fileId)){
                    files.remove(i);
                }
            }
        }
        productService.merge(product);
        return "success";
    }


    /**
     * 查找产品数据包文件
     */
    @GetMapping("/findProductFile")
    public String findProductFile(@RequestParam(value = "categoryId",required = false)String categoryId,@RequestParam(value = "page",defaultValue = "1",required = false)int page,
                                  @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,String id,String wid,Model model){
        log.info("page=={}", page);
        if (page <=0){
            page = 1;
        }
        log.info("controller==={}", page);
        MyPage<FileUpload> filePage = null;
        if (StringUtils.isNotBlank(categoryId)) {
            Set<String> ids = new HashSet<>();
            ids.add(categoryId);
            filePage = fileService.findByCategorys(ids, page, pageSize,null);
            model.addAttribute("categoryId", categoryId);
        } else {
            filePage = fileService.findByFilePage(0, page, pageSize);
        }
        Product product = productService.findById(id);
        List<String> ids = product.getFiles();
        if (ids!= null && ids.size()>0){
            model.addAttribute("ids",ids);
        }

        List<String> fileListId = new ArrayList<>();
        filePage.getItems().stream().map(x -> x.getId()).forEach(y -> {
            if (product.getFiles().contains(y)){
                fileListId.add(y);
            }
        });
        if (fileListId.size() == filePage.getItems().size()){
            model.addAttribute("check", "ture");
        }

        List<String> fileIds = filePage.getItems().stream().map(x -> x.getId()).collect(Collectors.toList());
        Category category = categoryService.findOne();
        Worktable worktable = worktableService.findById(wid);
        model.addAttribute("filePage",filePage);
        model.addAttribute("fileIds",fileIds);
        model.addAttribute("category",category);
        model.addAttribute("product",product);
        model.addAttribute("id",id);
        model.addAttribute("wid",wid);
        model.addAttribute("worktable", worktable);
        return "admin/new_web/addProductFile";
    }



    /**
     * 查找产品文章
     */
    @GetMapping("/findProductBlog")
    public String findProductBlog(@RequestParam(value = "categoryId",required = false) String categoryId, @RequestParam(value = "page",defaultValue = "1",required = false)int page,
                                  @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize, @RequestParam(value = "id") String proId, Model model){
        MyPage<Blog> blogPage ;
        if (StringUtils.isNotBlank(categoryId)) {
            Set<String> ids = new HashSet<>();
            ids.add(categoryId);
            blogPage = blogService.findByCategorys(ids, page, pageSize, 1, null);
        }else {
            blogPage = blogService.findAllByStatusAndReview(0, 1, pageSize, page, null);
        }
        Product product = productService.findById(proId);
        Worktable worktable = worktableService.findById(product.getWid());
        String blogId = product.getBlogId();
        if (StringUtils.isNotBlank(blogId)) {
            model.addAttribute("blogId", blogId);
        }
        Category category = categoryService.findOne();
        model.addAttribute("blogPage",blogPage);
        model.addAttribute("category",category);
        model.addAttribute("categoryId",categoryId);
        model.addAttribute("worktable",worktable);
        model.addAttribute("product",product);
        model.addAttribute("id",proId);
        return "admin/new_web/addProductBlog";
    }

    @GetMapping("/findProductImage")
    public String findProductImage(@RequestParam("id") String id, @RequestParam(value = "page",defaultValue = "1",required = false)int page,
                          @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize, Model model){
        Product product = productService.findById(id);
        MyPage<Image> imgPage = imageService.findAllByStatus(0, page, pageSize);
        Worktable worktable = worktableService.findById(product.getWid());
        Category category = categoryService.findOne(); //分类
        model.addAttribute("category",category);
        model.addAttribute("imgPage", imgPage);
        model.addAttribute("product", product);
        model.addAttribute("worktable", worktable);
        model.addAttribute("url", "addProductImage");
        return "admin/new_web/addProductImage";
    }

    @GetMapping("/findProductDevImage")
    public String findProductDevImage(@RequestParam("id") String id, @RequestParam(value = "page",defaultValue = "1",required = false)int page,
                          @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize, Model model){
        Product product = productService.findById(id);
        MyPage<Image> imgPage = imageService.findAllByStatus(0, page, pageSize);
        Worktable worktable = worktableService.findById(product.getWid());
        Category category = categoryService.findOne(); //分类
        model.addAttribute("category",category);
        model.addAttribute("imgPage", imgPage);
        model.addAttribute("product", product);
        model.addAttribute("worktable", worktable);
        model.addAttribute("url", "addProductDevImg");
        return "admin/new_web/addProductDevImg";
    }

    @GetMapping("/addPackageBlog")
    @ResponseBody
    public String addPackageBlog(@RequestParam String proId , @RequestParam String blogId){
        Product product = productService.findById(proId);
        product.setBlogId(blogId);
        productService.update(product);
        return "success";
    }

    @GetMapping("/addPackageImage")
    @ResponseBody
    public String addPackageImage(@RequestParam String id , @RequestParam String imgId){
        Product product = productService.findById(id);
        product.getImgs().add(imgId);
        productService.update(product);
        return "success";
    }

    @GetMapping("/deletePackageImage")
    @ResponseBody
    public String deletePackageImage(@RequestParam String id , @RequestParam String imgId){
        Product product = productService.findById(id);
        product.getImgs().remove(imgId);
        productService.update(product);
        return "success";
    }

    @GetMapping("/addPackageDevImg")
    @ResponseBody
    public String addPackageDevImg(@RequestParam String id , @RequestParam String imgId){
        Product product = productService.findById(id);
        product.getDevImgs().add(imgId);
        productService.update(product);
        return "success";
    }

    @GetMapping("/deletePackageDevImg")
    @ResponseBody
    public String deletePackageDevImg(@RequestParam String id , @RequestParam String imgId){
        Product product = productService.findById(id);
        product.getDevImgs().remove(imgId);
        productService.update(product);
        return "success";
    }


    @GetMapping("/categoryWork")
    public String categoryWork(@RequestParam("id")String id,@RequestParam(name = "page",defaultValue = "1",required = false)Integer page,
                               @RequestParam(name = "pageSize",defaultValue = "2",required = false)Integer pageSize,Model model){
        Set<String> ids = new HashSet<>();
        ids.add(id);
        if(page <= 0){
            page = 1;
        }
        MyPage<Worktable> workPage = worktableService.findWorkByCategroys(ids, page, pageSize);
        Category category = categoryService.findOne();
        model.addAttribute("id", id);
        model.addAttribute("category", category);
        model.addAttribute("workPage", workPage);
        return "admin/new_web/product_form";
    }



    /**
     * 产品详情页
     */
    @GetMapping("/proBlog")
    public String proBlog(@RequestParam String id, Model model, @RequestParam(name = "page",defaultValue="1",required = false) Integer page,
                          @RequestParam(name = "pageSize",defaultValue="5",required = false) Integer pageSize){
        Product product = productService.findById(id);
        MyPage<Product> productMyPage = new MyPage<>(0, 0, 0);
        productMyPage.setItems(Arrays.asList(product));
        List<String> productName = productService.findProductName(productMyPage);
        List<String> files = product.getFiles();
        MyPage<FileUpload> filePage = new MyPage<>(0,0,0);
        if (files != null && files.size() != 0){
            Set<String> ids = new HashSet<>(files);
            filePage = fileService.findByIds(ids, pageSize, page);
        }
        Blog blog = blogService.findById(product.getBlogId());
        model.addAttribute("filePage",filePage);
        model.addAttribute("productName",productName);
        model.addAttribute("blog",blog);
        model.addAttribute("id",id);
        model.addAttribute("product",product);
        return "/products_details";
    }



    /**
     * 添加工作簿分类
     * @param
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/addWorkCategory")
    public String addImageCategory(@RequestParam(name = "worktableIds")String mids, @RequestParam(name = "cateids")String cids){
        List<String> worktableIds = JSON.parseArray(mids, String.class);
        List<String> cateids = JSON.parseArray(cids, String.class);
        log.info("【worktableIds】={}",worktableIds);
        log.info("【cateids】={}",cateids);
//        log.info("【进入】={}",cateids);
        worktableIds.stream().forEach(x -> { Worktable worktable = worktableService.findById(x);worktable.setCateids(cateids);worktableService.update(worktable);});
        return "redirect:/product/findWorktable";
    }


}
