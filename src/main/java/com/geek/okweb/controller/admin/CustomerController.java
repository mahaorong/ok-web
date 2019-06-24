package com.geek.okweb.controller.admin;

import com.geek.okweb.domain.Customer;
import com.geek.okweb.service.CustomerService;
import com.geek.okweb.utils.FileUtil;
import com.geek.okweb.utils.KeyUtil;
import com.geek.okweb.utils.Result;
import com.geek.okweb.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 客服模块
     *
     * @return
     */
    @GetMapping("/customerAll")
    public String customerAll(Model model) {
        List<Customer> customerList = customerService.findAll();
        if (customerList == null || customerList.size() <= 0) {
            Customer customer = new Customer();
            customer.setId(KeyUtil.UUID());
            customerService.save(customer);
            customerList = customerService.findAll();
        }
        Customer customer = customerList.get(0);
        if (StringUtils.isBlank(customer.getCompany())) {
            customer.setCompany("英锐恩科技有限公司");
            customerService.save(customer);
        }
        model.addAttribute("customers", customerList);
        if (null != customerList && customerList.size() > 0) {
            model.addAttribute("id", customerList.get(0).getId());
        }
        return "/admin/new_web/customer_form";
    }

    @GetMapping("/customerEdit")
    @ResponseBody
    public Result customerEdit(@RequestParam("id") String id, @RequestParam("val") String val, @RequestParam("type") String type) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            if (StringUtils.equals(type, "tencent1"))
                customer.setTencent1(val);
            if (StringUtils.equals(type, "tencent2"))
                customer.setTencent2(val);
            if (StringUtils.equals(type, "alibaba"))
                customer.setAlibaba(val);
            if (StringUtils.equals(type, "email"))
                customer.setEmail(val);
            if (StringUtils.equals(type, "phone"))
                customer.setPhone(val);
            customerService.update(customer);
            return ResultUtil.success("success");
        }
        return ResultUtil.fail();
    }

    @PostMapping("/uploadWechat")
    public String uploadWechat(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        try {
            log.info("【上传】={}", file.getOriginalFilename());
            String resoucePath = FileUtil.getPath();
            String templatePath = resoucePath + "/upload/";
            String randomFileName = FileUtil.getRandomFileName(file.getOriginalFilename());
            FileUtil.fileExist(templatePath);
            FileUtil.uploadFile(file.getBytes(), templatePath, randomFileName);
            Customer customer = customerService.findById(id);
            customer.setWechat("/imagePath/" + randomFileName);
            customerService.update(customer);
            return "redirect:/admin/customerAll";
        } catch (Exception e) {
            log.error("【上传失败】={}", e);
        }
        return "redirect:/error";
    }

    @PostMapping("/editCompany")
    public String editCompany(String companyName) {
        List<Customer> customerList = customerService.findAll();
        log.info("companyname == {}", companyName);
        if (customerList == null || customerList.size() <= 0) {
            Customer customer = new Customer();
            customer.setId(KeyUtil.UUID());
            customerService.save(customer);
            customerList = customerService.findAll();
        } else if (StringUtils.isNotBlank(companyName)) {
            Customer customer = customerList.get(0);
            customer.setCompany(companyName);
            customerService.save(customer);
        }
        return "redirect:/admin/index";
    }
}
