package com.geek.okweb.dao;

import com.geek.okweb.domain.*;
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
 * Create by Gai on 2018/12/13 21:21
 */
@Slf4j
@Transactional
@Component
public class WorktableDao extends BaseDao<Worktable>{

    public void save(Worktable worktable){
        getSession().save(worktable);
    }

    public List<Worktable> findAll(){
        DetachedCriteria dc = DetachedCriteria.forClass(Worktable.class);
        return findAllByCriteria(dc);
    }

    public Worktable findById(String id){
       return getSession().find(Worktable.class,id);
    }

    public void update(Worktable worktable){
        getSession().update(worktable);
    }

    public void delete(String id){
        Worktable worktable = findById(id);
        getSession().delete(worktable);
    }

    /**
     * 根据文件名查询
     * @param fileName
     * @return
     */
    public Worktable findByFileName(String fileName){
        DetachedCriteria dc = DetachedCriteria.forClass(Worktable.class);
        dc.add(Restrictions.eq("fileName",fileName));
        Criteria criteria = dc.getExecutableCriteria(getSession());
        List<Worktable> list = criteria.list();
        Worktable worktable = new Worktable();
        if (list != null && list.size() != 0){
            worktable = list.get(0);
        }
        return worktable;
    }

    public MyPage<Worktable> findByWorktablePage(Integer page,Integer pageSize){
        DetachedCriteria dc = DetachedCriteria.forClass(Worktable.class);
        dc.addOrder(Order.desc("createTime"));
        return findPageByCriteria(dc,pageSize,page);
    }

    //查询审核通过以及未在回收站的产品的个数
    public int getSearchCount(String searchName,String keyword,String action){
        String sql = null;
        if (StringUtils.isNotBlank(action)&&StringUtils.equals("recovery",action)){
            sql = "where status = 1 and "+searchName+" like '%"+keyword+"%'";
        }else {
            sql = "where status = 0 and "+searchName+" like '%"+keyword+"%'";
        }
        return getCustomizeCount("worktable",sql);
    }

    public void merge(Worktable worktable) {
        getSession().merge(worktable);
    }

    public MyPage<Worktable> searchProduct(Integer page, Integer pageSize, String searchName, String keyword,String action) {
        DetachedCriteria dc = DetachedCriteria.forClass(Worktable.class);
        if (StringUtils.isNotBlank(action)&& StringUtils.equals("recovery",action)){
            Integer totalCount = getSearchCount("file_name",keyword,"recovery");
            return findPageBySearchCriteria(dc, totalCount, "recovery" , searchName, keyword, pageSize, page);
        }
        //dc.add(Restrictions.eq("status",0));
        dc.addOrder(Order.desc("createTime"));
        Integer totalCount = getSearchCount("file_name",keyword,null);
        return findPageBySearchCriteria(dc, totalCount, null , searchName, keyword, pageSize, page);
    }

    public MyPage<Worktable> findAllByWork(Integer status, Integer page, Integer pageSize) {
        return findAllByStatus(DetachedCriteria.forClass(Worktable.class),status,page,pageSize);
    }

    public void restore(String id) {
        Worktable worktable = findById(id);
        worktable.setStatus(0);
        update(worktable);
    }



    /**
     * 根据标签查询产品
     * @return
     */
    @Transactional(readOnly = true)
    public List<Worktable> findByTags(Set<String> categorys, Integer page, Integer pageSize, String flag){
        log.info("【WorkTableDao标签】={}",categorys);
        Integer startIndex = (page-1)*pageSize;
        List<Worktable> workList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM worktable WHERE (";
            for (int i=0; i<categorys.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<categorys.size()-1)
                    sql += "OR ";
            }

            if ("total".equals(flag)){
                sql +=") AND status=0 ORDER BY create_time DESC ";
            }else {
                sql +=") AND status=0 ORDER BY create_time DESC limit "+startIndex+","+pageSize+"";
            }

            log.info(sql);
            Query query = getSession().createNativeQuery(sql,Worktable.class);
            int i=0;
            for (String id:categorys) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            workList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return workList;
    }

    /**
     * 根据标签查询产品(不带分页)
     * @return
     */
    @Transactional(readOnly = true)
    public List<Worktable> findWork(Set<String> categorys){
        List<Worktable> workList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM worktable WHERE ";
            for (int i=0; i<categorys.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<categorys.size()-1)
                    sql += "OR ";
            }
            log.info(sql);
            Query query = getSession().createNativeQuery(sql,Worktable.class);
            int i=0;
            for (String id:categorys) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            workList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return workList;
    }

    /**
     * 根据标签查询产品
     * @return
     */
    @Transactional(readOnly = true)
    public List<Worktable> findByRecycleTags(Set<String> categorys, Integer page, Integer pageSize, String flag){
        log.info("【WorkTableDao标签】={}",categorys);
        Integer startIndex = (page-1)*pageSize;
        List<Worktable> workList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM worktable WHERE (";
            for (int i=0; i<categorys.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<categorys.size()-1)
                    sql += "OR ";
            }

            if ("total".equals(flag)){
                sql +=") AND status=1 ORDER BY create_time DESC ";
            }else {
                sql +=") AND status=1 ORDER BY create_time DESC limit "+startIndex+","+pageSize+"";
            }

            log.info(sql);
            Query query = getSession().createNativeQuery(sql,Worktable.class);
            int i=0;
            for (String id:categorys) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            workList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return workList;
    }
}
