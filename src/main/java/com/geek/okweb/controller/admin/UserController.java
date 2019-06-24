package com.geek.okweb.controller.admin;

import com.alibaba.fastjson.JSON;
import com.geek.okweb.domain.Authority;
import com.geek.okweb.domain.User;
import com.geek.okweb.domain.Website;
import com.geek.okweb.service.AuthorityService;
import com.geek.okweb.service.UserService;
import com.geek.okweb.service.WebsiteService;
import com.geek.okweb.utils.KeyUtil;
import com.geek.okweb.utils.MyPage;
import com.geek.okweb.utils.Result;
import com.geek.okweb.utils.ResultUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/detail")
    public String getWebUrl(@RequestParam(name = "page",defaultValue="1",required = false) Integer page,
                            @RequestParam(name = "pageSize",defaultValue="10",required = false) Integer pageSize
                            ,Model model,@RequestParam("uid")String uid){
        MyPage<Website> websitePage = websiteService.findByWebPage(pageSize, page);
        List<Authority> authorityList = authorityService.findWidByUid(uid);
        Set<String> set = authorityList.stream().map(x -> x.getWid()).collect(Collectors.toSet());
        List<String> list = new ArrayList<>();
        List<String> webId = websitePage.getItems().stream().map(x -> x.getId()).collect(Collectors.toList());
        webId.stream().forEach(x -> {
            Authority authority = authorityService.findUserAuthority(uid, x);
            if (authority != null) {
                if (authority.getOperateable() == 1){
                    list.add(x);
                }
            }
        });
        if (list.size() == websitePage.getItems().size()){
            model.addAttribute("check", "ture");
        }
        model.addAttribute("websitePage",websitePage);
        log.info("webId=={}", webId.toString());
        model.addAttribute("webId",webId);
        model.addAttribute("uid",uid);
        model.addAttribute("ids",set);
        return "admin/new_web/authority";
    }

    @GetMapping("/findAll")
    public String findAll(Model model,@RequestParam(value = "page",defaultValue = "1",required = false)int page,
                          @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize){
        MyPage<User> userPage = userService.findAll(page,pageSize);
        model.addAttribute("userPage", userPage);
        return "admin/new_web/user_form";
    }

    @ResponseBody
    @GetMapping("/volumeAuto")
    /*.replace("]","")*/
    public String volumeAuto(@RequestParam(value = "wid") String volumeWids,@RequestParam String uid,@RequestParam String flag){
        List<String> volumeWid = JSON.parseArray(volumeWids, String.class);
        try {
            for (int i = 0; i < volumeWid.size(); i++) {
                String wid = volumeWid.get(i);
                User user = userService.findByUserId(uid);
                Authority authority = authorityService.findUserAuthority(uid, wid);
                if (authority == null){ //保存
                    authority = new Authority(KeyUtil.UUID(),wid,1,user);
                }else { //修改
                    Integer operateable = authority.getOperateable();
                    if (StringUtils.equals(flag,"check")){
                        operateable = 1;
                    }else {
                        operateable = 0;
                    }
                    authority.setOperateable(operateable);
                }
                authorityService.merge(authority);
            }
            return "授权成功";
        } catch (Exception e) {
            log.info("授权失败=={}", e);
        }
        return "授权失败";
    }

    @ResponseBody
    @GetMapping("/authorize")
    public String authorize(@RequestParam("wid")String wid, @RequestParam("uid")String uid){
        try {
            log.info("【**************************开始授权**********************】");
            User user = userService.findByUserId(uid);
            Authority authority = authorityService.findUserAuthority(uid, wid);
            if (authority == null){ //保存
                authority = new Authority(KeyUtil.UUID(),wid,1,user);
            }else { //修改
                Integer operateable = authority.getOperateable();
                if (operateable == 0){
                    operateable = 1;
                }else {
                    operateable = 0;
                }
                authority.setOperateable(operateable);
            }
            authorityService.merge(authority);
            return "授权成功";
        } catch (Exception e) {
            log.info("授权失败==={}",e);
        }
        return "授权失败";
    }

    @ResponseBody
    @GetMapping("/delete")
    public String delete(String id){
        userService.delete(id);
        return "success";
    }

    @PostMapping("/banUser")
    @ResponseBody
    public Result banUser(@RequestParam String id, @RequestParam Boolean action){
        User user = userService.findByUserId(id);
        String message = "";
        if (action) {
            user.setUsable(1);
            message = "禁用成功";
        }else {
            user.setUsable(0);
            message = "解封成功";

        }
        userService.update(user);
        return ResultUtil.success(message);
    }

}
