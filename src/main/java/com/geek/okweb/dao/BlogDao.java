package com.geek.okweb.dao;


import com.geek.okweb.domain.Blog;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Transactional
@Component
@Slf4j
public class BlogDao extends BaseDao<Blog> {

    public void save(Blog blog){
        getSession().save(blog);
    }


    /*
     * 删除文章
     */
    public void delete(String blogId){
        Blog blog = findById(blogId);
        getSession().delete(blog);
    }

    /**
     * 根据文章id查询
     * @param id
     * @return
     */
    public Blog findById(String id){
         return getSession().get(Blog.class, id);
    }

    /*
     * 删除文章
     */
    public void delete(Blog blog) {
        getSession().delete(blog);
    }


    //查询博客(不带模糊查询)
    public MyPage<Blog> findPageAll(int page, int pageSize, Integer status, Integer review) {
        DetachedCriteria dc = DetachedCriteria.forClass(Blog.class);
        dc.add(Restrictions.eq("status", status));
        dc.add(Restrictions.eq("review", review));
        dc.addOrder(Order.desc("isTop")).addOrder(Order.desc("createTime"));
        return findPageByCriteria(dc, pageSize, page);
    }

    /*public MyPage<Blog> findOrderArticle(int page, int pageSize, Integer status, Integer review, String flag) {
        DetachedCriteria dc = DetachedCriteria.forClass(Blog.class);
        dc.add(Restrictions.eq("status", status));
        dc.add(Restrictions.eq("review", review));
        if (StringUtils.equals(flag, "asc")) { //倒序
            dc.addOrder(Order.desc("isTop")).addOrder(Order.asc("createTime"));
        } else {
            dc.addOrder(Order.desc("isTop")).addOrder(Order.desc("createTime"));
        }
        return findPageByCriteria(dc, pageSize, page);
    }*/

    /**
     * 产品查询文章
     * @return
     */
    public Blog findOneByStatusAndReview(String id){
        DetachedCriteria dc = DetachedCriteria.forClass(Blog.class);
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.add(Restrictions.eq("status", 0));
        criteria.add(Restrictions.eq("review", 1));
        criteria.add(Restrictions.eq("id", id));
        return (Blog) criteria.uniqueResult();
    }

    /**
     * 更新文章
     * @param blog
     */
    public void update(Blog blog){
      getSession().update(blog);
    }

    /**
     * 更新文章
     * @param blog
     */
    public void merge(Blog blog){
        getSession().merge(blog);
    }

    /**
     * 查询所有
     */
    public List<Blog> findAll() {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Blog.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.desc("createTime"));
        List<Blog> list = criteria.list();
        return list;
    }


    public List<Blog> findByIdByArticle(String id){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Blog.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.desc("createTime"));
        criteria.add(Restrictions.eq("id",id));
        List<Blog> blogList = criteria.list();
        return blogList;
    }

    /**
     * 根据标签查询文章
     * @return
     */
    @Transactional(readOnly = true)
    public MyPage<Blog> findByTags(Integer page,Integer pageSize,String tags){
        Integer startIndex = (page - 1) * pageSize;
        String sql = "SELECT * FROM blog WHERE status = 0 AND review = 1 AND FIND_IN_SET(:tag,tags) limit "+startIndex+","+pageSize+"";

        NativeQuery<Blog> query = getSession().createNativeQuery(sql, Blog.class);

        query.setParameter("tag", tags);
        log.info("【sql】={}",sql);
        List<Blog> blogList = query.getResultList();
        log.info("【数据】={}",blogList);
        Integer totalcount = findByTagsCount(page, pageSize, tags);
        MyPage<Blog> blogPage = new MyPage<>(totalcount,pageSize,page);
        blogPage.setItems(blogList);
        return blogPage;
    }

    public Integer findByTagsCount(Integer page,Integer pageSize,String tags){
        Integer startIndex = (page - 1) * pageSize;
        String sql = "SELECT * FROM blog WHERE status = 0 AND review = 1 AND FIND_IN_SET(:tag,tags)";

        NativeQuery<Blog> query = getSession().createNativeQuery(sql, Blog.class);

        query.setParameter("tag", tags);
        List<Blog> blogList = query.getResultList();
        return blogList.size();
    }

    /**
     * 根据标签查询文章
     * @return
     */
    @Transactional(readOnly = true)
    public List<Blog> findByTags(Set<String> categorys,Integer page,Integer pageSize,String flag,Integer review,String language,Integer isHidden,String sort){
        log.info("【Blogdao标签】={}",categorys);
        Integer startIndex = (page-1)*pageSize;
        List<Blog> blogList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM blog WHERE (";
            for (int i=0; i<categorys.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<categorys.size()-1)
                    sql += "OR ";
            }
            sql +=") AND status=0 AND review="+review+"";

            if (StringUtils.isNotBlank(language)){
                sql += " AND language="+'\''+language+'\'';
            }

            if (isHidden != null){
                sql += " AND is_hidden="+'\''+isHidden+'\'';
            }

            if ("asc".equals(sort)) {
                sql +=" ORDER BY is_top DESC, number ASC, create_time ASC ";
            }else {
                sql +=" ORDER BY is_top DESC, number ASC, create_time DESC ";
            }

            if ("total".equals(flag)){
            }else {
                sql +=" limit "+startIndex+","+pageSize+"";
            }

            log.info(sql);
            Query query = getSession().createNativeQuery(sql, Blog.class);
            int i=0;
            for (String id:categorys) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
           blogList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return blogList;
    }


    /**
     * 根据标签查询文章
     * @return
     */
    @Transactional(readOnly = true)
    public List<Blog> findByRecoveryTags(Set<String> categorys,Integer page,Integer pageSize,String flag){
        log.info("【Blogdao标签】={}",categorys);
        Integer startIndex = (page-1)*pageSize;
        List<Blog> blogList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM blog WHERE (";
            for (int i=0; i<categorys.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<categorys.size()-1)
                    sql += "OR ";
            }
            sql +=") AND status=1";

            if ("total".equals(flag)){
                sql +=" ORDER BY is_top DESC,create_time DESC ";
            }else {
                sql +=" ORDER BY is_top DESC,create_time DESC limit "+startIndex+","+pageSize+"";
            }

            log.info(sql);
            Query query = getSession().createNativeQuery(sql, Blog.class);
            int i=0;
            for (String id:categorys) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
           blogList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return blogList;
    }

    /**
     * 根据ids查询文章
     * @param ids 文章id集合
     * @return
     */
    public List<Blog> findByIds(Set<String> ids,Integer page,Integer pageSize,String flag,String language){

        List<Blog> blogList = new ArrayList<>();
        Integer startIndex = (page-1)*pageSize;
        try {
            String sql = "SELECT * FROM blog WHERE (";
            for (int i=0; i<ids.size(); i++) {
                sql += "blog_id = :id"+i+"";
                if (i<ids.size()-1)
                    sql += " OR ";
            }
            sql +=") AND status=0 AND review=1 ";

            if (StringUtils.isNotBlank(language)){
                sql += " AND language="+'\''+language+'\'';
            }

            if ("total".equals(flag)){
                sql +=" ORDER BY is_top DESC,create_time DESC ";
            }else {
                sql +=" ORDER BY is_top DESC,create_time DESC limit "+startIndex+","+pageSize+"";
            }
            Query query = getSession().createNativeQuery(sql, Blog.class);
            int i=0;
            for (String id:ids) {
                query.setParameter("id"+i, ""+id+"");
                i++;
            }
            log.info(sql);
           blogList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return blogList;
    }

    /**
     * 根据文章回收状态查询文章
     * @param status 0代表未放入回收站，1代表已放入回收站
     * @return
     */
    public MyPage<Blog> findAllByStatus(Integer status,Integer page,Integer pageSize){
        return findAllByStatus(DetachedCriteria.forClass(Blog.class),status,page,pageSize);
    }

    /**
     * 恢复文章
     * @param id 文章id
     */
    public void restore(String id) {
        Blog blog = findById(id);
        blog.setStatus(0);
        update(blog);
    }

    /**
     * 审核文章
     * @param blogId 文章id
     */
    public void reviewArticle(String blogId,Integer review) {
        Blog blog = findById(blogId);
        blog.setReview(review);
        update(blog);
    }

    /**
     * 根据文章回收状态和文章审核状态查询文章进行排序
     * @param status 回收状态0代表未放入回收站，1代表已放入回收站
     * @param review 审核状态  0代表未审核，1代表已审核通过,2代表审核未通过
     * @return
     */
    public MyPage<Blog> findAllByStatusAndReviewAndSort(Integer status, Integer review,Integer pageSize,Integer page,String action) {
        DetachedCriteria dc = DetachedCriteria.forClass(Blog.class);
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.add(Restrictions.and(Restrictions.eq("status", status), Restrictions.eq("review", review)));
        if ("asc".equals(action)) {
            criteria.addOrder(Order.desc("isTop")).addOrder(Order.asc("createTime"));
        } else {
            criteria.addOrder(Order.desc("isTop")).addOrder(Order.desc("createTime"));
        }
        Integer totalCount = getTotalCount(status,review);
        return findPageByCriteria(totalCount,dc,pageSize,page);
    }

    /**
     * 根据文章回收状态和文章审核状态查询文章进行排序(不带分页查询结果)
     * @param status 回收状态0代表未放入回收站，1代表已放入回收站
     * @param review 审核状态  0代表未审核，1代表已审核通过,2代表审核未通过
     * @return
     */
    public List<Blog> findAllByStatusAndReviewAndSort(Integer status, Integer review, String cid, String action) {
        String sql = "SELECT * FROM blog WHERE (JSON_contains(cateids,:id)) AND status = "+status+" AND review = "+review+" ORDER BY is_top DESC,create_time "+action+"";
        Query query = getSession().createNativeQuery(sql, Blog.class);
        query.setParameter("id", "\"" + cid + "\"");
        return query.getResultList();
    }

    /**
     * 根据文章回收状态和文章审核状态查询文章
     * @param status 回收状态0代表未放入回收站，1代表已放入回收站
     * @param review 审核状态  0代表未审核，1代表已审核通过,2代表审核未通过
     * @return
     */
    public List<Blog> findAllByStatusAndReview(Integer status, Integer review) {
        DetachedCriteria dc = DetachedCriteria.forClass(Blog.class);
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.add(Restrictions.and(Restrictions.eq("status", status), Restrictions.eq("review", review)));
        criteria.addOrder(Order.desc("isTop")).addOrder(Order.desc("createTime"));
        return criteria.list();
    }

    public void isTopArticle(String id,Integer top) {
        Blog blog = findById(id);
        blog.setIsTop(top);
        update(blog);
    }

    //模糊查询审核通过以及未在回收站的文章的个数
    public int getSearchCount(String searchName,String keyword,String action){
        String sql = null;
        if (StringUtils.isNotBlank(action)&&StringUtils.equals("recovery",action)){
            sql = "where status = 1 and "+searchName+" like '%"+keyword+"%'";
        }else {
            sql = "where status = 0 and review = 1 and "+searchName+" like '%"+keyword+"%'";
        }
        return getCustomizeCount("blog",sql);
    }

    //模糊查询审核通过以及未在回收站的文章的个数
    public int getSearchCount(String searchName,String keyword,String action,Integer review){
        String sql = null;
        if (StringUtils.isNotBlank(action)&&StringUtils.equals("recovery",action)){
            sql = "where status = 1 and "+searchName+" like '%"+keyword+"%'";
        }else if (review == 4){
            sql = "where status = 0 and "+searchName+" like '%"+keyword+"%'";
        }else {
            sql = "where status = 0 and review = "+review+" and "+searchName+" like '%"+keyword+"%'";
        }
        return getCustomizeCount("blog",sql);
    }

    public int getSearchCount(Integer status,Integer review,Integer isHidden,String searchName,String keyword){
        String sql = "where status = "+status+" and review = "+review+" and is_hidden = "+isHidden+" and "+searchName+" like '%"+keyword+"%'";;
        return getCustomizeCount("blog", sql);
    }

    public int getSearchCount(Integer status,Integer review,Integer isHidden,String searchName,String keyword, String cateName){
        String sql = "where status = " + status + " and review = " + review + " and is_hidden = " + isHidden + " and " + "cate_name = " + "\""+cateName+"\"" + " and " + searchName + " like '%" + keyword + "%'";;
        log.info("sql={}",sql);
        return getCustomizeCount("blog", sql);
    }

    //查询审核通过以及未在回收站的文章的个数
    public int getTotalCount(Integer status,Integer review){
        String sql = "where status = "+status+" and review = "+review;
        return getCustomizeCount("blog",sql);
    }

    public int getTotalCount(Integer status,Integer review,Integer isHidden){
        String sql = "where status = "+status+" and review = "+review+" and isHidden = "+isHidden;
        return getCustomizeCount("blog",sql);
    }

    /**
     * 搜索文章分页
     * @param page 当前页
     * @param pageSize 每页显示数量
     * @param searchName 根据那个字段进行查询
     * @param keyword 关键字
     * @param action 操作   recovery为回收站的文章数据进行搜索分页
     * @return
     */
    public MyPage<Blog> searchBlog(Integer page, Integer pageSize, String searchName, String keyword,String action) {
        DetachedCriteria dc = DetachedCriteria.forClass(Blog.class);
        if (StringUtils.isNotBlank(action)&&StringUtils.equals("recovery",action)){
            Integer totalCount = getSearchCount(searchName,keyword,"recovery");
            return findPageBySearchCriteria(dc, totalCount, "recovery" , searchName, keyword, pageSize, page);
        }
        dc.add(Restrictions.eq("status",0));
        dc.add(Restrictions.eq("review",1));
        dc.addOrder(Order.desc("isTop")).addOrder(Order.desc("createTime"));
        Integer totalCount = getSearchCount(searchName,keyword,null);
        return findPageBySearchCriteria(dc, totalCount, null , searchName, keyword, pageSize, page);
    }
    /**
     * 搜索文章分页
     * @param page 当前页
     * @param pageSize 每页显示数量
     * @param searchName 根据那个字段进行查询
     * @param keyword 关键字
     * @param action 操作   recovery为回收站的文章数据进行搜索分页
     * @return
     */
    public MyPage<Blog> searchBlog(Integer page, Integer pageSize,Integer review,String searchName, String keyword,String action) {
        DetachedCriteria dc = DetachedCriteria.forClass(Blog.class);
        if (StringUtils.isNotBlank(action)&&StringUtils.equals("recovery",action)){
            Integer totalCount = getSearchCount(searchName,keyword,"recovery");
            return findPageBySearchCriteria(dc, totalCount, "recovery" , searchName, keyword, pageSize, page);
        }
        dc.add(Restrictions.eq("status",0));
        dc.add(Restrictions.eq("review",review));
        dc.addOrder(Order.desc("isTop")).addOrder(Order.desc("createTime"));
        Integer totalCount = getSearchCount(searchName,keyword,null,review);
        return findPageBySearchCriteria(dc, totalCount, null , searchName, keyword, pageSize, page);
    }

    public MyPage<Blog> searchBlog(Integer page, Integer pageSize, String searchName, String keyword) {
        DetachedCriteria dc = DetachedCriteria.forClass(Blog.class);
        int totalCount = getSearchCount(searchName, keyword, null, 4);
        log.info("【搜索totalCount=】={}",totalCount);
        return findPageBySearchCriteria(dc, totalCount, null, searchName, keyword, pageSize, page);
    }

    /**
     * 根据标签查询文章
     * @return
     */
    @Transactional(readOnly = true)
    public List<Blog> findByCategory(Set<String> categorys,Integer page,Integer pageSize,String flag){
        log.info("【Blogdao标签】={}",categorys);
        Integer startIndex = (page-1)*pageSize;
        List<Blog> blogList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM blog WHERE (";
            for (int i=0; i<categorys.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<categorys.size()-1)
                    sql += "OR ";
            }
            sql +=") AND status=0";

            if ("total".equals(flag)){
                sql +=" ORDER BY is_top DESC,create_time DESC ";
            }else {
                sql +=" ORDER BY is_top DESC,create_time DESC limit "+startIndex+","+pageSize+"";
            }

            log.info(sql);
            Query query = getSession().createNativeQuery(sql, Blog.class);
            int i=0;
            for (String id:categorys) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            blogList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return blogList;
    }

    public MyPage searchBlog(Integer page, Integer pageSize, String keyword, Integer status, Integer review, Integer isHidden, String cateName, String lang) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Blog.class);
        int totalCount = getSearchCount(status, review, isHidden, "title", keyword);
        detachedCriteria.add(Restrictions.eq("review", review));
        detachedCriteria.add(Restrictions.eq("isHidden", isHidden));
        detachedCriteria.add(Restrictions.eq("language", lang));
//        detachedCriteria.add(Restrictions.not(Restrictions.in("cateName", "产品中心")));
        if (StringUtils.isNotBlank(cateName)) {
            detachedCriteria.add(Restrictions.eq("cateName", cateName));
        }
        return findPageBySearchCriteria(detachedCriteria, totalCount, null, "title", keyword, pageSize, page);
    }

    /*public MyPage searchBlog(Integer page, Integer pageSize, String keyword, Integer status, Integer review, Integer isHidden, String cateName) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Blog.class);
        int totalCount = getSearchCount(status, review, isHidden, "title", keyword,cateName);
        detachedCriteria.add(Restrictions.eq("review", review));
        detachedCriteria.add(Restrictions.eq("isHidden", isHidden));
        detachedCriteria.add(Restrictions.eq("cateName", cateName));
        return findPageBySearchCriteria(detachedCriteria, totalCount, null, "title", keyword, pageSize, page);
    }*/



    public Blog findByCategoryAndTitle(String cid, String title, Integer review, Integer status, Integer isHidden, String language) {
        String sql = "SELECT * FROM blog WHERE (";
               sql += "JSON_contains(cateids,:id) ";
               sql +=") AND status="+status+" AND review="+review+" AND is_hidden="+isHidden+" AND language="+'\''+language+'\''+" AND title="+'\''+title+'\''+"";
               log.info(sql );
       Query query = getSession().createNativeQuery(sql, Blog.class);

       query.setParameter("id", "\""+cid+"\"");
        return (Blog) query.uniqueResult();
    }

    public Blog findByTitle(String title) {
        DetachedCriteria dc = DetachedCriteria.forClass(Blog.class);
        dc.add(Restrictions.eq("title", title));
        Criteria criteria = dc.getExecutableCriteria(getSession());
        return (Blog) criteria.uniqueResult();
    }

    public List<Blog> findAllByCid(String cid, Integer review, Integer status) {
        String sql = "SELECT * FROM blog WHERE (JSON_contains(cateids,:id)) AND status="+ status +" AND review=" + review;
        Query query = getSession().createNativeQuery(sql, Blog.class);
        query.setParameter("id", "\""+cid+"\"");
        log.info("【sql语句】 = {}", sql);
        return query.getResultList();
    }

    public List<Blog> findAllByCid(String cid, Integer review, Integer status, Integer isHidden) {
        String sql = "SELECT * FROM blog WHERE (JSON_contains(cateids,:id)) AND status="+ status +" AND review=" + review + " AND is_hidden=" + isHidden;
        Query query = getSession().createNativeQuery(sql, Blog.class);
        query.setParameter("id", "\""+cid+"\"");
        log.info("【sql语句】 = {}", sql);
        return query.getResultList();
    }
}
