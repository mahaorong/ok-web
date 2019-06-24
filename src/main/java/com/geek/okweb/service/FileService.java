package com.geek.okweb.service;


import com.geek.okweb.dao.FileDao;
import com.geek.okweb.domain.FileUpload;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FileService {


    @Autowired
    private FileDao fileDao;

   public List<FileUpload> findAllFiles(){
       List<FileUpload> files = fileDao.findAllFile();
       return files;
   }

    public MyPage<FileUpload> findByFilePage(Integer status, Integer page, Integer pageSize){
        return fileDao.findAllByFile(status,page, pageSize);
    }
    /**
     * 根据id查询文件
     * @param ids
     * @return
     */
    public MyPage<FileUpload> findByIds(Set<String> ids,Integer pageSize,Integer page){
        List<FileUpload> fileList = fileDao.findByIds(ids,page,pageSize,"total");
        log.info("【分类文章】集合数量={}",fileList.size());
        MyPage<FileUpload> myPage = new MyPage(fileList.size(),pageSize,page);
        List<FileUpload> limitResult = fileDao.findByIds(ids,page,pageSize,null);
        myPage.setItems(limitResult);
        return myPage;
    }

    public void saveFile(FileUpload fileUpload) {
        fileDao.saveFile(fileUpload);
    }

    public void delete(String id){
        fileDao.deleteFile(id);
    }

    public void deleteFile(String id) {
        fileDao.delete(id);
    }

    public void update(FileUpload fileUpload){
        fileDao.update(fileUpload);
    }

    public FileUpload findById(String id){
        return fileDao.findById(id);
    }

    public MyPage<FileUpload> findAllByStatus(Integer status, Integer page, Integer pageSize){
        return fileDao.findAllByStatus(status,page,pageSize);
    }

    /**
     * 根据分类查询文件
     * @param categorys
     * @return
     */
    public MyPage<FileUpload> findByCategorys(Set<String> categorys,Integer page,Integer pageSize,String action){
        List<FileUpload> fileList = fileDao.findByTags(categorys, page, pageSize, "total", action);
        log.info("集合数量={}",fileList.size());
        MyPage<FileUpload> myPage = new MyPage(fileList.size(),pageSize,page);
        List<FileUpload> limitResult = fileDao.findByTags(categorys, page, pageSize, null, action);
        myPage.setItems(limitResult);
        return myPage;
    }

    public MyPage<FileUpload> searchFile(Integer page,Integer pageSize,String keyword){
        return fileDao.searchFile(page, pageSize, keyword);
    }


}
