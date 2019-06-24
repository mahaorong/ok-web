package com.geek.okweb.dao;

import com.geek.okweb.domain.Authority;
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
public class AuthorityDao extends BaseDao<Authority> {

    public void save(Authority authority){
        getSession().save(authority);
    }
    public void delete(String id){
        getSession().delete(findById(id));
    }
    public void update(Authority authority){
        getSession().update(authority);
    }
    public Authority findById(String id){
        return getSession().find(Authority.class,id);
    }
    public List<Authority> findAll(){
        DetachedCriteria dc = DetachedCriteria.forClass(Authority.class);
        Criteria criteria = dc.getExecutableCriteria(getSession());
        return criteria.list();
    }


    public Authority findByUrl(String requestURI) {
        DetachedCriteria dc = DetachedCriteria.forClass(Authority.class);
        dc.add(Restrictions.eq("url", requestURI));
        Criteria criteria = dc.getExecutableCriteria(getSession());
        return (Authority)criteria.uniqueResult();
    }

    public Authority findByWid(String wid) {
        DetachedCriteria dc = DetachedCriteria.forClass(Authority.class);
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.add(Restrictions.eq("wid", wid));
        return (Authority)criteria.uniqueResult();
    }

    public void merge(Authority authority) {
        getSession().merge(authority);
    }

    public Authority findUserAuthority(String uid,String wid){
        DetachedCriteria dc = DetachedCriteria.forClass(Authority.class,"auth");
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.add(Restrictions.eq("wid",wid));
        criteria.add(Restrictions.eq("auth.user.id", uid));
        return (Authority) criteria.uniqueResult();
    }


    public List<Authority> findWidByUid(String uid){
        DetachedCriteria dc = DetachedCriteria.forClass(Authority.class,"auth");
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.add(Restrictions.eq("auth.user.id", uid));
        criteria.add(Restrictions.eq("operateable",1));
        return criteria.list();
    }



}
