package com.geek.okweb.service;

import com.geek.okweb.dao.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class  SearchService {

    @Autowired
    private FormDao formDao;

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private WorktableDao workDao;

    @Autowired
    private ProductDao productDao;

    public Object search(Integer page, Integer pageSize,String type,String keyword,String id){
        if ("manageBlog".equals(type)){
            return blogDao.searchBlog(page,pageSize,"title",keyword,null);
        }else if ("recycle_article".equals(type)){
            return blogDao.searchBlog(page,pageSize,"title",keyword,"recovery");
        } else if ("addProductBlog".equals(type)){
            return blogDao.searchBlog(page,pageSize,1,"title",keyword,null);
        } else if ("article_review".equals(type)) {
            return blogDao.searchBlog(page, pageSize, 0, "title", keyword, null);
        } else if ("article_refust".equals(type)) {
            return blogDao.searchBlog(page, pageSize, 2, "title", keyword, null);
        } else if ("article_all".equals(type)) {
            return blogDao.searchBlog(page, pageSize, "title", keyword);
        } else if ("article_adopt".equals(type)) {
            return blogDao.searchBlog(page, pageSize, 1, "title", keyword, null);
        }else if ("form_admin".equals(type)){
            return formDao.searchForm(page,pageSize,3,"name",keyword,null);
        }else if ("form_all".equals(type)){
            return formDao.searchForm(page,pageSize,4,"name",keyword,null);
        }else if ("form_untreated".equals(type)){
            return formDao.searchForm(page,pageSize,0,"name", keyword,null);
        }else if ("form_processed".equals(type)){
            return formDao.searchForm(page,pageSize,1,"name", keyword,null);
        }else if ("form_abnormal".equals(type)){
            return formDao.searchForm(page,pageSize,2,"name", keyword,null);
        }else if ("recycle_form".equals(type)){
            return formDao.searchForm(page,pageSize,0,"name", keyword,"recovery");
        }else if ("image_admin".equals(type)){
            return imageDao.searchImage(page,pageSize,"imageName", keyword,null);
        }else if ("recycle_image".equals(type)){
            return imageDao.searchImage(page,pageSize,"imageName", keyword,"recovery");
        }else if ("product_form".equals(type)){
            return workDao.searchProduct(page,pageSize,"fileName", keyword,null);
        }else if ("recycle_product".equals(type)){
            return workDao.searchProduct(page,pageSize,"fileName", keyword,"recovery");
        }else if ("file_management".equals(type)){
            return fileDao.searchFile(page,pageSize,"name", keyword,null);
        }else if ("recycle_file".equals(type)){
            return fileDao.searchFile(page,pageSize,"name", keyword,"recovery");
        }else if ("productFileData".equals(type)){
            return fileDao.searchFileList("name", keyword , id);
        }else if ("addProductFile".equals(type)){
            return fileDao.searchFile(page,pageSize,"name", keyword,null);
        }else if ("productDetail".equals(type)){
            return productDao.searchProduct(page,pageSize,"name", keyword,null);
        } else if ("model_dataType_article".equals(type)) {
            return blogDao.searchBlog(page,pageSize,"title",keyword,null);
        }
        return "error";
    }
}
