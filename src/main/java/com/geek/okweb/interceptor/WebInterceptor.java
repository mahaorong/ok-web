package com.geek.okweb.interceptor;

import com.geek.okweb.domain.Authority;
import com.geek.okweb.domain.User;
import com.geek.okweb.domain.Website;
import com.geek.okweb.service.AuthorityService;
import com.geek.okweb.service.WebsiteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class WebInterceptor implements HandlerInterceptor {

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("============================拦截器启动==============================");
        String requestURI = request.getRequestURI();
        log.info("【请求地址】={}",requestURI);
        Website website = websiteService.findByUrl(requestURI);
        HttpSession session = request.getSession();
        if (website.getOperateable() == 1){
             if (StringUtils.equals(website.getType(),"query")){
                return true;
            }
            User user =(User) session.getAttribute("user");
            if(null == user){
                response.sendRedirect("/login");
                return false;
            }
//            log.info("user={}",user.getUsername());
            Authority authority = authorityService.findUserAuthority(user.getId(), website.getId());
            if (authority!=null && authority.getOperateable() == 1){
                log.info("【授权成功！！】");
                return true;
            }
            response.sendRedirect("/error");
            return false;
        }else {
            response.sendRedirect("/error");
            return false;
       }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("============================拦截器关闭==============================");
    }
}
