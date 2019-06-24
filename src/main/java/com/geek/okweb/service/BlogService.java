package com.geek.okweb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geek.okweb.dao.BlogDao;
import com.geek.okweb.dao.CategoryDao;
import com.geek.okweb.domain.Blog;
import com.geek.okweb.domain.Category;
import com.geek.okweb.domain.Cateitem;
import com.geek.okweb.domain.User;
import com.geek.okweb.exception.BlogException;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class BlogService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryDao categoryDao;


    /**
     * 保存博客文章
     * @param
     * @param blog
     */
    public Blog  saveBlog(Blog blog,String userId) throws Exception{
        //判断博客是否为空
        if (blog == null){
            throw  new BlogException("博客为空",2);
        }else {
            User user = userService.findByUserId(userId);
            blog.setUser(user);

            Category category = categoryService.findOne();
            categoryDao.detach(category);
            ObjectMapper mapper = new ObjectMapper();
            blog.setCateitems(mapper.writeValueAsString(category
                    .getCateitems()));
            List<Cateitem> cateitems = new ArrayList<>();
            for (String cateId : blog.getCateids()){
                cateitems.add(category.findCateitem(cateId));
            }
            String names = cateitems.stream().map((x) -> x.getText()).collect(Collectors.joining(","));
            blog.setCateName(names);

//            if (blog.getId() == null){
//                blog.setId(KeyUtil.UUID());
//                //保存文章
//                blogDao.save(blog);
//                log.info("【保存】");
//            }else {
//                blog.setCreateTime(new Date());
                blogDao.merge(blog);
                log.info("【发布文章操作】");
            }

            return blog;

            //重新渲染模板
            //templatePackage.updateIndexTemplate("index",tagList);
        }

    public Blog findOnBlog(String id){
        return blogDao.findOneByStatusAndReview(id);
    }

    /**
     * 查询博客
     */
    public MyPage<Blog> findPageAll(int page, int pageSize,Integer status,Integer review){
        return blogDao.findPageAll(page,pageSize,status,review);
    }


    /**
     * 根据id查询博客
     * @param
     * @return
     */
    public Blog findBlogById(String blogId){
        return blogDao.findById(blogId);
    }

    /**
     * 添加阅读量
     */
    public void addRead(String blodId){
        Blog blog = blogDao.findById(blodId);
        Integer readCount = blog.getReadCount()+1;
        blog.setReadCount(readCount);
        blogDao.update(blog);
    }

    //更新数据库
    public void updateBlog(Blog blog){
        blogDao.update(blog);
    }

    //查询所有博客
    public List<Blog> findAllBlog(){
        List<Blog> blogList = blogDao.findAll();

        return blogList;
    }

    /**
     * 删除文章
     * @param blog
     */
    public void deleteBlog(Blog blog){
        blogDao.delete(blog);
    }

    /**
     * 根据id删除文章
     * @param id
     */
    public void deleteArticleById(String id){
        blogDao.delete(id);
    }

    /**
     * 根据分类查询博客
     * @param categorys
     * @return
     */
    public MyPage<Blog> findByCategorys(Set<String> categorys,Integer page,Integer pageSize,Integer review,String language,Integer isHidden){
        String sort = null;
        List<Blog> articlesScope = findAllByCid(categorys.iterator().next(), review, 0);
        if (articlesScope != null && articlesScope.size() > 0) {
            sort = articlesScope.get(0).getSort();
        }

        List<Blog> blogList = blogDao.findByTags(categorys,page,pageSize,"total", review, language, isHidden,null);
        log.info("【分类文章】集合数量={}",blogList.size());
        MyPage<Blog> myPage = new MyPage(blogList.size(),pageSize,page);
        List<Blog> limitResult = blogDao.findByTags(categorys,page,pageSize,null, review, language, isHidden, sort);
        myPage.setItems(limitResult);
        return myPage;
    }

    public List<Blog> findByAllCategorys(Set<String> categorys,Integer page,Integer pageSize,Integer review,String language,Integer isHidden) {
        String sort = null;
        List<Blog> areiclesScope = findAllByCid(categorys.iterator().next(), review, 0);
        if (areiclesScope != null && areiclesScope.size() > 0) {
            sort = areiclesScope.get(0).getSort();
        }

        List<Blog> blogList = blogDao.findByTags(categorys, page, pageSize, "total", review, language, isHidden, sort);
        return blogList;
    }

    /**
     * 根据分类，审核状态查询博客
     *
     * @param categorys
     * @return
     */
    public MyPage<Blog> findByCategorys(Set<String> categorys, Integer page, Integer pageSize, Integer review, String action) {
        List<Blog> blogList = blogDao.findByTags(categorys, page, pageSize, "total", review, null, null,null);
        log.info("集合数量={}", blogList.size());
        MyPage<Blog> myPage = new MyPage(blogList.size(), pageSize, page);
        List<Blog> limitResult = blogDao.findByTags(categorys, page, pageSize, null, review, null, null, action);
        myPage.setItems(limitResult);
        return myPage;
    }

    /**
     * 根据id查询博客
     * @param ids
     * @return
     */
    public MyPage<Blog> findByIds(Set<String> ids,Integer page,Integer pageSize,String language){
        List<Blog> blogList = blogDao.findByIds(ids,page,pageSize,"total",language);
        log.info("集合数量={}",blogList.size());
        MyPage<Blog> myPage = new MyPage(blogList.size(),pageSize,page);
        List<Blog> limitResult = blogDao.findByIds(ids,page,pageSize,null,language);
        myPage.setItems(limitResult);
        return myPage;
    }

    public Blog findById(String id) {
        Blog article = new Blog();
        try {
            article = blogDao.findById(id);
            if (article != null) {
                Category category = categoryService.findOne();
                categoryDao.detach(category);
                ObjectMapper mapper = new ObjectMapper();

                article.setCateitems(mapper.writeValueAsString(category
                        .getCateitems()));
        }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return article;
    }


    public Blog findOne(String id){
        return blogDao.findById(id);
    }

/*
    //查询所有博客
    public List<Blog> findAllByStatus(Integer status){
        return blogDao.findAllByStatus(status);
    }*/

    /**
     * 审核文章
     * @param blogId 文章id
     */
    public void reviewArticle(String blogId,Integer review) {
        blogDao.reviewArticle(blogId,review);
    }

    public MyPage<Blog> findAllByStatusAndReview(Integer status, Integer review, Integer pageSize, Integer page, String action) {
        return blogDao.findAllByStatusAndReviewAndSort(status, review, pageSize, page, action);
    }

    public List<Blog> findAllByStatusAndReviewAndSort(Integer status, Integer review, String cid, String action) {
        return blogDao.findAllByStatusAndReviewAndSort(status, review, cid, action);
    }

    public List<Blog> findAllByStatusAndReview(Integer status, Integer review) {
        return blogDao.findAllByStatusAndReview(status,review);
    }

    public void isTopArticle(String id,Integer top) {
        blogDao.isTopArticle(id,top);
    }


    public MyPage<Blog> findAllByStatus(Integer status,Integer page,Integer pageSize){
        return blogDao.findAllByStatus(status,page,pageSize);
    }

    /**
     * 根据分类查询博客
     * @param categorys
     * @return
     */
    public MyPage<Blog> findByArticleCategory(Set<String> categorys,Integer page,Integer pageSize){
        List<Blog> blogList = blogDao.findByCategory(categorys,page,pageSize,"total");
        log.info("集合数量={}",blogList.size());
        MyPage<Blog> myPage = new MyPage(blogList.size(),pageSize,page);
        List<Blog> limitResult = blogDao.findByCategory(categorys,page,pageSize,null);
        myPage.setItems(limitResult);
        return myPage;
    }

    public void updateDeleteStatus(String id,Integer status){
        Blog blog = blogDao.findById(id);
        blog.setStatus(status);
        blogDao.update(blog);
    }

    public String getCategoryName(Category category,List<String> cateids){
        if (cateids.size() > 0 && cateids != null){
            List<Cateitem> cateitems = new ArrayList<>();
            for (String cid : cateids) {
                cateitems.add(category.findCateitem(cid));
            }
            String cateStr = cateitems.stream().map((x) -> x.getText()).collect(Collectors.joining(","));
            return cateStr;
        }else {
            return "";
        }
    }

    public MyPage<Blog> findByTags(Integer page,Integer pageSize,String tags){
        return blogDao.findByTags(page, pageSize, tags);
    }

    public MyPage<Blog> searchBlog(Integer page, Integer pageSize, String keyword, Integer status, Integer review, Integer isHidden, String cateName, String lang) {
        return blogDao.searchBlog(page, pageSize, keyword, status, review, isHidden, cateName, lang);
    }

    /*public MyPage<Blog> searchBlog(Integer page, Integer pageSize, String keyword, Integer status, Integer review, Integer isHidden, String cateName) {
        return blogDao.searchBlog(page, pageSize, keyword, status, review, isHidden, cateName);
    }*/

    public Blog findByCategoryAndTitle(String cid, String title, Integer review, Integer status, Integer isHidden, String language){
        return blogDao.findByCategoryAndTitle(cid, title, review, status, isHidden, language);
    }

    /**
     * 根据分类ID查询文章
     * @param cid
     * @param review
     * @param status
     * @return
     */
    public List<Blog> findAllByCid(String cid, Integer review, Integer status) {
        return blogDao.findAllByCid(cid, review, status);
    }

    /**
     * 根据分类ID查询文章
     * @param cid
     * @param review
     * @param status
     * @return
     */
    public List<Blog> findAllByCid(String cid, Integer review, Integer status, Integer isHidden) {
        return blogDao.findAllByCid(cid, review, status, isHidden);
    }

    /**
     * 根据文章标题查询
     * @param title
     * @return
     */
    public boolean findbyTitle(String title) {
        Blog blog = blogDao.findByTitle(title);
        if (blog != null) {
            return true;
        } else {
            return false;
        }
    }

    public Blog findByTitle(String title) {
        return blogDao.findByTitle(title);
    }

    public void readCount(Blog blog) {
        blog.setReadCount(blog.getReadCount() + 1);
        updateBlog(blog);
    }

}
