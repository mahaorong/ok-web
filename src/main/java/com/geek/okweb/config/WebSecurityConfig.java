package com.geek.okweb.config;


import com.geek.okweb.utils.FileUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
    public final static String USERID="user";

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = FileUtil.getPath();
        registry.addResourceHandler("/imagePath/**").addResourceLocations("file:///"+path+"/upload/");
        registry.addResourceHandler("/video/**").addResourceLocations("file:///"+path+"/video/");
        registry.addResourceHandler("/**").addResourceLocations("file:///"+path+"/static/");
//        registry.addResourceHandler("/index/**").addResourceLocations("file:/E:\\enroo\\ok-web\\src\\main\\resources\\templates\\index.html");

    }

    public void addInterceptors(InterceptorRegistry registry){
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/");
        addInterceptor.excludePathPatterns("/blog");
        addInterceptor.excludePathPatterns("/blog/detail/{id}");
        addInterceptor.excludePathPatterns("/register");
        addInterceptor.excludePathPatterns("/static/**");
        addInterceptor.excludePathPatterns("/templates/template/**");
        addInterceptor.excludePathPatterns("/form/saveForm");
        addInterceptor.excludePathPatterns("/product/proPackageDownload");
        addInterceptor.excludePathPatterns("/file/fileDownload");


        // 拦截配置
        addInterceptor.addPathPatterns("/addBlog");
        addInterceptor.addPathPatterns("/admin/**");
        addInterceptor.addPathPatterns("/template/findAll");
        addInterceptor.addPathPatterns("/image/**");
        addInterceptor.addPathPatterns("/form/**");
        addInterceptor.addPathPatterns("/formItem/**");
        addInterceptor.addPathPatterns("/product/**");
        addInterceptor.addPathPatterns("/changePassword");
        //addInterceptor.addPathPatterns("/addComment/{blogId}/{userId}");
        //addInterceptor.addPathPatterns("/addSubreview/comment/{commentId}/{blogId}/{userId}");
    }




    class SecurityInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        HttpSession session = request.getSession();
        if(session.getAttribute(USERID)!=null) {
            return true;
        }else {
            String url="/login";
            response.sendRedirect(url);
            return false;
        }

    }
}

}