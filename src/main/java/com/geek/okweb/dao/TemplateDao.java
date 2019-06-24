package com.geek.okweb.dao;

import com.geek.okweb.domain.Template;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@Transactional
public class TemplateDao extends BaseDao<Template> {

    public void save(Template template){
        getSession().saveOrUpdate(template);
        log.info("保存或更新成功");
    }

    public void delete(Template template){
        getSession().delete(template);
    }

    public Template findById(String id){
        return getSession().find(Template.class,id);
    }

    @Transactional(readOnly = true)
    public Template findByUrl(String url,String language){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Template.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.add(Restrictions.eq("url",url));
        criteria.add(Restrictions.eq("language",language));
        Template template = (Template)criteria.uniqueResult();
        return template;
    }

    public MyPage<Template> findAll(Integer page, Integer pageSize, String language){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Template.class);
        detachedCriteria.add(Restrictions.eq("language", language));
        return findPageByCriteria(detachedCriteria,pageSize,page);
    }

    public List<Template> findAll(){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Template.class);
        return findAllByCriteria(detachedCriteria);
    }

    public void update(Template template){
        getSession().update(template);
    }

    public void merge(Template template){
        getSession().merge(template);
    }

}
