package com.geek.okweb.dao;

import com.geek.okweb.domain.Product;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Create by Gai on 2018/12/11 09:12
 */
@Slf4j
@Component
@Transactional
public class ProductDao extends BaseDao<Product>{

    public void save(Product product){
        getSession().save(product);
    }

    public List<Product> findByWideProduct(String wid){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Product.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.desc("createTime"));
        criteria.add(Restrictions.eq("wid",wid));
        List<Product> productList = criteria.list();
        return productList;
    }

    public MyPage<Product> findByWideProduct(String wid,Integer page,Integer pageSize){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Product.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.desc("createTime"));
        criteria.add(Restrictions.eq("wid",wid));
        MyPage<Product> productPage = findPageByCriteria(detachedCriteria, pageSize, page);
        return productPage;
    }

    /*public List<Product> findProduct(){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Product.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.createAlias("perms","p");
        criteria.add(Restrictions.in("p.uuid", perms));    //join 查询，出现重复数据
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);    //设置ENTITY级的DISTINCT模式，根实体
        criteria.list();
    }*/


    /**
     * 根据标签查询产品
     * @return
     */
    @Transactional(readOnly = true)
    public List<Product> findByTags(Set<String> categorys,Integer page,Integer pageSize,String flag){
        log.info("【ProductDao标签】={}",categorys);
        Integer startIndex = (page-1)*pageSize;
        List<Product> productList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM product WHERE ";
            for (int i=0; i<categorys.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<categorys.size()-1)
                    sql += "OR ";
            }

            if ("total".equals(flag)){
                sql +=" ORDER BY create_time DESC ";
            }else {
                sql +=" ORDER BY create_time DESC limit "+startIndex+","+pageSize+"";
            }

            log.info(sql);
            Query query = getSession().createNativeQuery(sql,Product.class);
            int i=0;
            for (String id:categorys) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            productList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return productList;
    }


    public Product findById(String id){
        Product product = getSession().find(Product.class, id);
        return product;
    }

    public void merge(Product product){
        getSession().merge(product);
    }

    public void delete(String id) {
        Product product = findById(id);
        getSession().delete(product);
    }


    public List<Product> findByIds(Set<String> ids) {

        List<Product> productList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM product WHERE ";
            for (int i=0; i<ids.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<ids.size()-1)
                    sql += "OR ";
            }
            sql +=" ORDER BY create_time DESC ";
            Query query = getSession().createNativeQuery(sql, Product.class);
            int i=0;
            for (String id:ids) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            productList = query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
        return productList;
    }


    public void update(Product product) {
        getSession().update(product);
    }

//    public List<Product> findAllProduct() {
//        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Product.class);
//        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
//        criteria.addOrder(Order.desc("createTime"));
//        List<Product> productList = criteria.list();
//        return productList;
//    }


    public List<Product> findByWid(String wid) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Product.class);
        detachedCriteria.add(Restrictions.eq("wid",wid));
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        List productList = criteria.list();
        return productList;
    }


    public MyPage<Product> findWidByPage(Integer status, Integer page ,Integer pageSize,String wid) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Product.class);
        detachedCriteria.add(Restrictions.eq("wid",wid));
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.asc("createTime"));
        int totalCount = criteria.list().size();
        MyPage<Product> myPage = new MyPage<>(totalCount, pageSize, page);
        List productList = criteria.setFirstResult(myPage.getStartindex()).setMaxResults(pageSize).list();
        myPage.setItems(productList);
        return myPage;
    }

    //查询审核通过以及未在回收站的文件的个数
    public int getSearchCount(String searchName,String keyword,String action){
        String sql = null;
        if (StringUtils.isNotBlank(action)&& StringUtils.equals("recovery",action)){
            sql = "where "+searchName+" like '%"+keyword+"%'";
        }else {
            sql = "where "+searchName+" like '%"+keyword+"%'";
        }
        return getCustomizeCount("product",sql);
    }


    public MyPage<Product> searchProduct(Integer page, Integer pageSize, String searchName, String keyword, String action) {
        DetachedCriteria dc = DetachedCriteria.forClass(Product.class);
        if (StringUtils.isNotBlank(action)&& StringUtils.equals("recovery",action)){
            Integer totalCount = getSearchCount("name",keyword,"recovery");
            return findPageBySearchCriteria(dc, totalCount, "recovery" , searchName, keyword, pageSize, page);
        }

        //dc.add(Restrictions.eq("status",0));
        dc.addOrder(Order.desc("createTime"));
        Integer totalCount = getSearchCount("name",keyword,null);
        log.info("totalCount={},page={},pageSize={}", totalCount, page, pageSize);
        return findPageBySearchCriteria(dc, totalCount, "notOperating" , searchName, keyword, pageSize, page);
    }


    public MyPage<Product> findAllPage(Integer page, Integer pageSize) {
        DetachedCriteria dc = DetachedCriteria.forClass(Product.class);
        return findPageByCriteria(dc, pageSize, page);
    }

    public List<Product> findAll(){
        DetachedCriteria dc = DetachedCriteria.forClass(Product.class);
        Criteria criteria = dc.getExecutableCriteria(getSession());
        return criteria.list();
    }

}
