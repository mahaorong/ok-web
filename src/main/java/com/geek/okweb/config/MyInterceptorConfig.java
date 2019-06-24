package com.geek.okweb.config;

import com.geek.okweb.filter.UrlFilter;
import com.geek.okweb.domain.Template;
import com.geek.okweb.interceptor.WebInterceptor;
import com.geek.okweb.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private TemplateService templateService;

    @Bean
    public Filter urlFilter(){
        return new UrlFilter();
    }


    @Bean
    public FilterRegistrationBean urlFilterRegistration(){
        log.info("【url过滤器进行配置】");
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(new DelegatingFilterProxy("urlFilter"));
        registrationBean.setName("urlFilter");

        List<String> urls = new ArrayList<>();

        List<Template> tmpAll = templateService.findAll();
        if (tmpAll != null && tmpAll.size() > 0) {
            Set<String> tmpUrls = tmpAll.stream().map((tmp) -> tmp.getUrl()).collect(Collectors.toSet());
            tmpUrls.stream().forEach((url) -> {
                url = "/"+ url + "/*";
                log.info("【过滤器】url = {}",url);
                urls.add(url);
            });
        }else {
            urls.add("/index");
        }

        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }

    @Bean
    public WebInterceptor webInterceptor(){
        return new WebInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(webInterceptor());
        registration.addPathPatterns("/admin/**")
                    .addPathPatterns("/form/**")
                    .addPathPatterns("/formItem/**")
                    .addPathPatterns("/product/**")
                    .addPathPatterns("/template/**")
                    .addPathPatterns("/excel/**")
                    .addPathPatterns("/image/**")
                    .addPathPatterns("/recovery/**")
                    .addPathPatterns("/user/findAll")
                    .addPathPatterns("/user/detail")
                    .addPathPatterns("/user/banUser")
                    .addPathPatterns("/file/**");

        registration.excludePathPatterns("/image/imagePath/**");
        registration.excludePathPatterns("/admin/imagePath/**");
        registration.excludePathPatterns("/form/saveForm");
        registration.excludePathPatterns("/admin/uploadImg");
        registration.excludePathPatterns("/product/proPackageDownload");
        registration.excludePathPatterns("/file/fileDownload");
    }
}
