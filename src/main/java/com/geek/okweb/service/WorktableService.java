package com.geek.okweb.service;

import com.geek.okweb.dao.ProductDao;
import com.geek.okweb.dao.WorktableDao;
import com.geek.okweb.domain.Product;
import com.geek.okweb.domain.ProductData;
import com.geek.okweb.domain.Worktable;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Create by Gai on 2018/12/13 21:19
 */
@Slf4j
@Service
public class WorktableService {

    @Autowired
    private WorktableDao worktableDao;

    @Autowired
    private ProductDao productDao;

    public void save(Worktable worktable){
        worktableDao.save(worktable);
    }

    public List<Worktable> findAll(){
        return worktableDao.findAll();
    }

    public MyPage<Worktable> findByWorkTablePage(Integer status,Integer page,Integer pageSize){
        return worktableDao.findAllByWork(status,page, pageSize);
    }

    public Worktable findById(String id){
        return worktableDao.findById(id);
    }

    public void update(Worktable worktable){
        worktableDao.update(worktable);
    }

    public void delete(String id){
        worktableDao.delete(id);
    }

    public Worktable findByFileName(String fileName){
        return worktableDao.findByFileName(fileName);
    }

    public void merge(Worktable worktable) {
        worktableDao.merge(worktable);
    }

    public void deleteWorktable(String wid) {
        delete(wid);
        List<Product> productList = productDao.findByWid(wid);
        productList.stream().map(x -> x.getId()).forEach(x -> productDao.delete(x));
    }

    public MyPage<Worktable> findWorkByCategroys(Set<String> cids, Integer page, Integer pageSize) {
        List<Worktable> imageListTotal = worktableDao.findByTags(cids, page, pageSize, "total");
        log.info("【imageService】集合数量={}",imageListTotal.size());
        List<Worktable> proPage = worktableDao.findByTags(cids, page, pageSize, null);
        MyPage<Worktable> workPage = new MyPage(imageListTotal.size(),pageSize,page);
        workPage.setItems(proPage);
        return workPage;
    }
    public Worktable findWorkTable(Set<String> cids) {
        List<Worktable> worktableList = worktableDao.findWork(cids);
        if (worktableList != null && worktableList.size() > 0){
            return worktableList.get(0);
        }
        return null;
    }
}
