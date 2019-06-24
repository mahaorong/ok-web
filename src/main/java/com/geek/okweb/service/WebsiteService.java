package com.geek.okweb.service;

import com.geek.okweb.dao.WebsiteDao;
import com.geek.okweb.domain.Website;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class WebsiteService {

    @Autowired
    private WebsiteDao websiteDao;

    public void save(Website website){
        websiteDao.save(website);
    }
    public void delete(String id){
        websiteDao.delete(id);
    }
    public void update(Website website){
        websiteDao.update(website);
    }
    public Website findById(String id){
        return websiteDao.findById(id);
    }
    public List<Website> findAll(){
        return websiteDao.findAll();
    }

    public Website findByUrl(String requestURI) {
        return websiteDao.findByUrl(requestURI);
    }

    public MyPage<Website> findByWebPage(Integer pageSize, Integer page) {
        return websiteDao.findByWebPage(pageSize, page);
    }

}
