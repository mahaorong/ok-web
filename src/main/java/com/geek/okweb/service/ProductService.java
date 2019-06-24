package com.geek.okweb.service;

import com.geek.okweb.dao.ProductDao;
import com.geek.okweb.domain.*;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Create by Gai on 2018/12/11 09:21
 */
@Slf4j
@Service
@Transactional
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    public Session getSession(){
        return entityManager.unwrap(Session.class);
    }

    @Autowired
    private ProductDao productDao;

    public void update(Product product){
        productDao.update(product);
    }

    public void save(Product product){
        productDao.save(product);
    }
    //保存产品名字
    public void saveName(String id,String name){
        Product product = productDao.findById(id);
        product.setName(name);
        productDao.update(product);
    }

    public List<Product> findByWidProduct(String wid){
        List<Product> products = productDao.findByWideProduct(wid);
        return products;
    }

    public MyPage<Product> findPageByWidProduct(String wid,Integer page,Integer pageSize){
        MyPage<Product> productPage = productDao.findByWideProduct(wid,page,pageSize);
        return productPage;
    }

    public void merge(Product product){
        productDao.merge(product);
    }

    public Product findById(String id){
        Product product = productDao.findById(id);
        return product;
    }



   public List<String> findProductName(MyPage<Product> proPage){
       Product product = proPage.getItems().get(0);
       List<String> productName = new ArrayList<>();
       log.info("productName=={}",product.getName());
       List<ProductData> productData = product.getJson();
       if (productData == null && productData.size() ==0){
       }else {
           for (ProductData productData1 : productData){
               productName.add(productData1.getProauctName());
           }
       }
       return productName;
   }

    //保存一条数据
    public void save(){
        Product product = new Product();
        productDao.save(product);
    }

    public void delete(String id){
        productDao.delete(id);
    }

    /**
     * 根据id查询文件
     * @param ids
     * @return
     */
    public List<Product> findByIds(Set<String> ids){
        return productDao.findByIds(ids);
    }

    public MyPage<Product> findWidByPage(Integer status, Integer page ,Integer pageSize,String wid){
        return productDao.findWidByPage(status,page,pageSize,wid);
    }

    /**
     * 根据分类查产品
     * @param categorys
     * @return
     */
    public MyPage<Product> findByCategorys(Set<String> categorys,Integer page,Integer pageSize){
        List<Product> productList = productDao.findByTags(categorys, page, pageSize, "total");
        log.info("集合数量={}",productList.size());
        MyPage<Product> myPage = new MyPage(productList.size(),pageSize,page);
        List<Product> limitResult = productDao.findByTags(categorys,page,pageSize,null);
        myPage.setItems(limitResult);
        return myPage;
    }
    public MyPage<Product> findAllPage(Integer page,Integer pageSize){
        return productDao.findAllPage(page,pageSize);
    }


    public MyPage<Product> findByName(Integer page, Integer pageSize, String searchName, String keyword){
        return productDao.searchProduct(page, pageSize, searchName, keyword, null);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

}
