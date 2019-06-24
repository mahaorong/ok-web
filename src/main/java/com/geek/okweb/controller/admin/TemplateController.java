package com.geek.okweb.controller.admin;

import com.alibaba.fastjson.JSON;
import com.geek.okweb.domain.*;
import com.geek.okweb.form.TemplateForm;
import com.geek.okweb.service.*;
import com.geek.okweb.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FormService formService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private WorktableService worktableService;

    @Autowired
    private ProductService productService;

    /**
     * 查询所有模板
     * @param model
     * @return
     */
    @GetMapping("/findAll")
    public String findAll(Model model,@RequestParam(value = "page",defaultValue = "1",required = false)int page,
                          @RequestParam(value = "pageSize",defaultValue = "99",required = false)int pageSize,
                          @RequestParam(value = "language",required = true,defaultValue = "zh_CN") String language){
        if (StringUtils.isBlank(language)){
            language = "zh_CN";
        }
        MyPage<Template> templatePage = templateService.findAll(page,pageSize,language);
        model.addAttribute("templatePage",templatePage);
        return "admin/new_web/model_form";
    }

    /**
     * 删除模板
     * @param templateId
     * @return
     */
    @ResponseBody
    @GetMapping("/delete")
    public String delete(@RequestParam("templateId") String templateId){
        templateService.delete(templateId);
        return "success";
    }

  /*  *//**
     * 生成模板
     * @param templateId
     * @return
     *//*
    @ResponseBody
    @GetMapping("/generateTemplate")
    public String generateTemplate(@RequestParam("templateId") String templateId, HttpServletRequest request, HttpServletResponse response){
        ServletContext servletContext = request.getServletContext();
        log.info("【servletContext】={}",request.getServletPath());
        templateService.generateTemplate(templateId,request,response,servletContext);

        return "success";
    }*/


    /**
     * 上传模板文件
     * @param templateForm
     * @return
     */
    @PostMapping("/uploadTemplate")
    public String uploadTemplate(TemplateForm templateForm, @RequestParam(name = "templateFile",required = false)MultipartFile templateFile,Model model){
        try {
            //创建模板
            Template template = new Template();
            String uuid = KeyUtil.UUID();
            String templateName = templateForm.getTemplateName(); //模板名称
            String requestUrl = templateForm.getRequestUrl(); //请求地址
            String language = templateForm.getLanguage();
            log.info("模板名称：{}",templateName);
            log.info("请求地址：{}",requestUrl);
            log.info("语言：{}",language);
            log.info("templateFile：{}",templateFile.getOriginalFilename());

            if (StringUtils.isNotBlank(templateFile.getOriginalFilename()) ){ //判断是否文件名为空，为空则没上传文件
                String filename = templateFile.getOriginalFilename();
                log.info("文件名称：{}",filename);
                String resoucePath = FileUtil.getPath();
                String templatePath =resoucePath+"/templates/";
                if(StringUtils.equals("en_US",language)){
                    templatePath += "en_US/";
                }else if(StringUtils.equals("zh_TW",language)){
                    templatePath += "zh_TW/";
                }else {
                    templatePath += "zh_CN/";
                }

                //判断文件是否存在
                FileUtil.fileExist(templatePath);

                //上传模板文件
                FileUtil.uploadFile(templateFile.getBytes(),templatePath,filename);
                log.info("上传成功：{}",filename);
                template.setPath(templatePath+filename);
            }

            template.setUrl(requestUrl);
            template.setName(templateName);
            template.setId(uuid);
            template.setLanguage(language);
            template.setKeywords(templateForm.getKeywords());
            template.setDescription(templateForm.getDescription());
            template.setPosition(templateForm.getPosition());
            template.setTmpGroup(templateForm.getTmpGroup());

            templateService.saveOrUpdate(template);
        }catch (Exception e){
            log.error("上传失败：{}",e);
        }

        return "redirect:/template/findAll";
    }

    /**
     * 返回模板数据页面
     * @param model 模型
     * @param templateId 模板id
     * @param page 当前页
     * @param pageSize 每页显示
     * @param key key
     * @return
     */
    @GetMapping("/templateData")
    public String templateData(Model model,@RequestParam(name = "templateId",required = false)String templateId,@RequestParam(value = "page",defaultValue = "1",required = false)int page,
                                @RequestParam(value = "pageSize",defaultValue = "5",required = false)int pageSize,
                               @RequestParam(name = "key",required = false) String key,@RequestParam(name = "url",required = false)String url,@RequestParam(name = "cateids",required = false)Set<String> cateids){
        MyPage<Blog> blogPage;
        MyPage<Image> imagePage;
        MyPage<FileUpload> filePage;
        MyPage<Form> formPage;
        MyPage<Product> productPage;
        MyPage<Worktable> worktablePage;

        if (null != cateids && cateids.size() > 0){
           blogPage = blogService.findByCategorys(cateids, page, pageSize, 1, null); //文章
           imagePage = imageService.findImageByCategroys(cateids, page, pageSize); //图片
           filePage= fileService.findByCategorys(cateids, page, pageSize, null);  //文件
           formPage = formService.findByFormPage(0,3,page,pageSize); //表单
           productPage = productService.findByCategorys(cateids, page, pageSize);
           worktablePage = worktableService.findWorkByCategroys(cateids,page,pageSize);
        }else {
            blogPage = blogService.findPageAll(page, pageSize,0,1); //文章
            imagePage = imageService.findAllByStatus(0,page,pageSize); //所有图片
            filePage = fileService.findAllByStatus(0, page, pageSize);//文件
            formPage = formService.findByFormPage(0,3,page,pageSize); //表单
            productPage = productService.findAllPage(page,pageSize);
            worktablePage = worktableService.findByWorkTablePage(0,page,pageSize);
        }

        log.info("formPage==={}", formPage.getItems().toString());

        Category category = categoryService.findOne(); //分类
        if (templateId != null && StringUtils.isNotEmpty(key)){
            Template template = templateService.findById(templateId);

            for (TemplateData data : template.getData()){
                if (StringUtils.equals(key,data.getKey())){
                    Set<String> ids = data.getIds();
                    model.addAttribute("ids",ids);
                    model.addAttribute("td",data);
                }
            }
        }

        if (cateids != null && cateids.size() != 0){
            model.addAttribute("cateids", cateids.iterator().next());
        }

        log.info("模板id={}",templateId);
        log.info("key={}",key);
        model.addAttribute("category",category);
        model.addAttribute("imagePage",imagePage);
        model.addAttribute("formPage",formPage);
        model.addAttribute("pages",blogPage);
        model.addAttribute("templateId",templateId);
        model.addAttribute("key",key);
        model.addAttribute("filePage",filePage);
        model.addAttribute("productPage",productPage);
        model.addAttribute("worktablePage",worktablePage);
        log.info("【url】={}",url);
        return "admin/new_web/"+url;
    }


    /**
     * 保存关于该分类的所有文章
     * @param jsonId 标签id
     * @param templateId 模板id
     * @param key key
     * @return
     */
    @ResponseBody
    @PostMapping("/saveCategory")
    public String saveTag(String jsonId,String templateId,@RequestParam(name = "key",required = false)String key,@RequestParam(name = "number",required = false)Integer number){
        log.info("模板ID={}",templateId);
        System.out.println("********集合数据："+jsonId);
        log.info("key={}",key);
        log.info("number={}",number);
        List<String> list = JSON.parseArray(jsonId, String.class);
        Set<String> categorysName = new HashSet<String>();
        categorysName.addAll(list);
        log.info("list={}",list);

        if ("new".equals(key)){ //如果是新建模板数据
            //根据模板id查询模板
            Template template = templateService.findById(templateId);
            //获取模板数据列表
            List<TemplateData> datas = template.getData();

            Integer order = 0;
            for (TemplateData dt : datas) {
                if (dt.getOrder() > order)
                    order = dt.getOrder();
            }

            //创建模板数据对象
            TemplateData newtd = new TemplateData("category", categorysName,order+1);
            newtd.setNumber(number);
            //添加进模板数据列表
            datas.add(newtd);
            //重新设置数据回模板里
            template.setData(datas);
            //保存更新模板
            templateService.merge(template);
            return newtd.getKey();
        }else {  //如果是更新模板数据
              String keyResult = templateService.updateData(jsonId,templateId,key,number);
              return keyResult;
        }

    }

    /**
     * 保存表单
     * @param jsonId 表单id
     * @param templateId 模板id
     * @param key key
     * @return
     */
    @ResponseBody
    @PostMapping("/saveForm")
    public String saveForm(String jsonId,String templateId,@RequestParam(name = "key",required = false)String key){
        log.info("模板ID={}",templateId);
        log.info("表单ID={}",jsonId);
        log.info("key={}",key);
        Set<String> formId = new HashSet<String>();
//        List<String> list = Arrays.asList(jsonId);
        formId.add(jsonId);
        if ("new".equals(key)){ //如果是新建模板数据
            //根据模板id查询模板
            Template template = templateService.findById(templateId);
            //获取模板数据列表
            List<TemplateData> datas = template.getData();

            Integer order = 0;
            for (TemplateData dt : datas) {
                if (dt.getOrder() > order)
                    order = dt.getOrder();
            }

            //创建模板数据对象
            TemplateData newtd = new TemplateData("form",formId,order+1);
            //添加进模板数据列表
            datas.add(newtd);
            //重新设置数据回模板里
            template.setData(datas);
            //保存更新模板
            templateService.merge(template);
            return newtd.getKey();
        }else {  //如果是更新模板数据
            log.info("【进入更新操作】");
              String keyResult = templateService.updateData(jsonId,templateId,key,null);
              return keyResult;
        }
    }

    /**
     * 回显模板数据
     * @param templateId 模板id
     * @param model
     * @return
     */
    @GetMapping("/showData")
    public String showData(String templateId,Model model){
        Template template = templateService.findById(templateId);
        Category category = categoryService.findOne();
        List<TemplateData> tds = template.getData();
        Map<String,String> tdNames = new HashMap<>();

        tds.stream().forEach((td) -> {
            if (StringUtils.equals("category", td.getType())) {
                String id = td.getIds().iterator().next();
                Cateitem cateitem = category.findCateitem(id);
                tdNames.put(td.getKey(),cateitem.getText());
            }
        });

        model.addAttribute("templateId",templateId);
        model.addAttribute("dataList",tds);
        model.addAttribute("tdNames",tdNames);
        return "admin/new_web/model_dataType";
    }

    /**
     * 删除模板数据
     * @param templateId
     * @param key
     * @return
     */
    @ResponseBody
    @GetMapping("/deleteData")
    public String deleteTemplateData(@RequestParam("templateId")String templateId,String key){
        Template template = templateService.findById(templateId);
        List<TemplateData> datas = template.getData();
        for (TemplateData td : datas){
            if (StringUtils.equals(key,td.getKey())){
                datas.remove(td);
                templateService.merge(template);
                return templateId;
            }
        }
        return templateId;
    }


    /**
     * 保存模板数据（文章）
     * @param jsonId 文章id
     * @param templateId 模板id
     * @param key key
     * @return
     */
    @ResponseBody
    @PostMapping("/saveArticle")
    public String saveArticle(String jsonId,String templateId,@RequestParam(name = "key",required = false)String key){
        log.info("模板ID={}",templateId);
        log.info("文章ID={}",jsonId);
        Set<String> blogIds = new HashSet<String>();
        blogIds.add(jsonId);
        log.info("key值={}",key);
        if (("new").equals(key)){
            //根据模板id查询模板信息
            Template template = templateService.findById(templateId);
            List<TemplateData> datas = template.getData();
            if (datas == null){
                datas = new ArrayList<>();
            }
            Integer order = 0;
            for (TemplateData dt : template.getData()) {
                if (dt.getOrder() > order)
                order = dt.getOrder();
            }
            TemplateData newtd = new TemplateData("blogs", blogIds, order+1);
            log.info("【进入保存新数据操作】={}",newtd);
            datas.add(newtd);
            template.setData(datas);
            //更新模板
            templateService.merge(template);
            return newtd.getKey();
        }else {
            log.info("【更新文章操作】");
            String keyResult = templateService.updateData(jsonId,templateId,key,null);
            return keyResult;
        }
    }

    /**
     * 保存模板数据
     * @param jsonId 图片id
     * @param templateId 模板id
     * @param key key
     * @return
     */
    @ResponseBody
    @PostMapping("/saveImageData")
    public String saveImageData(String jsonId,String templateId,@RequestParam(name = "key",required = false)String key){
        log.info("模板ID={}",templateId);
        log.info("图片ID={}",jsonId);
        Set<String> imageId = new HashSet<String>();
        imageId.add(jsonId);
        log.info("key值={}",key);
        if (("new").equals(key)){
            //根据模板id查询模板信息
            Template template = templateService.findById(templateId);

            List<TemplateData> datas = template.getData();
            if (datas == null){
                datas = new ArrayList<>();
            }
            Integer order = 0;
            for (TemplateData dt : template.getData()) {
                if (dt.getOrder() > order)
                    order = dt.getOrder();
            }
            TemplateData newtd = new TemplateData("image", imageId, order+1);
            log.info("【进入保存新数据操作】={}",newtd);
            datas.add(newtd);
            template.setData(datas);
            //更新模板
            templateService.merge(template);
            return newtd.getKey();
        }else {
            log.info("【更新文章操作】");
            String keyResult = templateService.updateData(jsonId,templateId,key,null);
            return keyResult;
        }
    }


    /**
     * 保存文件数据
     * @param jsonId
     * @param templateId 模板id
     * @param key key
     * @return
     */
    @ResponseBody
    @PostMapping("/saveFileData")
    public String saveFileData(String jsonId,String templateId,@RequestParam(name = "key",required = false)String key){
        log.info("模板ID={}",templateId);
        log.info("文件id={}",jsonId);
        Set<String> fileIds = new HashSet<String>();
        fileIds.add(jsonId);
        log.info("key值={}",key);
        if (("new").equals(key)){
            //根据模板id查询模板信息
            Template template = templateService.findById(templateId);

            List<TemplateData> datas = template.getData();
            if (datas == null){
                datas = new ArrayList<>();
            }

            Integer order = 0;
            for (TemplateData dt : template.getData()) {
                if (dt.getOrder() > order)
                    order = dt.getOrder();
            }

            TemplateData newtd = new TemplateData("file", fileIds, order+1);
            log.info("【进入保存新数据操作】={}",newtd);
            datas.add(newtd);
            template.setData(datas);
            //更新模板
            templateService.merge(template);
            return newtd.getKey();
        }else {
            log.info("【更新文件操作】");
            String keyResult = templateService.updateData(jsonId,templateId,key,null);
            return keyResult;
        }

    }


//    /**
//     * 保存产品数据
//     * @param jsonId
//     * @param templateId 模板id
//     * @param key key
//     * @return
//     */
//    @ResponseBody
//    @PostMapping("/saveProductData")
//    public String saveProductData(String jsonId,String templateId,@RequestParam(name = "key",required = false)String key){
//        log.info("模板ID={}",templateId);
//        log.info("文件id={}",jsonId);
//        Set<String> productIds = new HashSet<String>();
//        productIds.add(jsonId);
//        log.info("key值={}",key);
//        if (("new").equals(key)){
//            //根据模板id查询模板信息
//            Template template = templateService.findById(templateId);
//
//            List<TemplateData> datas = template.getData();
//            if (datas == null){
//                datas = new ArrayList<>();
//            }
//
//            Integer order = 0;
//            for (TemplateData dt : template.getData()) {
//                if (dt.getOrder() > order)
//                    order = dt.getOrder();
//            }
//
//            TemplateData newtd = new TemplateData("product",productIds, order+1);
//            log.info("【进入保存新数据操作】={}",newtd);
//            datas.add(newtd);
//            template.setData(datas);
//            //更新模板
//            templateService.merge(template);
//            return newtd.getKey();
//        }else {
//            log.info("【更新文件操作】");
//            String keyResult = templateService.updateData(jsonId,templateId,key,null);
//            return keyResult;
//        }
//    }
//


    /**
     * 保存产品数据
     * @param jsonId
     * @param templateId 模板id
     * @param key key
     * @return
     */
    @ResponseBody
    @PostMapping("/saveProductData")
    public String saveWorktableData(String jsonId,String templateId,@RequestParam(name = "key",required = false)String key){
        log.info("模板ID={}",templateId);
        log.info("文件id={}",jsonId);
        Set<String> worktableIds = new HashSet<String>();
        worktableIds.add(jsonId);
        log.info("key值={}",key);
        if (("new").equals(key)){
            //根据模板id查询模板信息
            Template template = templateService.findById(templateId);

            List<TemplateData> datas = template.getData();
            if (datas == null){
                datas = new ArrayList<>();
            }

            Integer order = 0;
            for (TemplateData dt : template.getData()) {
                if (dt.getOrder() > order)
                    order = dt.getOrder();
            }

            TemplateData newtd = new TemplateData("worktable",worktableIds, order+1);
            log.info("【进入保存新数据操作】={}",newtd);
            datas.add(newtd);
            template.setData(datas);
            //更新模板
            templateService.merge(template);
            return newtd.getKey();
        }else {
            log.info("【更新文件操作】");
            String keyResult = templateService.updateData(jsonId,templateId,key,null);
            return keyResult;
        }
    }




    /**
     *
     * 删除模板数据里的一条文章数据
     * @param templateId
     * @param key
     * @return
     */
    @ResponseBody
    @PostMapping("/deleteBlogData")
    public String deleteBlogData(String templateId,String key,String jsonId){
        log.info("id={}",jsonId);
        Template template = templateService.findById(templateId);
        List<TemplateData> datas = template.getData();
        for (TemplateData td : datas){
            if (StringUtils.equals(key,td.getKey())){
                Set<String> ids = td.getIds();
                    ids.remove(jsonId);
                    td.setIds(ids);
                }
                template.setData(datas);
                templateService.merge(template);
            }
        return templateId;

    }

    @ResponseBody
    @GetMapping("/editTemplate")
    public Result editTemplate(@RequestParam("templateId")String templateId, @RequestParam("value")String value, @RequestParam("type")String type){

        log.info("【templateId】={}",templateId);
        log.info("【value】={}",value);
        log.info("【type】={}",type);
        Template template = templateService.findById(templateId);
        if (null != template){
            if (StringUtils.equals("templateName",type))
                template.setName(value);
            else if (StringUtils.equals("url",type))
                template.setUrl(value);
            else if (StringUtils.equals("keywords",type))
                template.setKeywords(value);
            else if (StringUtils.equals("description",type))
                template.setDescription(value);
            else if (StringUtils.equals("title",type))
                template.setTitle(value);
            templateService.update(template);
            return ResultUtil.success("success");
        }else {
            return ResultUtil.fail();
        }
    }

    /**
     * 下载模板文件代码
     * @param templateId
     * @param response
     */
    @GetMapping("/downloadTemplate")
    public void downloadTemplate (String templateId, HttpServletResponse response){
        Template template = templateService.findById(templateId);
        String resource = template.getPath();
        String fileName = resource.substring(resource.lastIndexOf("/")+1);
        response.setHeader("content-disposition", "attachment;filename="+fileName);
        String path = template.getPath();
        File file = new File(path);
        // 根据文件路径获取要下载的文件输入流
        try {
            InputStream in = new FileInputStream(file);
            int len = 0;
            // 创建数据缓冲区
            byte[] buffer = new byte[1024];
            // 通过response对象获取OutputStream流
            OutputStream out = response.getOutputStream();
            // 将FileInputStream流写入到buffer缓冲区
            while ((len = in.read(buffer)) > 0) {
                // 使用OutputStream将缓冲区的数据输出到客户端浏览器
                out.write(buffer, 0, len);
            }
        }catch(Exception e){
            log.error("下载失败",e);
        }
    }


    /**
     * 读取html文件内容
     * @param id 模板id
     * @return str 文件内容
     */
    @GetMapping("/readHtmlContent")
    @ResponseBody
    public String readHtmlContent(@RequestParam("id")String id) {
        try {
            Template template = templateService.findById(id);  //根据id查询模板
            String path = template.getPath(); //获得模板文件路径
            String lineTxt = "";

            File file = new File(path); //构建File文件对象
            if (file.isFile() && file.exists()){  //判断该文件是否存在
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
                int line = 1;
                while ( (lineTxt = br.readLine() )!= null) { //逐行读取文件内容，不读取换行符和末尾的空格
//                   log.info("信息：{}",lineTxt);
                    sb.append(lineTxt+"\n"); //将读取的字符串添加换行符后累加存放在缓存中
                    line++;
                }
                br.close();
                String str = sb.toString(); //获得字符串缓存
//                log.info("文件内容={}",str);
                return str;
            } else {
                log.info("文件不存在！");
            }
        } catch (Exception e){
            log.error("文件出错",e);
        }

        return "";
    }

    @PostMapping("/saveEditor")
    public String saveEditor(@RequestParam("id")String id, @RequestParam("htmlContent")String htmlContent){
        if (StringUtils.isNotBlank(htmlContent) && StringUtils.isNotBlank(id)){
            Template template = templateService.findById(id);
            if (template != null){
                String path = template.getPath(); //获取文件路径
                try {
                    OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(path),"UTF-8");
//                    FileWriter writer = new FileWriter(path);
                    BufferedWriter bw = new BufferedWriter(writerStream);//创建文件输出流
                    bw.write(htmlContent);
                    bw.flush(); //刷新缓存
                    bw.close(); //关闭流
                    log.info("写入成功！");
                }catch (Exception e){
                    log.error("文件不存在！",e);
                }
            }
        }
        return "redirect:/template/findAll";
    }

    @PostMapping("/templateGroup")
    @ResponseBody
    public Result templateGroup(@RequestParam String id, @RequestParam Integer tmpGroup){
        Template template = templateService.findById(id);
        if (template == null) {
            return ResultUtil.fail();
        }
        template.setTmpGroup(tmpGroup);
        templateService.update(template);
        return ResultUtil.success("分组成功");
    }


}

