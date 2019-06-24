package com.geek.okweb.service;

import com.geek.okweb.dao.FormDao;
import com.geek.okweb.dao.FormItemDao;
import com.geek.okweb.domain.Form;
import com.geek.okweb.domain.FormItem;
import com.geek.okweb.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
public class FormItemService {

    @Autowired
    private FormItemDao formItemDao;

    @Autowired
    private FormDao formDao;

    //保存一条初始表单项
    public void save(String formId, Integer number,Integer index) {
        Form form = formDao.findById(formId);
        if (number != null && number != 0) {
            for (int i = 0; i < number; i++) {
                saveFormItem(form,index);
                index++;
            }
        } else {
            saveFormItem(form,0);
        }

    }

    public void saveFormItem(Form form,Integer index) {
        FormItem formItem = new FormItem();
        formItem.setId(KeyUtil.UUID());
        formItem.setForm(form);
        //默认类型为文本类型
        formItem.setType("text");
        //将该formId下的所有表单项重新进行排序
        List<FormItem> itemList = formItemDao.findByForm("All", form.getId());
        log.info("itemListSize=={}", itemList.size());
        if (index == 0) {
            if (itemList.size() != 0) {
                for (FormItem formItem1 : itemList) {
                    Integer sort = formItem1.getSort();
                    sort++;
                    formItem1.setSort(sort);
                    formItemDao.merge(formItem1);
                }
            }
            //设置新插入的表单项排序
            formItem.setSort(1);
        } else {
            log.info("index=={}",index);
            if (itemList.size() != 0) {
                for (FormItem formItem1 : itemList) {
                    Integer sort = formItem1.getSort();
                    if (sort < index) {
                    } else {
                        sort++;
                        formItem1.setSort(sort);
                        formItemDao.merge(formItem1);
                    }
                }
            }
            formItem.setSort(index);
        }

        formItemDao.save(formItem);
    }

    public void save(FormItem formItem) {
        formItemDao.save(formItem);
    }

    //查询全部表单项
    public List<FormItem> findAll() {
        List<FormItem> formItemList = formItemDao.findAll();
        return formItemList;
    }

    //查询当个表单项
    public FormItem findById(String id) {
        FormItem formItem = formItemDao.findByFormItem(id);
        return formItem;
    }

    //删除
    public void delete(String formItemId, String formId) {
        FormItem formItem = formItemDao.findByFormItem(formItemId);
        log.info("formItem===={}", formItem);
        formItemDao.delete(formItem);
        //删除一个表单项后重新排序
        SortRadio("sort", "All", formId);
    }

    //对文本,单选,多选的keyValue进行排序
    public void SetSortOrKey(String formId) {
        SortRadio(null, "radio", formId);
        SortRadio(null, "checkbox", formId);
        SortRadio(null, "text", formId);
    }

    public void SortRadio(String key, String type, String formId) {
        List<FormItem> formItems = formItemDao.findByForm(type, formId);
        //删除时对表单项的顺序进行排序
        if (StringUtils.equals(key, "sort")) {
            if (formItems.size() != 0) {
                Integer number = 0;
                for (int i = 0; i < formItems.size(); i++) {
                    FormItem formItem = formItems.get(i);
                    number++;
                    formItem.setSort(number);
                    formItemDao.update(formItem);
                }
            }
            //给keyValue进行排序
        } else {
            if (formItems.size() != 0) {
                for (int i = 0; i < formItems.size(); i++) {
                    FormItem formItem = formItems.get(i);
                    formItem.setKeyValue(i);
                    formItemDao.update(formItem);
                }
            }
        }
    }

    //更新
    public void update(FormItem formItem) {
        formItemDao.update(formItem);
    }

    //根据表单id查询全部表单项
    public List<FormItem> findByForm(String formId) {
        List<FormItem> formItemList = formItemDao.findByForm("All", formId);
        return formItemList;
    }

    public List<String> findAllType(String formId) {
        return formItemDao.findByType(formId);
    }
}
