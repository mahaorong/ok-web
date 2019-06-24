package com.geek.okweb;

import com.geek.okweb.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Slf4j
@SpringBootApplication
@EnableAsync //开启异步注解
public class OkWebApplication  {


    public static void main(String[] args) {
        SpringApplication.run(OkWebApplication.class, args);

    }

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public SpringResourceTemplateResolver templateResolver(){

        String path = FileUtil.getPath();

        String templatesResource = path + "/templates/";
        String staticResource = path + "/static/";
        FileUtil.fileExist(templatesResource);
        FileUtil.fileExist(staticResource);


        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);

        resolver.setPrefix("file:///"+templatesResource);

        resolver.setCacheable(false);//不允许缓存
        resolver.setCharacterEncoding("UTF-8");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        log.info("【创立成功】");

        return resolver;
    }
}
