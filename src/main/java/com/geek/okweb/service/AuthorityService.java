package com.geek.okweb.service;

import com.geek.okweb.dao.AuthorityDao;
import com.geek.okweb.domain.Authority;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class AuthorityService {

    @Autowired
    private AuthorityDao authorityDao;

    public void save(Authority authority){
        authorityDao.save(authority);
    }
    public void delete(String id){
        authorityDao.delete(id);
    }
    public void update(Authority authority){
        authorityDao.update(authority);
    }
    public Authority findById(String id){
        return authorityDao.findById(id);
    }
    public List<Authority> findAll(){
        return authorityDao.findAll();
    }

    public void merge(Authority authority) {
        authorityDao.merge(authority);
    }

    public Authority findByWid(String wid) {
        return authorityDao.findByWid(wid);
    }


    public Authority findUserAuthority(String uid, String wid){
        return authorityDao.findUserAuthority(uid, wid);
    }

    public List<Authority> findWidByUid(String uid){
        return authorityDao.findWidByUid(uid);
    }

}
