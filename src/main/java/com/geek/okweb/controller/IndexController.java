package com.geek.okweb.controller;

import com.geek.okweb.domain.*;
import com.geek.okweb.service.*;
import com.geek.okweb.utils.ChineseCharacterUtil;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class IndexController {
    @Autowired
    private TemplateService templateService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private FormService formService;

    @Autowired
    private FormItemService formItemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WorktableService worktableService;

    @Autowired
    private CustomerService customerService;

    @PersistenceContext
    private EntityManager entityManager;

    private String lang;

    private String searchArticleCateName = "公司方案";

    @ModelAttribute
    public void language(@RequestParam(value = "lang",required = false)String lang, HttpSession session){
        List<String> langs = Arrays.asList("zh_CN","zh_TW","en_US");

        if (StringUtils.isNotBlank(lang) && langs.contains(lang)){
            this.lang = lang;
            session.setAttribute("lang",lang);
        } else {
            String language = (String) session.getAttribute("lang");
            if (StringUtils.isNotBlank(language) && langs.contains(language)){
                this.lang = language;
                session.setAttribute("lang",language);
            }else {
                this.lang = "zh_CN";
                session.setAttribute("lang",this.lang);
            }
        }
    }


    /**
     * 查询全部
     * @param model
     * @param page
     * @param keyword
     * @return
     */
    @GetMapping("/searchAll")
    public String searchAll(Model model,
                            @RequestParam(name = "page",defaultValue="1",required = false) Integer page,
                            @RequestParam(name = "keyword",required = false)String keyword,
                            @RequestParam(name = "type")String type){

        if (StringUtils.isNotBlank(keyword)) {
//            MyPage<Blog> articlePage = blogService.searchBlog(page, 5, keyword, 0, 1, 0, searchArticleCateName);
            MyPage<Blog> articlePage = blogService.searchBlog(page, 5, keyword, 0, 1, 0, null, "zh_CN");
            MyPage<FileUpload> filePage = fileService.searchFile(page, 5, keyword);
            MyPage<Product> productPage = productService.findByName(page, 5, "name", keyword);
            List<String> productName = new ArrayList<>();
            if (productPage != null && productPage.getItems().size() > 0) {
                productName.add("pdf");
                productName.add("产品名称");
                Category category = categoryService.findOne();
                productPage = productCateName(productPage, category);

            }
            Integer pageCount = 0;
            if (articlePage.getPagecount() > filePage.getPagecount()) {
                pageCount = articlePage.getPagecount();
            } else {
                pageCount = filePage.getPagecount();
            }

            if (pageCount < productPage.getPagecount()) {
                pageCount = productPage.getPagecount();
            }

            List<Integer> pageNum = pageNum(page, pageCount);

            model.addAttribute("articlePage", articlePage);
            model.addAttribute("filePage",filePage);
            model.addAttribute("productPage", productPage);
            model.addAttribute("proName", productName);

            model.addAttribute("keyword", keyword);
            model.addAttribute("pageCount", keyword);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("pageNum", pageNum);
            model.addAttribute("currentPage", page);
        }
        model.addAttribute("type", type);
        return lang + "/searchs";
    }

    public List<Integer> pageNum(Integer page, Integer pagecount){
        List<Integer> pages = new ArrayList<>();
        // 显示5个相邻的页码
        int first = 1;
        int end = page + 2;
        if (page - 2 > 0) {
            first = page - 2;
        }
        if (end > pagecount) {
            end = pagecount;
        }
        if ((end - first) < 4 && (first + 4) <= pagecount) {// 后不足五个页码的补齐
            end = first + 4;
        }
        if ((end - first) < 4 && (end - 4) >= 1) {// 前不足五个页码的补齐
            first = end - 4;
        }

        int fornum = end - first + 1;
        for (int i = 0; i < fornum; i++) {
            pages.add(first);// 存入页码
            first++;
        }
        return pages;
    }


    @GetMapping("/searchFile")
    public String searchFile(Model model,
                             @RequestParam(name = "page",defaultValue="1",required = false) Integer page,
                             @RequestParam(name = "keyword",required = false)String keyword,
                             @RequestParam(name = "type")String type){
        if(StringUtils.isNotBlank(keyword)){
            MyPage<FileUpload> filePage = fileService.searchFile(page, 5, keyword);
            model.addAttribute("searchResult",filePage);
        }
        model.addAttribute("keyword",keyword);
        model.addAttribute("type",type);
        return lang + "/searchs";
    }


    /**
     * 搜索产品
     * @param model
     * @param page
     * @param keyword
     * @return
     */
    @GetMapping("/searchProduct")
    public String searchProduct(Model model,
                                @RequestParam(name = "page",defaultValue="1",required = false) Integer page,
                                @RequestParam(name = "keyword")String keyword,
                                @RequestParam(name = "type")String type){
        MyPage<Product> productPage = productService.findByName(page,5,"name",keyword);
        List<String> productName = new ArrayList<>();
        if (productPage != null && productPage.getItems().size() > 0 ){
            productName.add("pdf");
            productName.add("产品名称");
            Category category = categoryService.findOne();
            productPage = productCateName(productPage, category);

        }

        model.addAttribute("searchResult", productPage);
        model.addAttribute("proName", productName);
        model.addAttribute("keyword",keyword);
        model.addAttribute("type",type);

        return lang + "/searchs";
    }

    private MyPage<Product> productCateName(MyPage<Product> productPage, Category category) {
        List<Product> items = productPage.getItems();
        items.forEach((pro) -> {
            Worktable worktable = worktableService.findById(pro.getWid());
            List<String> cateids = worktable.getCateids();
            if (StringUtils.isNotBlank(pro.getBlogId())) {
                Blog blog = blogService.findById(pro.getBlogId());
                if (StringUtils.isNotBlank(blog.getId())) {
                    pro.setArticleTitle(blog.getTitle());
                }
            }
            if (cateids != null && cateids.size() > 0) {
                String cateId = cateids.get(0);
                Cateitem cateitem = category.findCateitem(cateId);
                pro.setTag(ChineseCharacterUtil.getLowerCase(cateitem.getText(), false));
            }
        });
        productPage.setItems(items);
        return productPage;
    }


    /**
     * 搜索文章
     * @param model
     * @param page
     * @param keyword
     * @return
     */
    @GetMapping("/searchArticle")
    public String searchArticle(Model model,
                                @RequestParam(name = "page",defaultValue="1",required = false) Integer page,
                                @RequestParam(name = "keyword",required = false)String keyword,
                                @RequestParam(name = "type")String type){
        if (StringUtils.isNotBlank(keyword)){
            MyPage<Blog> articlePage = blogService.searchBlog(page,5,keyword,0,1,0,searchArticleCateName, "zh_CN");
//            MyPage<Blog> articlePage = blogService.searchBlog(page,5,keyword,0,1,0, "zh_CN");
            model.addAttribute("searchResult",articlePage);
            log.info("文章 = {}", articlePage.getItems().size());
        }

        model.addAttribute("keyword",keyword);
        model.addAttribute("type",type);

        log.info("进来 = {}", keyword);

        return lang + "/searchs";
    }

    /**
     * 搜索页面
     * @param model
     * @return
     */
    @GetMapping("/searchs")
    public String searchs(Model model, @RequestParam(name = "type", defaultValue = "all", required = false)String type ,
                          @RequestParam(name = "keyword" ,required = false)String keyword){
        model.addAttribute("type", type);
        model.addAttribute("currentPage", 1);
        model.addAttribute("keyword", keyword);
        log.info("类型 = {}", type);
        return lang + "/searchs";
    }



    @GetMapping("/")
    public String index(Model model) {
        return this.url(model, "index", null, null, null,1,5,null,null);
    }

    @GetMapping("/{url}")
    public String url(Model model, @PathVariable String url, @RequestParam(name = "key",required = false) String key,
                      @RequestParam(name = "cid",required = false) String cid,
                      @RequestParam(name = "ids",required = false)Set<String> ids,
                      @RequestParam(name = "page",defaultValue="1",required = false) Integer page,
                      @RequestParam(name = "pageSize",defaultValue="10",required = false) Integer pageSize,
                      @RequestParam(name = "articleFlag",required = false)String articleFlag,
                      @RequestParam(name = "keyword",required = false)String keyword) {

        log.info("id集合="+ids+"=============");
        if (!StringUtils.isNotBlank(url)) {
            url = "index";
        }

        //根据模板url查询模板对象
        Template template = templateService.findByUrl(url,lang);

        if (null != template && StringUtils.isNotBlank(template.getUrl())) {

            Category category = categoryService.findOne();

            //获取模板数据列表
            List<TemplateData> templateDatas = template.getData();

            log.info("【进入】={}", template);
            //遍历模板数据列表
            for (TemplateData td : templateDatas) {
                log.info("=================================================【遍历开始】=================================================");
                //判断是否有输入显示条数
                if (null != td.getNumber() && td.getNumber() > 0){
                    pageSize = td.getNumber();
                }

                if (StringUtils.equals(td.getType(), "blogs")) { //如果模板数据属于blogs类型
                    if (null != ids && ids.size() > 0 && StringUtils.equals(td.getKey(), key)) {
                        Blog blog = blogService.findBlogById(ids.iterator().next());
                        model.addAttribute("article",blog);
                    }
                    MyPage<Blog> blogPage = blogService.findByIds(td.getIds(),page,pageSize,lang);
                    model.addAttribute(td.getKey(), blogPage);
                    if (null != blogPage.getItems() && blogPage.getItems().size() > 0) {
                        model.addAttribute("default", blogPage.getItems().get(0));
                    }
                    log.info("ID文章={}", blogPage.getItems());
                    model.addAttribute("key", td.getKey());

                } else if (StringUtils.equals(td.getType(), "category")) { //如果模板数据属于category类型

                    Integer order = 1;
                    for (String id : td.getIds()) {
                        List<Cateitem> cateitemList = category.findCateitemList(id);
                        log.info("分类列表={}", cateitemList);
                        model.addAttribute(td.getKey() + "_cate" + order, cateitemList);
                        log.info(td.getKey() + "_cate" + order);
                        order++;
                    }

                    Set<String> cids = td.getIds();

                    if (StringUtils.isNotBlank(cid) && StringUtils.equals(td.getKey(), key)) {
                        Cateitem def_cateitem = category.findCateitem(cid);
                        log.info("【默认分类】={}", def_cateitem);
                        model.addAttribute("default_cate", def_cateitem);
                        entityManager.unwrap(Session.class).clear();
                        cids.clear();
                        cids.add(cid);
                    }

                    //根据分类id 分页查询文章
                    MyPage<Blog> blogPage = blogService.findByCategorys(cids, page, pageSize, 1, lang, 0);
                    List<Blog> blogList = blogService.findByAllCategorys(cids, page, pageSize, 1, lang, 0);


                    Blog blogName = new Blog();
                    model.addAttribute(td.getKey() + "_blog" + td.getOrder(), blogPage);
                    //文章阅读量
                    if (blogPage.getItems().size() == 1) {
                        blogService.readCount(blogPage.getItems().get(0));
                    }
                    log.info("分类文章={}", blogPage);
                    model.addAttribute("currentPage", page);


                    if (null != ids && ids.size() > 0 && StringUtils.equals(td.getKey(), key)) {
                        String preTitle = "";
                        String nextTitle = "";
                        Integer size = 0;

                        for (int i = 0; i < blogList.size(); i++) {
                            if (StringUtils.equals(ids.iterator().next(), blogList.get(i).getId())) {
                                size = i;
                                Blog blog1 = blogService.findById(ids.iterator().next());
                                if (i == 0) {
                                    log.info("【上一篇】" + blog1.getTitle());
                                    model.addAttribute("preTitle", blog1.getTitle());
                                    model.addAttribute("preId", blog1.getId());
                                    preTitle = blog1.getId();
//                                    preTitle = blog1.getTitle();
                                } else {
                                    --i;
                                    log.info("【上一篇】" + blogList.get(i).getTitle());
                                    model.addAttribute("preTitle", blogList.get(i).getTitle());
                                    model.addAttribute("preId", blogList.get(i).getId());
//                                    preTitle = blogList.get(i).getTitle();
                                    preTitle = blogList.get(i).getId();
                                    ++i;

                                }
                                if (i == blogList.size() - 1) {
                                    log.info("【下一篇】" + blog1.getTitle());
                                    model.addAttribute("nextTitle", blog1.getTitle());
                                    model.addAttribute("nextId", blog1.getId());
                                    nextTitle = blog1.getId();
//                                    nextTitle = blog1.getTitle();
                                } else {
                                    ++i;
                                    log.info("【下一篇】" + blogList.get(i).getTitle());
                                    model.addAttribute("nextTitle", blogList.get(i).getTitle());
                                    model.addAttribute("nextId", blogList.get(i).getId());
//                                    nextTitle = blogList.get(i).getTitle();
                                    nextTitle = blogList.get(i).getId();
                                    --i;
                                }
                            }
                        }

                        Blog blog = null;
                        if (StringUtils.isNotBlank(articleFlag)) {
                            Integer flag = 0;
                            log.info("【翻文章】={},{}", blogPage.getItems().get(0), blogPage.getItems().size());
                            if (size == 0) {
                                if (StringUtils.equals("pre", articleFlag)) {
//                                    blog = blogService.findByTitle(preTitle);
                                    blog = blogService.findById(preTitle);
                                } else {
//                                    blog = blogService.findByTitle(nextTitle);
                                    blog = blogService.findById(nextTitle);
                                }
                                model.addAttribute("blogDetail", blog);
                            } else {
                                String value = String.valueOf((double) size / pageSize);
                                String index = value.substring(0, value.indexOf("."));
                                flag = Integer.valueOf(index);
                                page = ++flag;
                                log.info("当前篇=={}", page);
                                blog = blogService.findById(ids.iterator().next());
                                if (blog != null) {
                                    model.addAttribute("blogDetail", blog);
                                }
                                model.addAttribute("currentPage", page);
                            }

                        } else {
                            page = 1;
                            MyPage<Blog> list = blogService.findByIds(ids, page, pageSize, lang);
                            log.info("【ids】={}", ids);
                            if (list != null && list.getItems().size() > 0) {
                                blog = list.getItems().get(0);
                                model.addAttribute("blogDetail", list.getItems().get(0));
                                //blogName = list.getItems().get(0);
                            }
                        }
                        if (blog != null) {
                            blogService.readCount(blog);
                        }
                    }

                    //根据分类查找文件
                    MyPage<FileUpload> filePage = fileService.findByCategorys(cids, page, pageSize, null);

                    model.addAttribute(td.getKey() + "_file" + td.getOrder(), filePage);

                    if (ids != null && ids.size() > 0){
                        Product productJson = productService.findById(ids.iterator().next());
                        model.addAttribute("productJson",productJson);
                    }

                    //根据分类查找产品
                    MyPage<Product> productPage = new MyPage<>(0,0,0);
                    List<String> productName = new ArrayList<>();
                    log.info("【keyword】={}", keyword);
                    if (StringUtils.isNotBlank(keyword)) {
                        productPage = productService.findByName(page,pageSize,"name", keyword);

                        if (productPage != null && productPage.getItems().size() > 0) {
                            productName.add("pdf");
                            productName.add("产品名称");
                        }

                        log.info("【检索product】=={}", productPage.getItems());
                    } else {
                        Worktable workTable = worktableService.findWorkTable(cids);
                        if (workTable != null && StringUtils.isNotBlank(workTable.getId())) {

                            productPage = productService.findPageByWidProduct(workTable.getId(), page, pageSize);
                            if (productPage != null && productPage.getItems().size() > 0) {
//                                Product product = productPage.getItems().get(0);
                                productName = productService.findProductName(productPage);

                                MyPage<Blog> articlePage = new MyPage<>(0,0,0);
                                MyPage<FileUpload> fileDetail = new MyPage<>(0, 0, 0 );
                                Product product = new Product() ;

                                if (ids != null && ids.size() > 0){
                                    product = productService.findById(ids.iterator().next());

                                    articlePage = blogService.findByTags(page, pageSize, product.getName());
                                    log.info("【产品案例文章】={}",articlePage);

                                    if (!CollectionUtils.isEmpty(product.getImgs())) {
                                        List<Image> imgList = product.getImgs().stream().map(id -> imageService.findById(id)).collect(Collectors.toList());
                                        model.addAttribute("imgList", imgList);
                                        log.info("【产品轮播图片】 = {}", imgList);
                                    }

                                    if (!CollectionUtils.isEmpty(product.getDevImgs())) {
                                        List<Image> devImgList = product.getDevImgs().stream().map(id -> imageService.findById(id)).collect(Collectors.toList());
                                        model.addAttribute("devImgList", devImgList);
                                        log.info("【产品研发工具图片】 = {}", devImgList);
                                    }

                                    if (StringUtils.isNotBlank(product.getBlogId())){
                                        Blog blog = blogService.findOnBlog(product.getBlogId());
                                        log.info("【产品详情文章】={}",blog);
                                        model.addAttribute("blog", blog);
                                    }

                                    if (product.getFiles() != null && product.getFiles().size() > 0) {
                                        Set<String> files = new HashSet<>(product.getFiles());
                                        fileDetail = fileService.findByIds(files, pageSize, page);
                                        log.info("【产品文件】={}",fileDetail);
                                    }
                                }

                                model.addAttribute("articlePage", articlePage);
                                model.addAttribute("fileDetail", fileDetail);
                                model.addAttribute("product", product);
                            }
                        }
                    }


                    model.addAttribute("keyword", keyword);
                    model.addAttribute(td.getKey() + "_product", productPage);
                    model.addAttribute(td.getKey() + "_productName", productName);
                    log.info("【分类产品】={}", productPage.getItems());
                    log.info("【分类产品Key】={}", td.getKey() + "_product");

                    //查询分类下的图片
                    MyPage<Image> imagePage = imageService.findImageByCategroys(cids,page,pageSize);
                    model.addAttribute(td.getKey()+"_img"+td.getOrder(  ), imagePage);
                    log.info("【category】图片Key={}",td.getKey()+"_img"+td.getOrder());

                    model.addAttribute("key"+td.getOrder(), td.getKey());
                    model.addAttribute("cid",cid);

                }else if (StringUtils.equals(td.getType(), "image")) { //如果模板数据属于image类型
                    Set<String> imageIds = td.getIds();
                    List<Image> imageList = new ArrayList();
                    imageIds.stream().forEach( x ->  imageList.add(imageService.findById(x)));
                    model.addAttribute(td.getKey(), imageList);
                    log.info("imageList={}", imageList);
                    model.addAttribute("key", td.getKey());
                } else if (StringUtils.equals(td.getType(), "form")) { //如果模板数据属于tags类型
                    //根据模板数据里的标签名称列表查询文章列表
                    Set<String> cids = td.getIds();
                    List<Form> formList = new ArrayList<>();
                    /*if (StringUtils.isNotBlank(cid)) {
                        entityManager.unwrap(Session.class).clear();
                        cids.clear();
                        cids.add(cid);
                        formList = formService.findByCataForm(cids);
                    }else {
                        formList = formService.findByIds(cids);
                    }*/
                    formList = formService.findByIds(cids);

                    List<FormItem> formItemList = new ArrayList<>();
                    Form form = new Form();
                    List<String> allType = Arrays.asList("text", "checkbox", "radio", "textarea");
                    if (formList != null && formList.size() > 0) {
                        form = formList.get(0);
                        formItemList = formItemService.findByForm(form.getId());
                        log.info("formItemList=={}",formItemList);
                        log.info("td.getKey=={}",td.getKey());
                        //判断哪些表单项类型是没有的
                        allType = formItemService.findAllType(form.getId());
                       //重新将排序和keyValue进行排序
                        formItemService.SetSortOrKey(form.getId());
                    }
                    //填充数据
                    model.addAttribute(td.getKey(), formItemList);
                    model.addAttribute("allType", allType);
                    model.addAttribute("formId", form.getId());
                } else if (StringUtils.equals(td.getType(), "file")) {
                    Set<String> fileIds = td.getIds();
                    MyPage<FileUpload> filePage = fileService.findByIds(fileIds,pageSize,page);
                    model.addAttribute(td.getKey(), filePage);
                    log.info("fileUploadList={}", filePage);
                    model.addAttribute("key", td.getKey());
                }else if (StringUtils.equals(td.getType(), "worktable")){
                    String workId = td.getIds().iterator().next();
                    MyPage<Product> productPage = productService.findPageByWidProduct(workId,page,pageSize);
                    model.addAttribute(td.getKey(), productPage);
                    log.info("productPage={}", productPage);
                    model.addAttribute("key", td.getKey());
                }
                //返回博客id，页面高亮显示
                if (null != ids && ids.size() > 0) {
                    model.addAttribute("id", ids.iterator().next());
                }else {
                    model.addAttribute("id", null);
                }
            }



            List<Customer> customerList = customerService.findAll();
            Customer customer;
            if (customerList == null && customerList.size() == 0) {
                customer = new Customer();
            }else {
                customer = customerList.get(0);
            }
            model.addAttribute("customer", customer);
            model.addAttribute("url", template.getUrl());
            String title = "";
            String description = "";
            String keywords = "";
            if (StringUtils.isNotBlank(cid)) {
                Cateitem cateitem = category.findCateitem(cid);
                if (StringUtils.isNotBlank(cateitem.getTitle()))
                    title = cateitem.getTitle();
                else
                    title = template.getTitle();
                if (StringUtils.isNotBlank(cateitem.getDescription()))
                    description = cateitem.getDescription();
                else
                    description = template.getDescription();
                if (StringUtils.isNotBlank(cateitem.getKeywords()))
                    keywords = cateitem.getKeywords();
                else
                    keywords = template.getKeywords();
            } else {
                title = template.getTitle();
                description = template.getDescription();
                keywords = template.getKeywords();
            }

            Cateitem cateitem = category.findCateitem(cid);
            if (cateitem != null) {
                model.addAttribute("cateName", cateitem.initials(cateitem.getText()));
            }
            model.addAttribute("title", title);
            model.addAttribute("description", description);
            model.addAttribute("keywords", keywords);

            return lang+"/"+template.getUrl();
        } else
            return "error";
    }
}


