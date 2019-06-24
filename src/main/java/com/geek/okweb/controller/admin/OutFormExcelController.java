
package com.geek.okweb.controller.admin;

import com.geek.okweb.domain.Form;
import com.geek.okweb.domain.FormItem;
import com.geek.okweb.enums.ExcelEnum;
import com.geek.okweb.exception.ExcelException;
import com.geek.okweb.service.FormItemService;
import com.geek.okweb.service.FormService;
import com.geek.okweb.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 批量导入
 */
@Slf4j
@Controller
@RequestMapping(value = "/excel")
public class OutFormExcelController {

    @Autowired
    private FormItemService formItemService;

    @Autowired
    private FormService formService;

    /**
     * 创建表头
     *
     * @param workbook excel表格中的内容
     * @param sheet    sheet中的设置
     */
    private void createTitle(Workbook workbook, Sheet sheet) {
        Row row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第一个参数为第几列,第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0, 45 * 256);
        sheet.setColumnWidth(1, 30 * 256);
        sheet.setColumnWidth(2, 30 * 256);

        //创建格式对象
        CellStyle cellStyle = ExcelUtil.cellStyle(workbook);

//        Integer formSize = rows.size();
        Integer number = 0;
        /**
         * 创建表头的标题名称以及设置格式
         */
        Cell cell;
        //添加第一行，以此类推
//        for (int i = 0; i < formSize; i++) {
            cell = row.createCell(0);
            cell.setCellValue("提交时间");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue("表单项名称");
            cell.setCellStyle(cellStyle);
            /*number++;*/
            cell = row.createCell(2);
            cell.setCellValue("表单项结果");
            cell.setCellStyle(cellStyle);
            /*number++;*/
            /*cell = row.createCell(number);
            cell.setCellStyle(cellStyle);*/
            /*number++;*/
//        }
    }

    /**
     * 生成用户提交表单的excel
     *
     * @param response 设置HTTP状态码和管理Cookie。
     * @return
     * @throws Exception
     */
    @GetMapping("/getForm")
    @ResponseBody
    public String getForm(HttpServletResponse response, @RequestParam(value = "formId", required = false) List<String> formId, @RequestParam(value = "formName",required = false) String formName) throws Exception {
        try {
            log.info("formName=={}", formName);
            if (StringUtils.isBlank(formName)) {
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                formName = format.format(date)+".xlsx";
            }
            Workbook workbook = ExcelUtil.ExcelConversion(formName);
            //创建工作表
            int i = formName.indexOf(".");
            String formName1 = formName.substring(0, i);
            Sheet sheet = workbook.createSheet(formName1 + "工作表");
            //读取数据(更改数据时更换此处)
            List<Form> rows = new ArrayList<>();
            if (formId != null && formId.size() != 0) {
                log.info("导出勾选");
                for (String formId1 : formId) {
                    rows.add(formService.findById(formId1));
                }
            } else {
                log.info("导出全部");
                rows = formService.findAllByStatus();
            }

            //设置表头
            createTitle(workbook, sheet);
            /**
             * 判断数据是否为空
             */
            if (rows.size() == 0) {
                throw new ExcelException(ExcelEnum.EXCEL_DATA_NOT_FOUND);
            }
            log.info("size{}", rows.size());
            //创建表格格式
            CellStyle cellStyle = ExcelUtil.cellStyle(workbook);
            int rowNum = 1;
            int number = 1;
            //新增数据行，并且设置单元格数据
            for (Form form : rows) {
                List<FormItem> formItems = formItemService.findByForm(form.getId());

                Cell cell;
                Row rows1 = sheet.createRow(rowNum);
                cell = rows1.createCell(0);
                cell.setCellValue(form.getCreateTime() + form.getName());
                cell.setCellStyle(cellStyle);
                rowNum++;

                if (formItems != null && formItems.size() != 0) {
                    for (FormItem formItem : formItems) {
                        //输入数据
                        Row row = sheet.createRow(rowNum);

                        cell = row.createCell(number);
                        cell.setCellValue(formItem.getName());
                        cell.setCellStyle(cellStyle);

                        cell = row.createCell(number+1);
                        cell.setCellValue(String.join(",", formItem.getResult()));
                        cell.setCellStyle(cellStyle);
                        /*number++;
                        number = number - 3;*/
                        number = 1;
                        rowNum++;
                    }
                    /*number = number + 3;
                    rowNum = 1;
                    log.info("number=={}", number);*/
                }
            }
            //浏览器下载excel
            ExcelUtil.buildExcelDocument(formName, workbook, response);
        } catch (Exception e) {
            log.info("数据为空{}", e);
            return "下载失败，导致的原因可能是:" + e.getMessage();
        }
        return "download excel";
    }
}
