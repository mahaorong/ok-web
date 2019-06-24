package com.geek.okweb.dao;

import com.geek.okweb.domain.Form;
import com.geek.okweb.utils.KeyUtil;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Transactional
public class FormDao extends BaseDao<Form> {

    //保存表单
    public void save(Form form){
        getSession().save(form);
    }

    //根据状态查询在回收站中表单
    public MyPage<Form> findAllByStatus(Integer status,Integer page,Integer pageSize){
        DetachedCriteria dc = DetachedCriteria.forClass(Form.class);
        return findAllByStatus(dc,status,page,pageSize);
    }

    //根据状态查询在回收站中表单
    public List<Form> findAllByStatus(){
        DetachedCriteria dc = DetachedCriteria.forClass(Form.class);
        dc.add(Restrictions.ne("shape", 3));
        List<Form> formList = findAllByCriteria(dc);
        return formList;
    }

    //根据状态查询未在回收站的表单
    public List<Form> findAll(){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Form.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.desc("createTime"));
        criteria.add(Restrictions.eq("status",0));
        criteria.add(Restrictions.eq("shape",3));
        List<Form> formList = criteria.list();
        return formList;
    }

    //删除表单
    public void deleteForm(String formId){
        getSession().delete(findById(formId));
    }

    //将表单放入回收站
    public void delete(String formId){
        Form form = findById(formId);
        form.setStatus(1);
        getSession().update(form);
    }

    //将表单从回收站移除
    public void restore(String formId){
        Form form = findById(formId);
        form.setStatus(0);
        getSession().update(form);
    }


    //查询单个表单
    public Form findById(String formId){
        Form form = getSession().find(Form.class, formId);
        return form;
    }

    //封装Form数据
    public Form packageFormData(String formId, String userName){
        Form form = findById(formId);
        Form form1 = new Form();
        form1.setId(KeyUtil.UUID());
        form1.setName(form.getName());
        form1.setUserId(userName);
        form1.setForm(form);
        save(form1);
        return form1;
    }

    //更新
    public Form update(Form form){
        getSession().update(form);
        return form;
    }

    /**
     * 查询用户提交的表单以及未在回收站的表单的个数
     * @return
     */
    public int getCount(Integer status,Integer shape){
        String role = "where status = "+status+" and shape = "+shape;
        log.info("rol=={}",role);
        return getCustomizeCount("form",role);
    }

    /**
     * 模糊查询用户提交的表单以及未在回收站的表单的个数
     * @param searchName 模糊查询那个字段
     * @param keyword 关键字
     * @return
     */
    public int getSearchCount(Integer shape,String searchName,String keyword){
        String sql = null;
        if (shape == 10){
            sql  = "where status = 1 and "+searchName+" like '%"+keyword+"%'";
        }else {
           sql  = "where status = 0 and shape = "+shape+" and "+searchName+" like  '%"+keyword+"%'";
        }
        return getCustomizeCount("form",sql);
    }

    //表单结果分页查询
    public MyPage<Form> findByFormPage(Integer status,Integer shape,int page, int pageSize){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Form.class);
        int totalCount = getCount(status,shape);
        detachedCriteria.addOrder(Order.desc("createTime"));
        detachedCriteria.add(Restrictions.eq("shape",shape));
        detachedCriteria.add(Restrictions.eq("status",status));
        return findPageByCriteria(totalCount,detachedCriteria,pageSize,page);
    }

    /**
     * 搜索表单分页
     * @param page 当前页
     * @param pageSize 每页显示数量
     * @param shape 状态   0---未通过表单状态 1----已通过表单状态 2----异常表单状态 3-----页面表单
     * @param searchName 根据那个字段进行查询
     * @param keyword 关键字
     * @param action 操作   recovery为回收站的表单数据进行搜索分页
     * @return
     */
    public MyPage<Form> searchForm(int page, int pageSize,int shape,String searchName,String keyword,String action){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Form.class);
        detachedCriteria.addOrder(Order.desc("createTime"));
        if (StringUtils.isNotBlank(action)&& StringUtils.equals("recovery",action)){
            int totalCount =getSearchCount(10,searchName,keyword);
            return findPageBySearchCriteria(detachedCriteria,totalCount,"recovery",searchName,keyword,pageSize,page);
        }
        int totalCount = getSearchCount(shape,searchName,keyword);
        if (shape != 4){
            detachedCriteria.add(Restrictions.eq("shape",shape));
        }else {
            detachedCriteria.add(Restrictions.ne("shape", 3));
        }
        return findPageBySearchCriteria(detachedCriteria,totalCount,null,searchName,keyword,pageSize,page);
    }

    public MyPage<Form> findByUserForm(Integer pageSize ,Integer page) {
        DetachedCriteria dc = DetachedCriteria.forClass(Form.class);
        dc.addOrder(Order.desc("createTime"));
        dc.add(Restrictions.ne("shape", 3));
        dc.add(Restrictions.eq("status", 0));
        return findPageByCriteria(dc, pageSize, page);
    }

    /**
     * 根据标签查询文章
     * @return
     */
    public List<Form> findByTags(Set<String> categorys){
        log.info("【Formdao标签】={}",categorys);
        List<Form> formList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM form WHERE ";
            for (int i=0; i<categorys.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<categorys.size()-1)
                    sql += "OR ";
            }

            log.info(sql);
            Query query = getSession().createNativeQuery(sql, Form.class);
            int i=0;
            for (String id:categorys) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            formList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return formList;
    }

}
