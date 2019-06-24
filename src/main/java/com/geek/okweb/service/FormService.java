package com.geek.okweb.service;

import com.geek.okweb.dao.FormDao;
import com.geek.okweb.dao.FormItemDao;
import com.geek.okweb.domain.Form;
import com.geek.okweb.domain.FormItem;
import com.geek.okweb.utils.KeyUtil;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@Transactional
public class FormService {
    @Autowired
    private FormDao formDao;

    @Autowired
    private FormItemDao formItemDao;

    //后台保存一条表单
    public void save(){
        Form form = new Form();
        form.setId(KeyUtil.UUID());
        form.setShape(3);
        formDao.save(form);
    }

    //修改表单名字
    public void saveName(String formId,String formName){
        Form form = formDao.findById(formId);
        form.setName(formName);
        formDao.update(form);
    }

    //保存用户传来的表单数据
    public Map<String, List<String>> saveForm(String formId, String[] id, List<String> result,
                         String[] checkbookId, List<String>[] checkbox,
                         String[] radioId, List<String>[] radio) {
        String userName = "匿名用户";

        Map<String, List<String>> map = new HashMap<>();
        /*if (user != null){
            userName = user.getUsername();
        }*/
        //根据表单id创建表单并返回该表单
        Form form = formDao.packageFormData(formId, userName);
        if (null != id && 0 != result.size()) {
            //遍历单项选择
            for (int i = 0; i < id.length; i++) {
                //遍历id,查出对应的表单项
                FormItem formItem = formItemDao.findByFormItem(id[i]);
                //取出结果
                List<String> resultList = new ArrayList<>();
                String resultResul = result.get(i);
                resultList.add(resultResul);
                map.put(formItem.getName(), resultList);
                //封装表单项的数据,生成带有结果的表单项
                formItemDao.packageFormItemData(resultList, form, formItem, userName);
        }
        }
        if (null != checkbookId && null != checkbox) {
            //遍历多选的id
            for (int i = 0; i < checkbookId.length; i++) {
                //根据id查出表单项
                FormItem formItem = formItemDao.findByFormItem(checkbookId[i]);
                //拿出结果
                List<String> resultList = new ArrayList<>();
                if(i < checkbox.length){
                    resultList = checkbox[i];
                }
                map.put(formItem.getName(), resultList);

                //封装表单项的数据,生成带有结果的表单项
                formItemDao.packageFormItemData(resultList, form, formItem, userName);
            }
        }
        if (null != radioId && null != radio) {
            //遍历单选的id
            for (int i = 0; i < radioId.length; i++) {
                //根据id查出表单项
                FormItem formItem = formItemDao.findByFormItem(radioId[i]);
                //拿取数据
                List<String> resultList = new ArrayList<>();
                if (i < radio.length){
                    resultList = radio[i];
                }
                map.put(formItem.getName(), resultList);

                //封装表单项的数据,生成带有结果的表单项
                formItemDao.packageFormItemData(resultList, form, formItem, userName);
            }
        }
        //更新带有结果表单项的表单
//        Form form1 = formDao.findById(form.getId());
        return map;
    }

    /**
     * 分页查询
     * @param page 当前页
     * @param pageSize 每页显示的数量
     * @return
     */
    public MyPage<Form> findByFormPage(Integer status,Integer shape,int page,int pageSize){
        MyPage<Form> myPage = formDao.findByFormPage(status,shape,page,pageSize);
        return myPage;
    }

    /**
     * 查询全部表单
     * @return
     */
    public List<Form> findAll(){
        List<Form> formList = formDao.findAll();
        return formList;
    }

    /**
     * 根据表单id查询表单
     * @param id
     * @return
     */
    public Form findById(String id){
        Form form = formDao.findById(id);
        return form;
    }

    /**
     * 根据多个表单id查询表单
     *
     * @param ids 装有表单id的set集合
     * @return
     */
    public List<Form> findByIds(Set<String> ids) {
        List<Form> formList = new ArrayList<>();
        Form form ;
        for (String id : ids) {
            form = findById(id);
            formList.add(form);
        }
        return formList;
    }



    /**
     * 将表单放到回收站中
     * @param formId 表单id
     */
    public void delete(String formId){
        formDao.delete(formId);
    }

    /**
     * 删除表单以及关联的表单项
     * @param formId 表单id
     */
    public void deleteAllItem(String formId){
        List<FormItem> itemList = formItemDao.findByForm("All",formId);
        for (FormItem formItem : itemList){
            formItemDao.delete(formItem);
        }
        formDao.deleteForm(formId);
    }

    //更新
    public void update(Form form){
        formDao.update(form);
    }

    //处理表单
    public void processForm(String formId,Integer shape) {
        Form form = formDao.findById(formId);
        form.setShape(shape);
        formDao.update(form);
    }

    public MyPage<Form> findAllByStatus(Integer status,Integer page,Integer pageSize){
        return formDao.findAllByStatus(status, page, pageSize);
    }


    public List<Form> findAllByStatus(){
        return formDao.findAllByStatus();
    }

    public MyPage<Form> findAllUserForm(Integer pageSize, Integer page) {
        return formDao.findByUserForm(pageSize, page);
    }

    public List<Form> findByCataForm(Set<String> ids){
        return formDao.findByTags(ids);
    }

}
