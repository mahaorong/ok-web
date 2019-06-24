package com.geek.okweb;

import com.geek.okweb.dao.WebsiteDao;
import com.geek.okweb.utils.RegularUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class OkWebApplicationTests {

    @Autowired
    private WebsiteDao websiteDao;

    @Test
    public void main(){
        /*Website website = new Website(KeyUtil.UUID(), "/admin/editCompany", "修改公司名称", "add", 1);
        websiteDao.save(website);
        Website website = new Website(KeyUtil.UUID(), "/admin/findOneBlog", "查询单篇博客", "query", 1);
        websiteDao.save(website);*/
        String number = "83410132";
        System.out.println(number.matches(RegularUtil.telephoneRegular));
    }

}
