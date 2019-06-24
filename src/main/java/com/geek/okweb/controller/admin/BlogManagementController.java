package com.geek.okweb.controller.admin;

import com.alibaba.fastjson.JSON;
import com.geek.okweb.domain.*;
import com.geek.okweb.form.DiffArticle;
import com.geek.okweb.service.*;
import com.geek.okweb.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Create by Gai on 2018/10/17 13:40
 */
@Controller
@Slf4j
@RequestMapping("/admin")
public class BlogManagementController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WorktableService worktableService;

    @Autowired
    private CustomerService customerService;

    /**
     * 查找文件
     * @return
     */
    @GetMapping("/findFile")
    public String findFile(@RequestParam(value = "id" , required = false)String id,@RequestParam(value = "page",defaultValue = "1",required = false)int page,
                          @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,Model model){
        MyPage<FileUpload> filePage = null;
        if (StringUtils.isNotBlank(id)){
            Set<String> ids = new HashSet<>();
            ids.add(id);
            filePage = fileService.findByCategorys(ids, page, pageSize,null);
            model.addAttribute("id", id);
        }else {
            filePage = fileService.findByFilePage(0,page,pageSize);
        }
        List<String> fileIds = filePage.getItems().stream().map(FileUpload::getId).collect(Collectors.toList());
        Category category = categoryService.findOne();
        model.addAttribute("filePage",filePage);
        model.addAttribute("currentPageFileId",fileIds);
        model.addAttribute("category",category);
        return "admin/new_web/file_management";
    }

    /**
     * 查询博客
     * @param
     * @return
     */
    @GetMapping("/findBlog")
    public String findByStatusAndReview(Model model,@RequestParam(value = "status",required = false)Integer status,@RequestParam(value = "review",required = false)Integer review,
                                        @RequestParam(value = "id",required = false)String id,
                                        @RequestParam(value = "action",required = false)String action,
                                        @RequestParam(value = "page",defaultValue = "1",required = false)int page,
                                        @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize){
        Category category = categoryService.findOne();

        log.info("action=={}", action);

        MyPage<Blog> blogPage = null;
        if (StringUtils.isNotBlank(id)) {
            Set<String> ids = new HashSet<>();
            ids.add(id);
            List<Blog> articleList = blogService.findAllByCid(id, 1, 0);

            if (articleList != null && articleList.size() > 0) {
                action = articleList.get(0).getSort();
            } else {
                action = "desc";
            }
            blogPage = blogService.findByCategorys(ids, page, pageSize,review,action);
            model.addAttribute("id", id);
        }else {
            List<Blog> articleList = blogService.findAllByStatusAndReview(0, 1);
            if (articleList != null && articleList.size() > 0) {
                action = articleList.get(0).getSort();
            } else {
                action = "desc";
            }
            blogPage = blogService.findAllByStatusAndReview(status,review,pageSize,page,action);
        }
        List<String> articleIds = blogPage.getItems().stream().map(x -> x.getId()).collect(Collectors.toList());

        model.addAttribute("category", category);
        model.addAttribute("blogPage",blogPage);
        model.addAttribute("articleIds",articleIds);
        model.addAttribute("page", page);
        if (blogPage != null && blogPage.getItems().size() > 0) {
            String sort = blogPage.getItems().get(0).getSort();
            if ("asc".equals(sort)) {
                model.addAttribute("sort", "desc");
            }else {
                model.addAttribute("sort", "asc");
            }
        }


        if(review == 0) {
            return "admin/new_web/article_review";
        }
        else if (review == 1)
            return "admin/new_web/article_adopt";
        else if (review == 2)
            return "admin/new_web/article_refust";
        else
            return "error";
    }

    /**
     * delete blog
     * @param blogId
     * @return
     */
    @GetMapping("/delete/blog")
    public String deleteBlog(@RequestParam(name = "blogId") String blogId,Integer review){
        log.info("blogId========" ,blogId);
        Blog blog = blogService.findBlogById(blogId);
        blog.setStatus(1);
        blogService.updateBlog(blog);
        return "redirect:/admin/findBlog?status=0&review="+review;

    }

    @GetMapping("/blog/detail")
    public String blogDetail(@RequestParam("id") String id,@RequestParam(value = "flag",required = false)String flag, Model model){
        Blog blog = blogService.findById(id);
        Category category = categoryService.findOne();
        model.addAttribute("blog", blog);
        model.addAttribute("flag", flag);
        model.addAttribute("category", category);
        return "admin/new_web/article_detail";
    }

    @GetMapping("/findOneBlog")
    @ResponseBody
    public String findOneBlog(@RequestParam String id) {
        log.info("【博客id】=={}", id);
        Blog blog = blogService.findById(id);
        return blog.getHtmlContent();
    }

    /**
     * 修改文章
     * @param blog 文章id
     * @param file 文件
     * @return
     */
    @ResponseBody
    @PostMapping("/modify")
    public Result modify(@Valid Blog blog, /*@RequestParam(name = "userId") String userId,*/ @RequestParam(name = "uploadfile") MultipartFile file, HttpSession session,
                         @RequestParam(value = "bids",required = false)String bids){
        User user = (User)session.getAttribute("user");
        try {
            log.info("user={}",user.getId());
            log.info("blogId={}",blog.getId());
            Blog result = blogService.findById(blog.getId());
            //检验文章标题是否重复
            if (!StringUtils.equals(blog.getTitle(), result.getTitle())) {
                boolean isAble = blogService.findbyTitle(blog.getTitle());
                if(isAble){
                    //存在
                    return ResultUtil.fail("文章标题已经存在");
                }
            }

            if (blog.getId() != null){
                if (null != file && StringUtils.isNotBlank(file.getOriginalFilename())){
                    String filename =file.getOriginalFilename();
                    log.info("文件名称：{}",filename);
                    String imagePath = FileUtil.getPath()+"/upload/";
                    log.info("imagePath=={}",imagePath);
                    FileUtil.uploadFile(file.getBytes(),imagePath,filename);
                    blog.setImageUrl("/imagePath/"+filename);
                }else {
                    blog.setImageUrl(result.getImageUrl());
                }

                if (bids != null && StringUtils.isNotBlank(bids)){
                    List<DiffArticle> diffArticles = JSON.parseArray(bids, DiffArticle.class);
                    blog.setDiffLangArticles(diffArticles);
                }

                blogService.saveBlog(blog,user.getId());
                return ResultUtil.success("更新成功");
            }
            return ResultUtil.fail();
        }catch (Exception e){
            log.info("erroe={}",e);
            return ResultUtil.fail();
        }
    }


    /**
     * 审核文章
     * @param id 文章id
     * @return
     */
    @GetMapping("/reviewArticle")
    public String reviewArticle(String id,Integer review){
        blogService.reviewArticle(id,review);
        return "redirect:/admin/findBlog?status=0&review=0";
    }


    /*
     *删除文件
     */
    @ResponseBody
    @PostMapping("/deleteFile")
    public String deleteFile(String id) {
        fileService.delete(id);
        return "success";
    }


    /**
     * 删除文件到回收站
     */
    @GetMapping("/delete")
    public String delete(String id){
        FileUpload file = fileService.findById(id);
        log.info("id========{},file========={}",id ,file);
        file.setStatus(1);
        fileService.update(file);
        return "redirect:findFile";
    }

    /**
     * 查找工作簿里产品
     * @param page
     * @param pageSize
     * @param wid
     * @param model
     * @return
     */
    @GetMapping("/findProductDetail")
    public String findProductDetail(@RequestParam(value = "page",defaultValue = "1",required = false)int page,
                                    @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
            @RequestParam("wid") String wid,@RequestParam(value = "flag",required = false)String flag,Model model){
        log.info("wid=={}",wid);
        try {
            MyPage<Product> proPage = productService.findWidByPage(null,page,pageSize,wid);
            Category category = categoryService.findOne();
            List<String> productName = productService.findProductName(proPage);
            Worktable worktable = worktableService.findById(wid);
            model.addAttribute("productName",productName);
            model.addAttribute("category",category);
            model.addAttribute("wid",wid);
            model.addAttribute("worktable",worktable);
            model.addAttribute("proPage",proPage);
            model.addAttribute("flag",flag);
        }catch (Exception e){
            log.info("异常=={}",e);
        }
        return "admin/new_web/productDetail";
    }




    @GetMapping("/categoryProduct")
    public String categoryProduct(@RequestParam("id")String id,@RequestParam(name = "page",defaultValue = "1",required = false)Integer page,
                               @RequestParam(name = "pageSize",defaultValue = "10",required = false)Integer pageSize,
                              Model model){
        Set<String> ids = new HashSet<>();
        ids.add(id);
        if(page <= 0 ){
            page = 1;
        }
        MyPage<Product> proPage = productService.findByCategorys(ids, page, pageSize);
        List<String> productName = null;
        log.info("proPageSize={}，page{}", proPage.getItems().size(), proPage.getItems());
        if (proPage.getItems().size() == 0) {

        }else {
            log.info("shou");
             productName = productService.findProductName(proPage);
        }
        Category category = categoryService.findOne();
        model.addAttribute("productName",productName);

        model.addAttribute("id", id);
        model.addAttribute("category", category);
        model.addAttribute("proPage", proPage);
        return "admin/new_web/productDetail";
    }

    /**
     * 文章置顶
     * @param id 文章id
     * @return
     */
    @GetMapping("/isTop")
    public String isTopArticle(@RequestParam("id") String id, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "cid", required = false) String cid){
        blogService.isTopArticle(id, 1);
        return isExistTop(id, page, cid);
    }

    /**
     * 文章取消置顶
     *
     * @param id 文章id
     * @return
     */
    @GetMapping("/isNotTop")
    public String isNotTopArticle(@RequestParam("id") String id, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "cid", required = false) String cid) {
        blogService.isTopArticle(id, 0);
        return isExistTop(id, page, cid);
    }

    private String isExistTop(String id, Integer page,  String cid) {
        if (page == null || page <= 0) {
            page = 1;
        }
        if (StringUtils.isNotBlank(cid)) {
            return "redirect:/admin/findBlog?page=" + page + "&status=0&review=1&id=" + cid;
        } else {
            return "redirect:/admin/findBlog?page=" + page + "&status=0&review=1";
        }
    }

    @GetMapping("/addBlog")
    public String addBlogPage(Model model){
        Category category = categoryService.findOne();
        model.addAttribute("category",category);
        return "admin/new_web/addBlog_new";
    }

    /**
     * 发布博客
     * @param blog 博客对象
     * @param userId 用户id
     * @param file 封面图片
     * @param bids 不同语言的文章 （brother 兄弟）
     * @return
     */
    @PostMapping("/addBlog")
    @ResponseBody
    public Result addBlog(@Valid Blog blog,@RequestParam(name = "userId") String userId,@RequestParam(name = "uploadfile") MultipartFile file,
                          @RequestParam(value = "bids",required = false)List<String> bids){
        try {
            //判断文章标题是否已经存在
            boolean isAble = blogService.findbyTitle(blog.getTitle());
            if (isAble) {
                //存在
                return ResultUtil.fail("文章标题已存在");
            }

            if (null != file && StringUtils.isNotBlank(file.getOriginalFilename())){
                String filename = file.getOriginalFilename();

                log.info("文件名称：{}",filename);
                String randomFileName = FileUtil.getRandomFileName(filename);//带uuid的
                String imagePath = FileUtil.getPath()+"/upload/";
                log.info("imagePath=={}",imagePath);
                FileUtil.uploadFile(file.getBytes(),imagePath,randomFileName);

                blog.setImageUrl("/imagePath/"+randomFileName);
            }else {
                blog.setImageUrl("/imagePath/");
            }

//            blog.setImageUrl(httpUrl+filename);
            String uuid = KeyUtil.UUID();
            blog.setId(uuid);

            List<DiffArticle> diffArticles = new ArrayList<>();
            log.info("【bids】={}", bids);
            if (null != bids && bids.size() > 0){

                for (String id : bids) {
                    Blog diffLangArticle = blogService.findById(id);
                    if (StringUtils.equals(blog.getLanguage(), diffLangArticle.getLanguage())) {
                        return ResultUtil.fail("文章所选语言有冲突，请重新进入本页面");
                    }
                    diffArticles.add(new DiffArticle(blog.getLanguage(), blog.getId()));
                    diffArticles.add(new DiffArticle(diffLangArticle.getLanguage(), diffLangArticle.getId()));
                }

                bids.stream().forEach((id) -> {
                    Blog broArticle = blogService.findById(id);
                    broArticle.setDiffLangArticles(diffArticles);
                    blogService.updateBlog(broArticle);
                });



            }
            bids.add(uuid);

            blog.setDiffLangArticles(diffArticles);
            log.info("user={}",userId);
            blogService.saveBlog(blog, userId);

            log.info("【bids】={}", bids);
            return ResultUtil.success(bids);
        }catch (Exception e){
            log.info("erroe={}",e);
            return ResultUtil.fail();
        }

    }


    @ResponseBody
    @RequestMapping(value="/uploadImg")
    public Map<String,Object> uploadImg(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file, HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String fileName = file.getOriginalFilename();
        log.info("文件名：{}",fileName);
        String randomFileName = FileUtil.getRandomFileName(fileName);//带uuid的文件名
        String imagePath = FileUtil.getPath()+"/upload/";
        //判断文件是否存在
        FileUtil.fileExist(imagePath);

        //保存
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(imagePath + randomFileName);
            Files.write(path, bytes);
            //FileUtil.uploadFile(bytes,path,fileName);
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功！");
            resultMap.put("url","/imagePath/"+randomFileName);
        } catch (Exception e) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败！");
            e.printStackTrace();
        }
        System.out.println(resultMap.get("success"));
        return resultMap;
    }

    @GetMapping("/index")
    public String index(Model model, @RequestParam(value = "id", required = false) String id, HttpSession session) {
        Category category = categoryService.findOne();
        if (StringUtils.isNotBlank(id)) {
            Cateitem cateitem = category.findCateitem(id);
            model.addAttribute("cateitem", cateitem);
        }
        List<Customer> customerList = customerService.findAll();
        if (customerList != null && customerList.size() > 0) {
            session.setAttribute("compenyName", customerList.get(0).getCompany());
        }else {
            session.setAttribute("compenyName", "英锐恩科技有限公司");
        }
        model.addAttribute("category", category);
        return "admin/new_web/index";
    }

    @GetMapping("/image")
    public String image(){
        return "admin/new_web/image_admin";
    }

    @GetMapping("/categoryArticle")
    public String categoryArticle(@RequestParam("categoryId")String categoryId,@RequestParam(value = "page",defaultValue = "1",required = false)int page,
                                  @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,Model model){
        Set<String> ids = new HashSet();
        ids.add(categoryId);
        MyPage<Blog> blogPage = blogService.findByCategorys(ids, page, pageSize,1, null);
        Category category = categoryService.findOne();
        model.addAttribute("category", category);
        model.addAttribute("blogPage", blogPage);
        return "admin/new_web/article_adopt";
    }

    @GetMapping("/findAllArticle")
    public String findAll(@RequestParam(value = "page",required = false,defaultValue = "1")Integer page,@RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,Model model,
                          @RequestParam(value = "id",required = false)String id){
        MyPage<Blog> blogPage;
        if (StringUtils.isNotBlank(id)){
            Set ids = new HashSet();
            ids.add(id);
            blogPage = blogService.findByArticleCategory(ids, page, pageSize);
            model.addAttribute("id", id);
        }else {
            blogPage = blogService.findAllByStatus(0, page, pageSize);
        }
        Category category = categoryService.findOne();
        model.addAttribute("blogPage",blogPage);
        model.addAttribute("category",category);
        return "admin/new_web/article_all";
    }


    @ResponseBody
    @GetMapping("/batchDelete")
    public String batchDelete(@RequestParam("articleIds")List<String> articleIds) {
        log.info("接受文章ids = {}",articleIds);
        if (articleIds != null && articleIds.size() > 0){
            articleIds.stream().forEach(x -> {
                log.info(x);
                blogService.updateDeleteStatus(x, 1);
            });
        }
        return "success";
    }

    @ResponseBody
    @GetMapping("changeArticleCategory")
    public String changeArticleCategory(@RequestParam("cateids")List<String> cateids,@RequestParam("articleIds")List<String> articleIds){
        articleIds.stream().forEach(articleId -> {
            Blog blog = blogService.findBlogById(articleId);
            Category category = categoryService.findOne();
            blog.setCateids(cateids);
            String cateStr = blogService.getCategoryName(category, cateids);
            blog.setCateName(cateStr);
            blogService.updateBlog(blog);
        });
        return "success";
    }

    /**
     * 是否隐藏文章
     * @param id 文章id
     * @param operation 操作（true为隐藏文章，false为显示文章）
     * @return
     */
    @GetMapping("/isHiddenArticle")
    public String hiddenArticle(@RequestParam("id")String id,@RequestParam("operation")Boolean operation,
                                @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "cid", required = false) String cid){
        log.info("【operation】={}",operation);
        Blog blog = blogService.findOne(id);
        if (blog != null){
            if (operation) {
                blog.setIsHidden(1);  //隐藏文章
            } else {
                blog.setIsHidden(0);  //显示文章
            }
            blogService.updateBlog(blog);
            return isExistTop(id, page, cid);
        }
        return "redirect:/error";
    }

    @PostMapping("/exportDatabase")
    @ResponseBody
    public Result exportDatabase(){
        try {
            DatabaseBackupUtil.backupForLinux();
            log.info("【备份数据库成功】");
            return ResultUtil.success("备份成功");
        } catch (Exception e) {
            log.error("【备份数据库失败】 = {}", e);
            return ResultUtil.fail();
        }

    }

    @GetMapping("/sort")
    public String articleSort(@RequestParam(value = "cid", required = false) String cid, @RequestParam("sort") String sort,
                              @RequestParam(value = "page",defaultValue = "1",required = false)int page) {
        List<Blog> articleList = new ArrayList<>();
         if (StringUtils.isNotBlank(cid)) {
             List<Blog> sortArticles = blogService.findAllByStatusAndReviewAndSort(0, 1, cid, sort);
             articleList = blogService.findAllByCid(cid, 1, 0);
             for (int i = 0; i < sortArticles.size(); i++) {
                 Blog article = sortArticles.get(i);
                 article.setNumber(i + 1);
                 blogService.updateBlog(article);
             }
        } else {
            articleList = blogService.findAllByStatusAndReview(0, 1);
        }

        articleList.stream().forEach((article) -> {
            article.setSort(sort);
            blogService.updateBlog(article);
        });

        if (StringUtils.isNotBlank(cid)) {
            return "redirect:/admin/findBlog?status=0&review=1&id=" + cid + "&action="+ sort +"&page=" + page;
        }

        return "redirect:/admin/findBlog?status=0&review=1&action="+ sort +"&page=" + page;
    }

    @GetMapping("/editArticleSort")
    @ResponseBody
    public Result editArticleSort(@RequestParam String id, @RequestParam Integer sortNum){
        Blog blog = blogService.findById(id);
        log.info("【排序修改】blog = {}" , blog);
        blog.setNumber(sortNum);
        blogService.updateBlog(blog);
        return ResultUtil.success("success");
    }

}
