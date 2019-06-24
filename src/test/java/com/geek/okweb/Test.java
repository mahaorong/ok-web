/*
package com.geek.okweb;


import com.geek.okweb.dao.WebsiteDao;
import com.geek.okweb.domain.User;
import com.geek.okweb.domain.Website;
import com.geek.okweb.service.UserService;
import com.geek.okweb.service.WebsiteService;
import com.geek.okweb.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList; qw
import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {
    @Autowired
    private UserService userService;

    @Autowired
    private WebsiteDao websiteDao;


    @Autowired
    private WebsiteService websiteService;

    @org.junit.Test
    public void save(){
//        Website website1 = new Website(KeyUtil.UUID(),"/admin/index","首页","query",1);
//        Website website3 = new Website(KeyUtil.UUID(),"/admin/addBlog","添加文章","save",1);
//        Website website4 = new Website(KeyUtil.UUID(),"/admin/findFile","文件管理","query",1);
//        Website website5 = new Website(KeyUtil.UUID(),"/admin/findBlog","文章审核和管理","query",1);
//        Website website6 = new Website(KeyUtil.UUID(),"/admin/delete/blog","删除文章","delete",1);
//        Website website7 = new Website(KeyUtil.UUID(),"/admin/blog/detail","文章详情","query",1);
//        Website website8 = new Website(KeyUtil.UUID(),"/admin/modify","修改文章","update",1);
//        Website website9 = new Website(KeyUtil.UUID(),"/admin/reviewArticle","审核文章","update",1);
//        Website website10 = new Website(KeyUtil.UUID(),"/admin/deleteFile","删除文件","delete",1);
//        Website website11 = new Website(KeyUtil.UUID(),"/admin/findProductDetail","产品详情","query",1);
//        Website website12 = new Website(KeyUtil.UUID(),"/admin/isTop","文章置顶","update",1);
//        Website website13 = new Website(KeyUtil.UUID(),"/admin/isNotTop","文章取消置顶","delete",1);
//        Website website14 = new Website(KeyUtil.UUID(),"/admin/create_category","创建分类","save",1);
//        Website website15 = new Website(KeyUtil.UUID(),"/admin/categorys","查询所有分类","query",1);
//        Website website16 = new Website(KeyUtil.UUID(),"/admin/category/change","修改分类","update",1);
//        Website website17 = new Website(KeyUtil.UUID(),"/admin/deletecategory","删除分类","delete",1);
//
//
//        Website website18 = new Website(KeyUtil.UUID(),"/recovery/findAll","回收站查询","query",1);
//        Website website19 = new Website(KeyUtil.UUID(),"/recovery/delete","回收站删除","delete",1);
//        Website website20= new Website(KeyUtil.UUID(),"/recovery/restore","回收站恢复","update",1);
//
//
//        Website website21 = new Website(KeyUtil.UUID(),"/template/findAll","查询所有模板","query",1);
//        Website website22 = new Website(KeyUtil.UUID(),"/template/templateData","模板数据管理","query",1);
//        Website website23 = new Website(KeyUtil.UUID(),"/template/showData","显示模板数据","query",1);
//        Website website24 = new Website(KeyUtil.UUID(),"/template/delete","删除模板","delete",1);
//        Website website25 = new Website(KeyUtil.UUID(),"/template/uploadTemplate","上传模板","save",1);
//        Website website26 = new Website(KeyUtil.UUID(),"/template/saveCategory","保存模板数据（分类）","save",1);
//        Website website27 = new Website(KeyUtil.UUID(),"/template/saveForm","保存模板数据（表单）","save",1);
//        Website website28 = new Website(KeyUtil.UUID(),"/template/saveArticle","保存模板数据（文章）","save",1);
//        Website website29 = new Website(KeyUtil.UUID(),"/template/saveImageData","保存模板数据（图片）","save",1);
//        Website website30 = new Website(KeyUtil.UUID(),"/template/saveFileData","保存模板数据（文件）","save",1);
//        Website website31 = new Website(KeyUtil.UUID(),"/template/saveProductData","保存模板数据（产品）","save",1);
//        Website website32 = new Website(KeyUtil.UUID(),"/template/deleteBlogData","删除模板数据","delete",1);
//        Website website33 = new Website(KeyUtil.UUID(),"/template/editTemplate","修改模板","update",1);
//
//
//        Website website34 = new Website(KeyUtil.UUID(),"/formItem/add","后台添加一条表单项","save",1);
//        Website website35 = new Website(KeyUtil.UUID(),"/formItem/findByForm","查询属于该表单的全部表单项","query",1);
//        Website website36 = new Website(KeyUtil.UUID(),"/formItem/delete","删除一条表单项","delete",1);
//        Website website37 = new Website(KeyUtil.UUID(),"/formItem/saveOption","保存下拉列表项","save",1);
//        Website website38 = new Website(KeyUtil.UUID(),"/formItem/saveFormItem","保存表单项的各个属性(除下拉列表项)","save",1);
//        Website website39 = new Website(KeyUtil.UUID(),"/form/addName","修改表单名","update",1);
//        Website website40 = new Website(KeyUtil.UUID(),"/form/addForms","后台添加一条表单","save",1);
//        Website website41 = new Website(KeyUtil.UUID(),"/form/findPageForm","分页查询全部表单","query",1);
//        Website website42 = new Website(KeyUtil.UUID(),"/form/delete","将表单放入回收站中","delete",1);
//        Website website43 = new Website(KeyUtil.UUID(),"/form/processForm","将用户提交的表单处理(设置为已读或异常)","update",1);
//
//        Website website44 = new Website(KeyUtil.UUID(),"/excel/getForm","生成用户提交表单的excel","query",1);
//        Website website45 = new Website(KeyUtil.UUID(),"/excel/uploadProduct","产品Excel上传","add",1);
//        Website website46 = new Website(KeyUtil.UUID(),"/excel/getProduct","产品Excel下载","query",1);
//
//        Website website47 = new Website(KeyUtil.UUID(),"/image/findAll","查询所有图片","query",1);
//        Website website48 = new Website(KeyUtil.UUID(),"/image/save","上传图片","query",1);
//        Website website49 = new Website(KeyUtil.UUID(),"/image/delete","删除图片","query",1);
//        Website website50 = new Website(KeyUtil.UUID(),"/image/addImageCategory","图片添加分类","query",1);
//
//
//        Website website51 = new Website(KeyUtil.UUID(),"/file/upload","单文件上传","add",1);
//        Website website52 = new Website(KeyUtil.UUID(),"/file/multiUpload","多文件上传","add",1);
//        Website website53 = new Website(KeyUtil.UUID(),"/file/fileDownload","文件下载","query",1);
//
//
//        Website website54 = new Website(KeyUtil.UUID(),"/product/deleteProduct","删除产品","delete",1);
//        Website website55 = new Website(KeyUtil.UUID(),"/product/addName","修改产品名称","update",1);
//        Website website56 = new Website(KeyUtil.UUID(),"/product/findWorktable","查询全部上传的Excel表格","query",1);
//        Website website57 = new Website(KeyUtil.UUID(),"/product/deleteWorktable","将Excel表格放入回收站","query",1);
//        Website website58 = new Website(KeyUtil.UUID(),"/product/findProductFile","产品查找文件","query",1);
//        Website website59 = new Website(KeyUtil.UUID(),"/product/proPackageDownload","产品数据包下载","query",1);
//
//        Website website60 = new Website(KeyUtil.UUID(),"/search/searchAll","搜索","query",1);


//        Website website61 = new Website(KeyUtil.UUID(),"/admin/delete","删除文件到回收站","query",1);
          Website website62 = new Website(KeyUtil.UUID(),"/admin/categoryProduct","产品管理分类下查找产品","query",1);


        List<Website> list = new ArrayList<>();
//        list.add(website1);
//        list.add(website2);
//        list.add(website3);
//        list.add(website4);
//        list.add(website5);
//        list.add(website6);
//        list.add(website7);
//        list.add(website8);
//        list.add(website9);
//        list.add(website10);
//        list.add(website11);
//        list.add(website12);
//        list.add(website13);
//        list.add(website14);
//        list.add(website15);
//        list.add(website16);
//        list.add(website17);
//        list.add(website19);
//        list.add(website20);
//        list.add(website21);
//        list.add(website22);
//        list.add(website23);
//        list.add(website24);
//        list.add(website25);
//        list.add(website26);
//        list.add(website27);
//        list.add(website28);
//        list.add(website29);
//        list.add(website30);
//        list.add(website31);
//        list.add(website32);
//        list.add(website33);
//        list.add(website34);
//        list.add(website35);
//        list.add(website36);
//        list.add(website37);
//        list.add(website38);
//        list.add(website39);
//        list.add(website40);
//        list.add(website41);
//        list.add(website42);
//        list.add(website43);
//        list.add(website44);
//        list.add(website45);
//        list.add(website46);
//        list.add(website47);
//        list.add(website48);
//        list.add(website49);
//        list.add(website50);
//        list.add(website51);
//        list.add(website52);
//        list.add(website53);
//        list.add(website54);
//        list.add(website55);
//        list.add(website56);
//        list.add(website57);
//        list.add(website58);
//        list.add(website59);
//        list.add(website60);
//        list.add(website61);
        list.add(website62);

        list.stream().forEach(x -> websiteDao.save(x));
    }

  @org.junit.Test
    public void test1(){
        User user = userService.findByUserId("dcbf4aae65994897875bba3a3397628f");
        log.info("【user】={}",user.getAuthorities().size());
        Assert.assertNotNull(user);
    }

    @org.junit.Test
    public void test2(){
        Website website1 = new Website(KeyUtil.UUID(),"/admin/blog/detail/**","首页","query",1);
        websiteDao.save(website1);
    }


}

*/
