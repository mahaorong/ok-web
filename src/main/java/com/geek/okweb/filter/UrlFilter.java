package com.geek.okweb.filter;

import com.geek.okweb.domain.*;
import com.geek.okweb.service.BlogService;
import com.geek.okweb.service.CategoryService;
import com.geek.okweb.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UrlFilter implements Filter {

    private String url;
    private String cid;
    private String key;
    private String ids;
    private String pageAction;
    private Integer page;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private BlogService blogService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("【url过滤器】开始转换url");
        Category category = categoryService.findOne();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        String[] restUrls = requestURI.split("/");
        List<String> list = new ArrayList();
        for (int i = 1; i < restUrls.length; i++){
            list.add(restUrls[i]);
            if (StringUtils.contains(restUrls[restUrls.length - 1], ";")) {
                String str = restUrls[restUrls.length - 1];
                String newStr = str.substring(0, str.indexOf(";"));
                log.info("【newStr = {}】", newStr);
                restUrls[restUrls.length - 1] = newStr;
            }
        }


        if (list.size() == 1) {
            OnceUrlReversion(request, response, list);
            return;
        }

        if (list.size() == 3) {
            ThirdUrlReversion(category, request, response, list);
            return;
        }

        if (list.size() == 4) {
            FourthUrlReversion(category, request, response, list);
            return;
        }

        if (list.size() == 5) {
            FifthUrlReversion(category, request, response, list);
            return;
        }

        if (list.size() == 6) {
            SixUrlReversion(category, request, response, list);
            return;
        }

/*        log.info("【请求参数url】={}", url);
        log.info("【请求参数cid】={}", cid);
        log.info("【请求参数key】={}", key);
        log.info("【请求参数ids】={}", ids);*/
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void OnceUrlReversion(HttpServletRequest request, HttpServletResponse response, List<String> list) throws ServletException, IOException {
        url = list.get(0);
        log.info("【url】={}",url);
        String lang = request.getParameter("lang");
        request.getRequestDispatcher("/" + url).forward(request, response);
    }


    private void ThirdUrlReversion(Category category, HttpServletRequest request, HttpServletResponse response, List<String> list) throws ServletException, IOException {
        url = list.get(0);
        cid = list.get(1);
        key = list.get(2);

        cid = cidReversion(category, request, url, cid, key);

        log.info("【转换后的分类】 = {}",cid);

        request.getRequestDispatcher("/" + url + "?cid=" + cid + "&key=" + key).forward(request, response);
    }

    private void FourthUrlReversion(Category category, HttpServletRequest request, HttpServletResponse response, List<String> list) throws ServletException, IOException {

        url = list.get(0);
        cid = list.get(1);
        key = list.get(2);
        ids = list.get(3);

        convertArticleAndCategory(category, request);

        log.info("ids获取=={}", ids);

        request.getRequestDispatcher("/" + url + "?cid=" + cid + "&key=" + key + "&ids=" + ids).forward(request, response);
    }

    private void FifthUrlReversion(Category category, HttpServletRequest request, HttpServletResponse response, List<String> list) throws ServletException, IOException {

        url = list.get(0);
        cid = list.get(1);
        key = list.get(2);
        ids = list.get(3);
        page = Integer.parseInt(list.get(4));
        log.info("【page={}】",page);

        convertArticleAndCategory(category, request);

        request.getRequestDispatcher("/" + url + "?cid=" + cid + "&key=" + key + "&ids=" + ids + "&page=" + page).forward(request, response);
    }

    private void SixUrlReversion(Category category, HttpServletRequest request, HttpServletResponse response, List<String> list) throws ServletException, IOException {

        url = list.get(0);
        cid = list.get(1);
        key = list.get(2);
        ids = list.get(3);
        pageAction = list.get(4);
        page = Integer.parseInt(list.get(5));

        convertArticleAndCategory(category, request);
        log.info("【文章id】={}", ids);
        request.getRequestDispatcher("/" + url + "?cid=" + cid + "&key=" + key + "&ids=" + ids + "&articleFlag=" + pageAction + "&page=" + page).forward(request, response);
    }

    /**
     * 将文章名称和分类转换成文章ID和分类ID
     * @param category
     * @param request
     * @throws UnsupportedEncodingException
     */
    private void convertArticleAndCategory(Category category, HttpServletRequest request) throws UnsupportedEncodingException {
        HttpSession session = request.getSession();
        String lang = (String) session.getAttribute("lang");
        if (StringUtils.isBlank(lang)) {
            lang = "zh_CN";
        }

        log.info("【文章名称转码前】={}", ids);
        ids = URLDecoder.decode(ids, "UTF-8");
        log.info("【文章名称转码后】={}", ids);

//        cid = cidReversion(category, request, url, cid, key);
        cid = cidReversion(category, request, url, cid, key);

        Blog blog = blogService.findByCategoryAndTitle(cid, ids, 1, 0, 0, lang);
        if (blog != null) {
            log.info("文章转码成功");
            ids = blog.getId();
        }
    }

    /**
     * 将分类名称首字母转换成分类ID
     * @param category 分类
     * @param request
     * @param url 请求地址
     * @param cid 分类名称首字母缩写
     * @param key 模板key
     * @return
     */
    private String cidReversion(Category category, HttpServletRequest request, String url, String cid, String key) {
//        String mathCid = "";
        List<String> cateIds = category.findIdsByLowName(cid);
        log.info("【根据分类缩写查询 = {}】", cateIds);
        HttpSession session = request.getSession();
//        String lang = (String) session.getAttribute("lang");
        String lang = request.getParameter("lang");
        if (StringUtils.isBlank(lang)) {
            lang = "zh_CN";
        }
        Template template = templateService.findByUrl(url, lang);
        if (template != null){
            List<TemplateData> tds = template.getData();

            for (TemplateData td : tds) {
                if (StringUtils.equals(td.getKey(), key) && StringUtils.equals("category",td.getType())) {
                    String id = td.getIds().iterator().next();
                    Cateitem cateitem = category.findCateitem(id);
                    List<Cateitem> nodes = cateitem.getNodes();
                    for (String cateId : cateIds){
                        if (StringUtils.equals(id, cateId)) {
                            return id;
                        } else {
                            return recursion(nodes, cateId);
                        }
                    }
                }
            }
        }
        return cid;
//        return mathCid;
    }

    private String recursion(List<Cateitem> cateitemList, String cateId){
//        String matchCateId = "";
        for (Cateitem cateitem : cateitemList) {
            if (StringUtils.equals(cateId, cateitem.getId())) {
                return cateId;
            }else {
                recursion(cateitem.getNodes(), cateId);
            }
        }
        return cateId;
//        return matchCateId;
    }

    @Override
    public void destroy() {

    }


}
