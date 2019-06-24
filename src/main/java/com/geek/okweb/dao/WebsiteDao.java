package com.geek.okweb.dao;

import com.geek.okweb.domain.Website;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@Transactional
public class WebsiteDao extends BaseDao<Website> {

    public void save(Website website){
        getSession().save(website);
    }
    public void delete(String id){
        getSession().delete(findById(id));
    }
    public void update(Website website){
        getSession().update(website);
    }
    public Website findById(String id){
        return getSession().find(Website.class,id);
    }
    public List<Website> findAll(){
        DetachedCriteria dc = DetachedCriteria.forClass(Website.class);
        Criteria criteria = dc.getExecutableCriteria(getSession());
        return criteria.list();
    }


    public Website findByUrl(String requestURI) {
        DetachedCriteria dc = DetachedCriteria.forClass(Website.class);
        dc.add(Restrictions.eq("url", requestURI));
        Criteria criteria = dc.getExecutableCriteria(getSession());
        return (Website)criteria.uniqueResult();
    }

    public MyPage<Website> findByWebPage(Integer pageSize, Integer page) {
        DetachedCriteria dc = DetachedCriteria.forClass(Website.class);
        return findPageByCriteria(dc, pageSize, page);
    }
}
