package com.geek.okweb.controller.admin;

import com.geek.okweb.config.WebSocket;
import com.geek.okweb.domain.Category;
import com.geek.okweb.domain.Form;
import com.geek.okweb.domain.FormItem;
import com.geek.okweb.form.FormItemData;
import com.geek.okweb.form.FormItemPackage;
import com.geek.okweb.service.CategoryService;
import com.geek.okweb.service.EmailService;
import com.geek.okweb.service.FormItemService;
import com.geek.okweb.service.FormService;
import com.geek.okweb.utils.MyPage;
import com.geek.okweb.utils.RegularUtil;
import com.geek.okweb.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequestMapping("/form")
public class FormController {

    @Autowired
    private FormService formService;

    @Autowired
    private FormItemService formItemService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WebSocket webSocket;

    @Value("${spring.mail.username}")
    private String toEmail;


    /**
     * 显示表单结果数据
     * @param formId
     * @return
     */
    @PostMapping("/showForm")
    @ResponseBody
    public List<FormItemPackage> showForms(@RequestParam String formId){
        List<FormItem> formItems = formItemService.findByForm(formId);
        List<FormItemPackage> formItemPackages = new ArrayList<>();
        formItems.stream().forEach(x -> {
            FormItemPackage formItemPackage = new FormItemPackage();
            formItemPackage.setName(x.getName());
            formItemPackage.setResult(x.getResult());
            formItemPackages.add(formItemPackage);
        });
        return formItemPackages;
    }

    @GetMapping("/findByLang")
    @ResponseBody
    public String findByLang(@RequestParam String id){
        Form form = formService.findById(id);
        return form.getLanguage();
    }

    @GetMapping("/saveLang")
    public String saveLang(@RequestParam String formId, @RequestParam String lang) {
        Form form = formService.findById(formId);
        form.setLanguage(lang);
        formService.update(form);
        return "redirect:/form/findPageForm?status=0&shape=3";
    }

    /**
     * 修改表单名
     * @param formId 表单id
     * @param formName 表单名字
     * @return
     */
    @PostMapping("/addName")
    @ResponseBody
    public String saveName(@RequestParam String formId,@RequestParam String formName) {
        log.info("已经入");
        formService.saveName(formId, formName);
        return "success";
    }

    /**
     * 保存用户提交的数据
     * @param formItemData 提交数据
     * @return
     */
    @PostMapping("/saveForm")
    @ResponseBody
    public Object saveForm(@Valid FormItemData formItemData, BindingResult be) {
        Map<String, Object> map = new HashMap<>();
        String message = "";
        try {
            if (StringUtils.isNotBlank(formItemData.getInputCode()) && StringUtils.isNotBlank(formItemData.getCodeToUp())) {
                if (StringUtils.equalsIgnoreCase(formItemData.getInputCode(), formItemData.getCodeToUp())) {
                    if (be.hasErrors()) {
                        List<FieldError> allErrors = be.getFieldErrors();
                        Form form = formService.findById(formItemData.getFormId());
                        List<FormItem> formItems = formItemService.findByForm(form.getId());
                        for (FieldError fieldError : allErrors) {
                            Integer sort = 0;
                            String fileName = fieldError.getField();

                            for (FormItem formItem : formItems) {
                                if (StringUtils.equals(formItem.getName(), fileName)) {
                                    sort = formItem.getSort();
                                }
                            }
                            String fileMessage = fieldError.getDefaultMessage();
                            map.put("checkData" + sort, fileMessage);
                        }
                    }else {
                        String[] resultIds = formItemData.getId(); // 存放表单项类型的id
                        String[] ids = formItemData.getIds(); // 存放单选项的id
                        String[] radioId = formItemData.getRadioId(); //存放多选项的id
                        if (resultIds.length > 0) {
                            for (int i = 0; i < resultIds.length; i++) {
                                String resultId = resultIds[i];
                                FormItem formItem = formItemService.findById(resultId);
                                List<String> verifys = formItem.getVerify();
                                if (verifys != null && verifys.size() > 0) {
                                    String result = formItemData.getResult().get(i);
                                    result = StringUtils.trim(result);
                                    boolean regularVerify = RegularUtil.getListDatasRegularVerify(result, verifys);
                                    log.info("regular=={}", regularVerify);
                                    if (!regularVerify){
                                        message = RegularUtil.getMessDatasRegularVerify(result, verifys);
                                        log.info(formItem.getName()+"======================"+message);
                                        map.put("checkData"+formItem.getSort(), message);
                                    }
                                }
                            }
                        }
                        if (ids != null && ids.length > 0) {
                            for (int i = 0; i < ids.length; i++) {

                                String checkId = ids[i];
                                FormItem formItem = formItemService.findById(checkId);
                                List<String> verify = formItem.getVerify();
                                if (verify != null && verify.size() != 0) {
                                    if (formItemData.getCheckbox() != null){// formItemData.getCheckbox() != null 如果多选项的结果为空,则表示多选项全都没选
                                        List<String> resultList = new ArrayList<>();
                                        if (i < formItemData.getCheckbox().length){ // i -> 当前id位置   formItemData.getCheckbox().length checkbox结果总数   如果当前id位置小于结果总数时，就往结果中取值，否则就为一个新建的list取值
                                            resultList = formItemData.getCheckbox()[i];
                                        }

                                        String verifyData = "";
                                        if (resultList != null && resultList.size() > 0) {
                                            verifyData = resultList.get(0);
                                        }
                                        log.info("resutlId={},result=={}", checkId, verifyData);
                                        boolean regularVerify = RegularUtil.getListDatasRegularVerify(verifyData, verify);
                                        log.info("regular=={}", regularVerify);
                                        if (!regularVerify) {
                                            message = RegularUtil.getMessDatasRegularVerify(verifyData, verify);
                                            log.info(formItem.getName() + "======================" + message);
                                            map.put("checkData" + formItem.getSort(), message);
                                        }
                                    }else {
                                        map.put("checkData" + formItem.getSort(), "数据不能为空");
                                    }
                                }
                            }
                        }
                        if (radioId != null && radioId.length > 0) {
                            for (int i = 0; i < radioId.length; i++) {
                                String radio = radioId[i];
                                FormItem formItem = formItemService.findById(radio);
                                List<String> verify = formItem.getVerify();
                                if (verify != null && verify.size() != 0) {
                                    if (formItemData.getRadio() != null){
                                        List<String> strings = new ArrayList<>();
                                        if (i < formItemData.getRadio().length){
                                            strings = formItemData.getRadio()[i];
                                        }
                                        String verifyData = "";
                                        if (strings != null && strings.size() > 0   ) {
                                            verifyData = strings.get(0);
                                        }
                                        log.info("resutlId={},result=={}", radio, verifyData);
                                        boolean regularVerify = RegularUtil.getListDatasRegularVerify(verifyData, verify);
                                        if (!regularVerify){
                                            message = RegularUtil.getMessDatasRegularVerify(verifyData, verify);
                                            log.info(formItem.getSort()+"======================"+message);
                                            map.put("checkData"+formItem.getSort(), message);
                                        }
                                    }else {
                                        map.put("checkData" + formItem.getSort(), "数据不能为空");
                                    }
                                }
                            }
                        }
                        if (map != null && map.size() > 0) {
                            return map;
                        }
                    }
                    Map<String, List<String>> saveForm = formService.saveForm(formItemData.getFormId(), formItemData.getId(),
                            formItemData.getResult(), formItemData.getIds(), formItemData.getCheckbox(),
                            formItemData.getRadioId(), formItemData.getRadio());
                    log.info("【邮箱数据】 = {}", saveForm);
                    Form result = formService.findById(formItemData.getFormId());
                    String content = "";
                    for (String key : saveForm.keySet()) {
                        List<String> list = saveForm.get(key);
                        content += key + "：" + list + "<br>";
                    }
                    content="<html>\n"+"<body>\n"
                            + content
                            + "<br><a href='http://enroo.com/form/findAllForm'>点击前往查看</a>\n"
                            +"</body>\n"+"</html>";

                    emailService.sendSimpleMail(toEmail, result.getName(), content);
                    //提交表单后，提醒后台有新表单
                    webSocket.sendMessage("new form新表单来袭");
                    return ResultUtil.success("提交成功");
                }else {
                    return ResultUtil.verify("不正确");
                }
            }else {
                return ResultUtil.verify("不能为空");
            }
        } catch (Exception e) {
            log.info("erroe={}", e);
            return ResultUtil.fail();
        }
    }

    @GetMapping("/findAllForm")
    public String findAllUserForm(@RequestParam(value = "page",defaultValue = "1",required = false) Integer page,
                                    @RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize,
                                    Model model){
        MyPage<Form> formPage = formService.findAllUserForm(pageSize, page);
        model.addAttribute("formPage", formPage);
        return "/admin/new_web/form_all";
    }

    /**
     * 分页查询全部表单
     * @param status 回收状态
     * @param shape 表单状态
     * @param page 当前页
     * @param pageSize 每页显示数量
     * @param model
     * @return
     */
    @GetMapping("/findPageForm")
    public String findByFormPage(
            @RequestParam("status") Integer status,@RequestParam("shape") Integer shape,
            @RequestParam(value = "page",defaultValue = "1",required = false)int page,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false)int pageSize,
            Model model){
        MyPage<Form> formPage = formService.findByFormPage(status,shape,page,pageSize);
        List<String> formIds = formPage.getItems().stream().map(x -> x.getId()).collect(Collectors.toList());
        model.addAttribute("formPage",formPage);
        model.addAttribute("formIds", formIds);
        model.addAttribute("shape",shape);
        if (shape == 3) {
            Category category = categoryService.findOne();
            model.addAttribute("category", category);
            return "admin/new_web/form_admin";
        }
        else if (shape == 0)
            return "admin/new_web/form_untreated";
        else if (shape == 1)
            return "admin/new_web/form_processed";
        else if (shape == 2)
            return "admin/new_web/form_abnormal";
        return null;
    }

    @GetMapping("/batchHandleForm")
    @ResponseBody
    public String batchSucForm(@RequestParam List<String> formIds,@RequestParam Integer shape){
        if (formIds != null && formIds.size() != 0) {
            log.info("===========================开始处理===========================");
            formIds.stream().forEach(x -> formService.processForm(x, shape));
            return "success";
        } else {
            log.info("===========================处理失败===========================");
            return "error";
        }
    }

    @GetMapping("/batchDeteForm")
    @ResponseBody
    public String batchDeteForm(@RequestParam List<String> formIds) {
        if (formIds != null && formIds.size() != 0) {
            log.info("===========================开始处理===========================");
            formIds.stream().forEach(x -> formService.delete(x));
            return "success";
        } else {
            log.info("===========================处理失败===========================");
            return "error";
        }
    }

    /**
     * 后台添加一条表单
     * @return
     */
    @GetMapping("/addForms")
    public String addForms() {
        formService.save();
        return "redirect:/form/findPageForm?status=0&shape=3";
    }

    /**
     * 将表单放入回收站中
     * @param formId 表单id
     * @param shape 表单状态 1-----已读转态   2----异常状态
     * @return
     */
    @GetMapping("/delete")
    public String delete(@RequestParam String formId,@RequestParam Integer shape , @RequestParam Integer page){
        formService.delete(formId);
        return "redirect:/form/findPageForm?page="+page+"&status=0&shape="+shape;
    }

    /**
     * 处理表单
     * @param formId 表单id
     * @param shape  表单状态 1-----已读转态   2----异常状态
     * @return
     */
    @GetMapping("/processForm")
    public String processForm(@RequestParam String formId,@RequestParam Integer page,@RequestParam Integer shape){
        formService.processForm(formId,shape);
        return "redirect:/form/findPageForm?page="+page+"&status=0&shape=0";
    }

    /**
     * 表单选择分类
     * @param cateids 分类id集合
     * @param formIds 表单id集合
     * @return
     */
    @GetMapping("/choseCategory")
    public String choseCategory(@RequestParam("cateids")List<String> cateids,@RequestParam("formIds")List<String> formIds){
        formIds.stream().forEach(id -> {
            Form form = formService.findById(id);
            form.setCateids(cateids);
            formService.update(form);
        });
        return "redirect:/form/findPageForm?status=0&shape=3";
    }

}
