package com.geek.okweb.service;

import com.alibaba.fastjson.JSON;
import com.geek.okweb.dao.TemplateDao;
import com.geek.okweb.domain.Template;
import com.geek.okweb.domain.TemplateData;
import com.geek.okweb.utils.FreemarkerUtils;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Transactional
public class TemplateService {

    @Autowired
    private TemplateDao templateDao;

    @Autowired
    private FreemarkerUtils freemarkerUtils;

    @Autowired
    private BlogService blogService;


    @Autowired
    private FormItemService formItemService;

    @Autowired
    private FormService formService;

    @Autowired
    private CategoryService categoryService;


    public void saveOrUpdate(Template template){
        templateDao.save(template);
        log.info("=====【保存操作】=====");
    }

    public void delete(String id){
        Template template = findById(id);
        templateDao.delete(template);
    }

    public Template findById(String id){
        return templateDao.findById(id);
    }

    public Template findByUrl(String url,String language){
        return templateDao.findByUrl(url,language);
    }

    public MyPage<Template> findAll(Integer page,Integer pageSize, String language){
        return templateDao.findAll(page,pageSize,language);
    }

    public List<Template> findAll(){
        return templateDao.findAll();
    }

    public void update(Template template){
        templateDao.update(template);
    }

    /**
     * 生成模板
     * @param templateId
     * @return
     *//*
    public String generateTemplate(String templateId, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext){
        //创建thymeleaf上下文对象（Model）
        WebContext context = new WebContext(request, response, servletContext,request.getLocale());
//        Context context = new Context();
        //根据模板id查询模板对象
        Template template = templateDao.findById(templateId);
        //获取模板地址
        String path = template.getPath();
        //获取模板数据列表
        List<TemplateData> templateDatas = template.getData();

       log.info("【进入】={}",template);
       //遍历模板数据列表
        for (TemplateData td : templateDatas){
            if (StringUtils.equals(td.getType(),"blogs")){ //如果模板数据属于blogs类型
                //根据模板数据里的文章id列表查询文章列表
               List<Blog> blogList = blogService.findByIds(td.getIds());
               log.info("ID文章={}",blogList);
               //填充数据
               context.setVariable(td.getKey(), blogList);
            } else if (StringUtils.equals(td.getType(),"category")){ //如果模板数据属于category类型
                //根据模板数据里的标签名称列表查询文章列表
                List<Blog> blogList = blogService.findByCategorys(td.getIds());
                log.info("分类文章={}",blogList);
                //填充数据
                context.setVariable(td.getKey(), blogList);
            } else if (StringUtils.equals(td.getType(),"form")){ //如果模板数据属于tags类型
                //根据模板数据里的标签名称列表查询文章列表
                List<Form> formList = formService.findByIds(td.getIds());
                Form form = formList.get(0);
                List<FormItem> formItemList = formItemService.findByForm(form.getId());
                //填充数据
                context.setVariable(td.getKey(), formItemList);
                context.setVariable("formId", form.getId());
            }
        }

        String resoucePath = FileUtil.getPath();
        //渲染模板
        freemarkerUtils.process(path,context,resoucePath+"/templates/"+template.getUrl()+".html");
        return "success";
    }
*/
    public void merge(Template template){
        templateDao.merge(template);
    }


    /**
     * 更新模板数据
     * @param id id
     * @param templateId 模板id
     * @param key 模板键
     * @return
     */
    public String updateData(String id, String templateId, String key,Integer number){
        //根据模板id查询模板
        Template template = findById(templateId);
        //获取模板数据列表
        List<TemplateData> datas = template.getData();
        //遍历模板数据列表
        for (TemplateData td : datas){
            if (StringUtils.equals(key,td.getKey())){ //根据key匹配属于该key的模板数据对象
                //获取模板数据类型
                String type = td.getType();
                Set<String> ids = td.getIds();
                if ("blogs".equals(type)){ //如果模板数据是blogs类型
                    //将文章id添加进模板数据里
                    ids.add(id);
                }else if ("category".equals(type)){ //如果模板数据是category类型
                    List<String> list = JSON.parseArray(id, String.class);
                    ids.clear();
                    ids.addAll(list);
                    td.setNumber(number);
                    log.info("更新分类成功");
                }else if ("image".equals(type)){ //如果模板数据是image类型
                    ids.add(id);

                }else if("product".equals(type)){
                    ids.removeAll(td.getIds());
                    ids.add(id);

                }else if("file".equals(type)){
                    ids.add(id);

                }else if ("form".equals(type)){ //如果模板数据是form类型
                    ids.removeAll(td.getIds());
                    ids.add(id);
                }else if ("worktable".equals(type)){
                    ids.add(id);
                }
                //重新把模板数据列表设置进模板里
                template.setData(datas);
                //保存更新模板
                merge(template);
                //返回模板数据的key
                return td.getKey();
            }
        }
        return "new";
    }


}
