package com.geek.okweb.service;

import com.geek.okweb.dao.ImageDao;
import com.geek.okweb.domain.Image;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class ImageService {

    @Autowired
    private ImageDao imageDao;

    public void save(Image image){
        imageDao.save(image);
    }

    public List<Image> findAll(){
        return imageDao.findAll();
    }

    public void delete(String id) {
        imageDao.delete(id);
    }

    public Image findById(String id){
        return imageDao.findById(id);
    }

    public MyPage<Image> findAllByStatus(Integer status, Integer page, Integer pageSize){
        return imageDao.findAllByStatus(status,page,pageSize);
    }

    public void update(Image image) {
        imageDao.update(image);
    }


    public MyPage<Image> findImageByCategroys(Set<String> cids,Integer page,Integer pageSize) {
        List<Image> imageListTotal = imageDao.findImageByCategroys(cids, page, pageSize, "total");
        log.info("【imageService】集合数量={}",imageListTotal.size());
        List<Image> imageList = imageDao.findImageByCategroys(cids, page, pageSize, null);
        MyPage<Image> imagePage = new MyPage(imageListTotal.size(),pageSize,page);
        imagePage.setItems(imageList);
        return imagePage;
    }

    public MyPage<Image> search(int page, int pageSize, String keyword) {
        return imageDao.searchImage(page, pageSize, "imageName", keyword, null);
    }
}
