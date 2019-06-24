package com.geek.okweb.controller;

import com.geek.okweb.baidu.ueditor.ActionEnter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@Slf4j
@Controller
public class ServerController{

    @RequestMapping(value="/config")
    public void config(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setContentType("application/json");
        String rootPath =ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/ueditor/jsp";
        try {
            response.setCharacterEncoding("UTF-8");
            String exec = new ActionEnter(request, rootPath).exec();
//            System.out.println(exec);
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
