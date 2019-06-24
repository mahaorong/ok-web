package com.geek.okweb.dao;



import com.geek.okweb.domain.FileUpload;
import com.geek.okweb.domain.Product;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Create by Gai on 2018/11/7 19:19
 */
@Component
@Transactional
@Slf4j
public class FileDao extends BaseDao<FileUpload>  {

    @Autowired
    private ProductDao productDao;

    public List<FileUpload> findAllFile(){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FileUpload.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.addOrder(Order.desc("createTime"));
        List<FileUpload> fileUploadList = criteria.list();
        return fileUploadList;

    }

    public void saveFile(FileUpload fileUpload) {
        getSession().save(fileUpload);
    }


    public FileUpload findById(String id){
        return getSession().find(FileUpload.class,id);
    }
    //将文件从回收站移除
    public void restore(String formId){
        FileUpload fileUpload  = findById(formId);
        fileUpload.setStatus(0);
        getSession().update(fileUpload);
    }

    public void deleteFile(String id){
        FileUpload fileUpload = findById(id);
        fileUpload.setStatus(1);
        update(fileUpload);
    }
    /**
     * 根据标签查询文件
     * @return
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<FileUpload> findByTags(Set<String> categorys,Integer page,Integer pageSize,String flag,String action){
        log.info("【FileDao标签】={}",categorys);
        Integer startIndex = (page-1)*pageSize;
        List<FileUpload> fileList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM file_upload WHERE (";
            for (int i=0; i<categorys.size(); i++) {
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<categorys.size()-1)
                    sql += "OR ";
            }
            if ("recovery".equals(action)){
                sql +=") AND status=1 ";
            }else {
                sql +=") AND status=0 ";
            }

            if ("total".equals(flag)){
                sql +=" ORDER BY create_time DESC ";
            }else {
                sql +=" ORDER BY create_time DESC limit "+startIndex+","+pageSize+"";
            }

            log.info(sql);
            org.hibernate.query.Query query = getSession().createNativeQuery(sql, FileUpload.class);
            int i=0;
            for (String id:categorys) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            fileList = query.getResultList();
        } catch (RuntimeException re) {
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return fileList;
    }

    public void delete(String id) {
        FileUpload fileUpload = findById(id);
        getSession().delete(fileUpload);
    }

    public MyPage<FileUpload> findAllByStatus(Integer status, Integer page, Integer pageSize) {
        return findAllByStatus(DetachedCriteria.forClass(FileUpload.class),status,page,pageSize);
    }

    public List<FileUpload> findByIds(Set<String> ids,Integer page,Integer pageSize,String flag) {
        List<FileUpload> fileUploadList = new ArrayList<>();
        try {
            Integer startIndex = (page - 1) * pageSize;
            String sql = "SELECT * FROM file_upload WHERE ";
            for (int i=0; i<ids.size(); i++) {
                sql += "id=:id"+i+"";
                if (i<ids.size()-1)
                sql += " OR ";
            }

            if (ids.size() <= 0){
                sql += "1=1";
            }


            if (StringUtils.equals("total",flag)){
                sql +=" ORDER BY create_time DESC ";
            }else {
                sql +=" ORDER BY create_time DESC limit "+startIndex+","+pageSize+"";
            }
            Query query = getSession().createNativeQuery(sql, FileUpload.class);
            int i=0;
            for (String id:ids) {
                log.info("id={}", id);
                query.setParameter("id"+i, ""+id+"");
                i++;
            }
            fileUploadList = query.getResultList();
        } catch (RuntimeException re) {
            throw re;
        }
        return  fileUploadList;
    }

    public void update(FileUpload fileUpload) {
        getSession().update(fileUpload);
    }

    public MyPage<FileUpload> findAllByFile(Integer status, Integer page, Integer pageSize) {
        return findAllByStatus(DetachedCriteria.forClass(FileUpload.class),status,page,pageSize);
    }


    //查询审核通过以及未在回收站的文件的个数
    public int getSearchCount(String searchName,String keyword,String action){
        String sql = null;
        if (StringUtils.isNotBlank(action)&&StringUtils.equals("recovery",action)){
            sql = "where status = 1 and "+searchName+" like '%"+keyword+"%'";
        }else {
            sql = "where status = 0 and "+searchName+" like '%"+keyword+"%'";
        }
        return getCustomizeCount("file_upload",sql);
    }

    public List<FileUpload> searchFileList(String searchName,String keyword,String id){
        log.info("【进入产品搜索】");
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FileUpload.class);
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.add(Restrictions.like(searchName,"%"+keyword+"%"));
        Product product = productDao.findById(id);
        List<String> files = product.getFiles();
        List<FileUpload> fileUploadList = criteria.list();
        List<FileUpload> fileUploads = new ArrayList<>();
        fileUploadList.stream().forEach(x -> {
            files.stream().forEach(y -> {
                if (StringUtils.equals(x.getId(),y)){
                    fileUploads.add(x);
                }
            });
        });
        return fileUploads;
    }


    public MyPage<FileUpload> searchFile(Integer page, Integer pageSize, String searchName, String keyword, String action) {
        DetachedCriteria dc = DetachedCriteria.forClass(FileUpload.class);
        if (StringUtils.isNotBlank(action)&& StringUtils.equals("recovery",action)){
            Integer totalCount = getSearchCount("name",keyword,"recovery");
            return findPageBySearchCriteria(dc, totalCount, "recovery" , searchName, keyword, pageSize, page);
        }
        //dc.add(Restrictions.eq("status",0));
        dc.addOrder(Order.desc("createTime"));
        Integer totalCount = getSearchCount("name",keyword,null);
        return findPageBySearchCriteria(dc, totalCount, null , searchName, keyword, pageSize, page);
    }

    public MyPage<FileUpload> searchFile(Integer page, Integer pageSize, String keyword) {
        DetachedCriteria dc = DetachedCriteria.forClass(FileUpload.class);
        int totalCount = getSearchCount("name", keyword, null);
        return findPageBySearchCriteria(dc, totalCount, null, "name", keyword, pageSize, page);
    }
}
