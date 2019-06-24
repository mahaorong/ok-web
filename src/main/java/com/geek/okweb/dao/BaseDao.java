package com.geek.okweb.dao;


import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

@Slf4j
@Transactional
public class BaseDao<T> {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getSession(){
        return entityManager.unwrap(Session.class);
    }

    public MyPage<T> findPageByCriteria(final DetachedCriteria detachedCriteria){
        return findPageByCriteria(detachedCriteria,MyPage.PAGESIZE,1);
    }

    public MyPage<T> findPageByCriteria(final DetachedCriteria detachedCriteria, int page ){
        return findPageByCriteria(detachedCriteria,MyPage.PAGESIZE,page);
    }

    public MyPage<T> findPageByCriteria(final DetachedCriteria detachedCriteria, final int pageSize, int page){
        log.info("已经入此方法");
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());

        int totalCount = getCountByCriteria(detachedCriteria); //计算总数

        MyPage<T> myPage = new MyPage<T>(totalCount,pageSize,page);
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<T> items = criteria.setFirstResult(myPage.getStartindex()).setMaxResults(pageSize).list();
        myPage.setItems(items);
        log.info("items={}",items);
        return myPage;
    }

    public MyPage<T> findPageByCriteria(final Integer totalCount,final DetachedCriteria detachedCriteria, final int pageSize, int page){
        log.info("已经入此方法");
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        MyPage<T> myPage = new MyPage<T>(totalCount,pageSize,page);
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<T> items = criteria.setFirstResult(myPage.getStartindex()).setMaxResults(pageSize).list();
        myPage.setItems(items);
        log.info("items={}",items);
        return myPage;
    }

    public MyPage<T> findPageByCriteria(Session session, final DetachedCriteria detachedCriteria, final int pageSize, int page) {

        Criteria criteria = detachedCriteria.getExecutableCriteria(session);

        int totalCount = getCountByCriteria(session, detachedCriteria);

        MyPage<T> myPage = new MyPage<T>(totalCount, pageSize, page);
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<T> items = criteria.setFirstResult(myPage.getStartindex()).setMaxResults(pageSize).list();

        myPage.setItems(items);
        return myPage;
    }

    public List<T> findAllByCriteria(final DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        return criteria.list();
    }

    public List<T> findAllByCriteria(Session session, final DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        return criteria.list();
    }



    /**
     * 计算总数
     * @param detachedCriteria
     * @return
     */
    public int getCountByCriteria(final DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        Object object = criteria.setProjection(Projections.rowCount()).uniqueResult();
        long totalCount = 0;
        try {
            totalCount = (Long) object;
        } catch (Exception e) {
        }
        return (int) totalCount;
    }

    /**
     * 计算总数
     * @param detachedCriteria
     * @return
     */
    public int getCountByCriteria(Session session, final DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(session);
        Object object = criteria.setProjection(Projections.rowCount()).uniqueResult();
        long totalCount = 0;
        try {
            totalCount = (Long) object;
        } catch (Exception e) {
        }
        return (int) totalCount;
    }

    /**
     * 自定义规则计算总数
     * @return
     */
    public int getCustomizeCount(String tableName,String customRule){
        String sql = "SELECT COUNT(*) FROM "+tableName+" ";
        if (StringUtils.isNotBlank(customRule)){
            sql+=customRule;
        }
        log.info("totalCountSql=={}",sql);
        Query query = getSession().createNativeQuery(sql);
        Object object1 = query.uniqueResult();
        log.info("object1=={}",object1);
        BigInteger object = (BigInteger) query.uniqueResult();
        int totalCount = 0;
        try {
            totalCount = object.intValue();
        } catch (Exception e) {
            log.info("异常=={}",e);
        }
        return totalCount;
    }

    //根据状态分页查询数据
    public MyPage<T> findAllByStatus(final DetachedCriteria detachedCriteria,Integer status,Integer page,Integer pageSize) {
        detachedCriteria.add(Restrictions.eq("status",status));
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        int count = getCountByCriteria(getSession(), detachedCriteria);
        log.info("count={}",count);
        MyPage<T> myPage = new MyPage<T>(count,pageSize,page);
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.addOrder(Order.desc("createTime"));
        List<T> items = criteria.setFirstResult(myPage.getStartindex()).setMaxResults(pageSize).list();
        myPage.setItems(items);
        log.info("items={}",items);
        return myPage;
    }

    //根据关键字搜索查询
    public List<T> search(final DetachedCriteria detachedCriteria,String searchName,String keyword){
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.add(Restrictions.like(searchName,"%"+keyword+"%"));
        criteria.addOrder(Order.desc("createTime"));
        criteria.add(Restrictions.eq("status",0));
        List<T> searchList = criteria.list();
        return searchList;
    }

    //搜索分页查询
    public MyPage<T> findPageBySearchCriteria(final DetachedCriteria detachedCriteria, final Integer totalCount, String operating, String searchName, String keyword, final int pageSize, int page){
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        MyPage<T> myPage = new MyPage<T>(totalCount,pageSize,page);
        criteria.setProjection(null);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.like(searchName,"%"+keyword+"%"));
        criteria.addOrder(Order.desc("createTime"));
        if (StringUtils.isNotBlank(operating)&&StringUtils.equals("recovery",operating)){
            criteria.add(Restrictions.eq("status",1));
        }else if (StringUtils.equals("notOperating",operating)){
        }else {
            criteria.add(Restrictions.eq("status",0));
        }
        criteria.setFirstResult(myPage.getStartindex()).setMaxResults(pageSize);
        List items = criteria.list();
        myPage.setItems(items);
        log.info("items={}",items);
        return myPage;
    }
}
