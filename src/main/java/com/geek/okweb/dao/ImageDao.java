package com.geek.okweb.dao;

import com.geek.okweb.domain.Image;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
@Transactional
public class ImageDao extends BaseDao<Image> {

    public void save(Image image){
        getSession().save(image);
    }

    public List<Image> findAll(){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Image.class);
        detachedCriteria.addOrder(Order.desc("createTime"));
        return findAllByCriteria(detachedCriteria);
    }

    public void delete(String id) {
        Image image = getSession().find(Image.class, id);
        getSession().delete(image);
    }

    public Image findById(String id) {
        return getSession().find(Image.class,id);
    }

    public MyPage<Image> findAllByStatus(Integer status, Integer page, Integer pageSize) {
        return findAllByStatus(DetachedCriteria.forClass(Image.class),status,page,pageSize);
    }

    public void update(Image image) {
        getSession().update(image);
    }

    public void restore(String id) {
        Image image = findById(id);
        image.setStatus(0);
        update(image);
    }

    public List<Image> findImageByCategroys(Set<String> cids,Integer page,Integer pageSize,String flag) {
        log.info("【Imagedao标签】={}",cids);
        Integer startIndex = (page-1)*pageSize;
        List<Image> imageList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM image WHERE (";
            for (int i=0;i<cids.size();i++){
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<cids.size()-1)
                    sql += "OR ";
            }
            if (StringUtils.equals("total",flag)){
                sql+=") AND status=0 ORDER BY create_time DESC ";
            }else {
                sql+=") AND status=0 ORDER BY create_time DESC limit "+startIndex+","+pageSize+"";
            }
            log.info("【sql语句】={}",sql);
            NativeQuery<Image> query = getSession().createNativeQuery(sql, Image.class);
            int i=0;
            for (String id:cids) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            imageList = query.getResultList();
        }catch(RuntimeException re){
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return imageList;
    }

    public List<Image> findImageByRecoveryCategroys(Set<String> cids,Integer page,Integer pageSize,String flag) {
        log.info("【Imagedao标签】={}",cids);
        Integer startIndex = (page-1)*pageSize;
        List<Image> imageList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM image WHERE (";
            for (int i=0;i<cids.size();i++){
                sql += "JSON_contains(cateids,:id"+i+") ";
                if (i<cids.size()-1)
                    sql += "OR ";
            }
            if (StringUtils.equals("total",flag)){
                sql+=") AND status=1 ORDER BY create_time DESC ";
            }else {
                sql+=") AND status=1 ORDER BY create_time DESC limit "+startIndex+","+pageSize+"";
            }
            log.info("【sql语句】={}",sql);
            NativeQuery<Image> query = getSession().createNativeQuery(sql, Image.class);
            int i=0;
            for (String id:cids) {
                query.setParameter("id"+i, "\""+id+"\"");
                i++;
            }
            imageList = query.getResultList();
        }catch(RuntimeException re){
            log.error("refreshUserCount_failed", re);
            throw re;
        }
        return imageList;
    }

    /**
     * 模糊查询回收站的图片的个数
     * @param searchName 模糊查询那个字段
     * @param keyword 关键字
     * @return
     */
    public int getSearchCount(String searchName,String keyword,String action){
        String sql;
        if(StringUtils.equals("recovery",action)){
            sql  = "where status = 1 and "+searchName+" like '%"+keyword+"%'";
        }else {
            sql  = "where status = 0 and "+searchName+" like '%"+keyword+"%'";
        }
        return getCustomizeCount("image",sql);
    }

    /**
     * 搜索回收站的图片分页
     * @param page 当前页
     * @param pageSize 每页显示数量
     * @param searchName 根据那个字段进行查询
     * @param keyword 关键字
     * @return
     */
    public MyPage<Image> searchImage(Integer page, Integer pageSize, String searchName, String keyword,String action) {
        DetachedCriteria dc = DetachedCriteria.forClass(Image.class);
        Integer totalCount = getSearchCount("image_name",keyword,action);

        return findPageBySearchCriteria(dc,totalCount,action,searchName,keyword,pageSize,page);
    }
}
