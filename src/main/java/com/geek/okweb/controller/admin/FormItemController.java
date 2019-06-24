package com.geek.okweb.controller.admin;

import com.geek.okweb.domain.FormItem;
import com.geek.okweb.form.FormItemForm;
import com.geek.okweb.service.FormItemService;
import com.geek.okweb.utils.RegularUtil;
import com.geek.okweb.utils.Result;
import com.geek.okweb.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/formItem")
public class FormItemController {
    @Autowired
    private FormItemService formItemService;

    /**
     * 后台添加一条表单项
     *
     * @param formId 表单id
     * @return
     */
    @GetMapping("/add")
    public String save(@RequestParam String formId, @RequestParam(name = "number", required = false) Integer number, @RequestParam(name = "index", required = false) Integer index) {
        formItemService.save(formId, number, index);
        return "redirect:/formItem/findByForm?formId=" + formId;
    }

    /**
     * 查询属于该表单的全部表单项
     *
     * @param formId 表单id
     * @param model
     * @return
     */
    @GetMapping("/findByForm")
    public String findByForm(@RequestParam String formId, Model model) {
        List<FormItem> formItemList = formItemService.findByForm(formId);
        model.addAttribute("ontNull", "[^\\s]{1,}");
        model.addAttribute("email", "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        model.addAttribute("phone", "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[0135678])|(18[0,5-9]))\\d{8}$|^\\d{8}$|^\\d{5}$");
        model.addAttribute("url", "^(?=^.{3,255}$)(http(s)?:\\/\\/)?(www\\.)?[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:\\d+)*(\\/\\w+\\.\\w+)*$");
        model.addAttribute("data", "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))");
        model.addAttribute("posta", "[1-9]\\d{5}");
        model.addAttribute("number", "[0-9]{1,}");
        model.addAttribute("formItemList", formItemList);
        model.addAttribute("formId", formId);
        return "admin/new_web/form_term";
    }

    /**
     * 删除一条表单项
     *
     * @param formItemId 表单项id
     * @param formId     表单id
     * @return
     */
    @GetMapping("/delete")
    public String delete(String formItemId, String formId) {
        formItemService.delete(formItemId, formId);
        return "redirect:/formItem/findByForm?formId=" + formId;
    }

    /**
     * 查询单条评论
     *
     * @param formItemId 表单项id
     * @return
     */
    @PostMapping("/findOption")
    @ResponseBody
    public FormItemForm findOption(@RequestParam String formItemId) {
        FormItem formItem = formItemService.findById(formItemId);
        FormItemForm formItemForm = new FormItemForm();
        BeanUtils.copyProperties(formItem, formItemForm);
        return formItemForm;
    }

    /**
     * 保存下拉列表项
     *
     * @param form 接受后台传来的数据
     * @return
     */
    @PostMapping("/saveOption")
    public String saveOption(FormItemForm form) {
        FormItem formItem = formItemService.findById(form.getId());
        formItem.setOption(form.getOption());
        formItemService.update(formItem);
        return "redirect:/formItem/findByForm?formId=" + formItem.getForm().getId();
    }

    /**
     * 保存表单项的各个属性
     *
     * @param form
     * @return
     */
    @PostMapping("/saveFormItem")
    @ResponseBody
    public Result saveFormItem(FormItemForm form) {
        FormItem formItem = formItemService.findById(form.getId());
        try {
            if (StringUtils.equals(form.getOperating(), "saveType")) {
                log.info("【saveType】");
                formItem.setType(form.getType());
                formItem.setOption(new ArrayList<>());
            } else if (StringUtils.equals(form.getOperating(), "saveName")) {
                log.info("【saveName】");
                if (form.getFlag().equals("Name")) {
                    formItem.setName(form.getName());
                } else {
                    formItem.setRemarks(form.getRemarks());
                }
            } else if (StringUtils.equals(form.getOperating(), "saveSort")) {
                log.info("【saveSort】");
                String[] sorts = form.getSort().split(",");
                String[] formIds = form.getId().split(",");
                for (int i = 0; i < sorts.length; i++) {
                    formItem = formItemService.findById(formIds[i]);
                    Integer sort1 = Integer.valueOf(sorts[i]);
                    formItem.setSort(sort1);
                }
            }
            formItemService.update(formItem);
            return ResultUtil.success("提交成功");
        } catch (Exception e) {
            log.info("error={}", e);
            return ResultUtil.fail();
        }
    }

    @GetMapping("/saveRegular")
    public String saveRegular(@RequestParam String formId) {
        List<FormItem> itemList = formItemService.findByForm(formId);
        itemList.stream().forEach(x -> {
            List<String> verifys = x.getVerify();
            if (verifys.size() == 0) {
                verifys.add(RegularUtil.notNullRegular);
                x.setVerify(verifys);
                formItemService.save(x);
            } else {
                for (String verify : verifys) {
                    if (!StringUtils.equals(verify, RegularUtil.notNullRegular)) {
                        verifys.add(RegularUtil.notNullRegular);
                        x.setVerify(verifys);
                        formItemService.save(x);
                    }
                }
            }
        });
        return "redirect:/formItem/findByForm?formId=" + formId;
    }

    @PostMapping("/saveRegular")
    @ResponseBody
    public String saveRegular(@RequestParam String formItemId, @RequestParam String regular, @RequestParam String flag) {
        try {
            FormItem formItem = formItemService.findById(formItemId);
            List<String> verifyRegular = formItem.getVerify();
            if (verifyRegular == null) { // 防止空指针异常
                verifyRegular = new ArrayList<>();
            }
            if (StringUtils.equals(flag, "save")) {
                log.info("【正则表达式】=={},【序号】=={}", RegularUtil.getVerify(regular), regular);
                if (verifyRegular.size() == 0) {
                    verifyRegular.add(RegularUtil.getVerify(regular));
                } else {
                    for (String regular1 : verifyRegular) {
                        if (StringUtils.equals(regular, regular1)) {
                            log.info("【已经重复】");
                        } else {
                            log.info("【添加】");
                            verifyRegular.add(RegularUtil.getVerify(regular));
                            break;
                        }
                    }
                }
                formItem.setVerify(verifyRegular);
            } else {
                List<String> verify = formItem.getVerify();
                if (verify != null && verify.size() > 0) {
                    for (int i = 0; i < verify.size(); i++) {
                        String verifyData = verify.get(i);
                        if (StringUtils.equals(verifyData, RegularUtil.getVerify(regular))) {
                            verify.remove(i);
                        }
                    }
                }
            }
            formItemService.update(formItem);
            return "success";
        } catch (Exception e) {
            log.error("存放校验规则报错{}", e);
            return "error";
        }
    }

}
