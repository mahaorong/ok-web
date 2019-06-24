package com.geek.okweb.dao;

import com.geek.okweb.domain.Form;
import com.geek.okweb.domain.FormItem;
import com.geek.okweb.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Transactional
public class FormItemDao extends BaseDao<FormItem> {

    //保存表单项
    public void save(FormItem fromItem){
        getSession().save(fromItem);
    }

    //查询全部表单项
    public List<FormItem> findAll(){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FormItem.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        List<FormItem> formItemList = criteria.list();
        return formItemList;
    }

    //删除
    public void delete(FormItem formItem){
        getSession().delete(formItem);
    }

    //查询操作
    public List<FormItem> findByForm(String type,String formId){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FormItem.class,"f");
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.add(Restrictions.eq("f.form.id",formId));
        if (StringUtils.equals(type,"All")){
            log.info("【查询全部】");
        }else if (StringUtils.equals(type,"text")){
            log.info("【查询关于文本类的表单项】");
            criteria.add(Restrictions.or(Restrictions.eq("type","text"),
                    Restrictions.eq("type","textarea"),
                    Restrictions.eq("type","select")));
        }else {
            log.info("【根据传来的表单项类型查询全部】");
            criteria.add(Restrictions.eq("type",type));
        }
        criteria.addOrder(Order.asc("sort"));
        List<FormItem> formItems = criteria.list();
        return formItems;
    }

    //查询当个表单项
    public FormItem findByFormItem(String id){
        FormItem formItem = getSession().find(FormItem.class, id);
        return formItem;
    }

    //更新
    public void update(FormItem formItem){
        getSession().update(formItem);
    }
    public void merge(FormItem formItem){
        getSession().merge(formItem);
    }

    //封装FormItem数据
    public void packageFormItemData(List list, Form form , FormItem formItem, String userName){
        FormItem formItem1 = new FormItem();
        formItem1.setId(KeyUtil.UUID());
        formItem1.setName(formItem.getName());
        formItem1.setOption(formItem.getOption());
        formItem1.setSort(formItem.getSort());
        formItem1.setType(formItem.getType());
        formItem1.setResult(list);
        formItem1.setUserId(userName);
        formItem1.setForm(form);
        formItem1.setFormItem(formItem);
        save(formItem1);
    }

    //查询该表单下的全部表单项类型
    public List<String> findByType(String formId){
        List<String> allType = new ArrayList<>();
        DetachedCriteria detachedCriteria= DetachedCriteria.forClass(FormItem.class,"f");
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.add(Restrictions.eq("f.form.id",formId));
        List<FormItem> formItemList = criteria.list();
        for (FormItem formItem : formItemList){
            allType.add(formItem.getType());
        }
        return allType;
    }
}
