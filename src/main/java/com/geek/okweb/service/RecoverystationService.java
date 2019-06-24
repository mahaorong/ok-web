package com.geek.okweb.service;

import com.geek.okweb.dao.*;
import com.geek.okweb.domain.*;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RecoverystationService {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private FormDao formDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private WorktableDao worktableDao;

    @Autowired
    private WorktableService worktableService;

    public MyPage<Blog> findAllByArticle(Integer status, Integer page, Integer pageSize) {
        return blogDao.findAllByStatus(status, page, pageSize);
    }

    public MyPage<Blog> findByCategoryArticle(Set<String> categorys, Integer page, Integer pageSize) {
        List<Blog> blogList = blogDao.findByRecoveryTags(categorys, page, pageSize, "total");
        log.info("集合数量={}", blogList.size());
        MyPage<Blog> myPage = new MyPage(blogList.size(), pageSize, page);
        List<Blog> limitResult = blogDao.findByRecoveryTags(categorys, page, pageSize, null);
        myPage.setItems(limitResult);
        return myPage;
    }

    public MyPage<Image> findAllByImage(Integer status, Integer page, Integer pageSize) {
        return imageDao.findAllByStatus(status, page, pageSize);
    }

    public MyPage<Image> findByCategoryImage(Set<String> categorys, Integer page, Integer pageSize) {
        List<Image> blogList = imageDao.findImageByRecoveryCategroys(categorys, page, pageSize, "total");
        log.info("集合数量={}", blogList.size());
        MyPage<Image> myPage = new MyPage(blogList.size(), pageSize, page);
        List<Image> limitResult = imageDao.findImageByRecoveryCategroys(categorys, page, pageSize, null);
        myPage.setItems(limitResult);
        return myPage;
    }

    public MyPage<Form> findAllByForm(Integer status, Integer page, Integer pageSize) {
        return formDao.findAllByStatus(status, page, pageSize);
    }

    public MyPage<FileUpload> findAllByFile(Integer status, Integer page, Integer pageSize) {
        return fileDao.findAllByStatus(status, page, pageSize);
    }

    public MyPage<FileUpload> findByCategoryFile(Set<String> categorys, Integer page, Integer pageSize) {
        List<FileUpload> blogList = fileDao.findByTags(categorys, page, pageSize, "total", "recovery");
        log.info("集合数量={}", blogList.size());
        MyPage<FileUpload> myPage = new MyPage(blogList.size(), pageSize, page);
        List<FileUpload> limitResult = fileDao.findByTags(categorys, page, pageSize, null, "recovery");
        myPage.setItems(limitResult);
        return myPage;
    }

    public MyPage<Worktable> findAllByWork(Integer status, Integer page, Integer pageSize) {
        return worktableDao.findAllByWork(status, page, pageSize);
    }

    public MyPage<Worktable> findByCategoryWork(Set<String> categorys, Integer page, Integer pageSize) {
        List<Worktable> blogList = worktableDao.findByRecycleTags(categorys, page, pageSize, "total");
        log.info("集合数量={}", blogList.size());
        MyPage<Worktable> myPage = new MyPage(blogList.size(), pageSize, page);
        List<Worktable> limitResult = worktableDao.findByRecycleTags(categorys, page, pageSize, null);
        myPage.setItems(limitResult);
        return myPage;
    }

    public void delete(List<String> id, String type) {
        if (id != null && id.size() != 0) {
            if ("recycle_article".equals(type)) {
                id.stream().forEach(blogDao::delete);
            } else if ("recycle_image".equals(type)) {
                id.stream().forEach(imageDao::delete);
            } else if ("recycle_form".equals(type)) {
                id.stream().forEach(formDao::deleteForm);
            } else if ("recycle_file".equals(type)) {
                id.stream().forEach(fileDao::delete);
            } else if ("recycle_product".equals(type)) {
                id.stream().forEach(worktableService::deleteWorktable);
            }
        }
    }

    public void restore(List<String> id, String type) {
        if (id != null && id.size() != 0) {
            if ("recycle_article".equals(type))
                id.stream().forEach(blogDao::restore);
            else if ("recycle_image".equals(type))
                id.stream().forEach(imageDao::restore);
            else if ("recycle_form".equals(type)) {
                id.stream().forEach(formDao::restore);
            } else if ("recycle_file".equals(type)) {
                id.stream().forEach(fileDao::restore);
            } else if ("recycle_product".equals(type)) {
                id.stream().forEach(worktableDao::restore);
            }
        }
    }

}
