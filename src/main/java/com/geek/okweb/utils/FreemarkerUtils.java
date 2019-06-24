package com.geek.okweb.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.*;

/**
 * 页面静态化
 * @author guyinyihun
 *
 */
@Slf4j
@Component
public class FreemarkerUtils {

/*    TemplateEngine templateEngine;*/



    /*@Autowired
    private FileTemplateResolver fileTemplateResolver;*/

    @Autowired
    private TemplateEngine templateEngine;

   /* @Value("${template.path}")
    private String templatePath;*/


   /* @Value("${static.path}")
    private String staticPath;*/


    /**
     * 生成静态文件
     * @param freeTempName 模板名称
     * @param context 数据内容
     * @param outFilePath 输出路径
     * @return
     */
    public boolean process(String freeTempName, WebContext context, String outFilePath) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFilePath,false),"UTF-8"));
            templateEngine.process(freeTempName,context,writer);
            log.info("【写入*****】");
        } catch (IOException e) {
            e.printStackTrace();
            log.info("【失败*****】");
            return false;
        } finally {
            try {
               writer.flush();
               writer.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}

