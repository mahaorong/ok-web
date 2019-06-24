package com.geek.okweb.controller.admin;

import com.geek.okweb.domain.Blog;
import com.geek.okweb.enums.ExcelEnum;
import com.geek.okweb.exception.ExcelException;
import com.geek.okweb.service.BlogService;
import com.geek.okweb.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create by Gai on 2018/12/28 10:07
 */
@Slf4j
@Controller
@RequestMapping(value = "/excel")
public class OutArticleController {
    @Autowired
    private BlogService blogService;


    /**
     * 创建表头
     * @param workbook excel表格中的内容
     * @param sheet    sheet中的设置
     */
    private void createTitles(Workbook workbook, Sheet sheet) {
        Row row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第一个参数为第几列,第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0, 24 * 256);
        sheet.setColumnWidth(1, 24 * 256);
        sheet.setColumnWidth(2, 24 * 256);
        sheet.setColumnWidth(3, 24 * 256);
        sheet.setColumnWidth(4, 24 * 256);
        sheet.setColumnWidth(5, 24 * 256);
        sheet.setColumnWidth(6, 24 * 256);
        //创建格式对象
        CellStyle cellStyle = ExcelUtil.cellStyle(workbook);

        /**
         * 创建表头的标题名称以及设置格式
         */
        Cell cell;
        //添加第一行，以此类推
        cell = row.createCell(0);
        cell.setCellValue("文章标题");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("文章摘要");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        cell.setCellValue("文章内容");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(3);
        cell.setCellValue("文章内容");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(4);
        cell.setCellValue("文章内容");
        cell.setCellStyle(cellStyle);


        cell = row.createCell(5);
        cell.setCellValue("文章作者");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(6);
        cell.setCellValue("发布时间");
        cell.setCellStyle(cellStyle);

    }

    /**
     * 生成所有文章表的excel
     * @param response 设置HTTP状态码和管理Cookie。
     * @return
     * @throws Exception
     */
    @GetMapping("/outputArticle")
    public void getProduct(HttpServletResponse response, @RequestParam(value = "articleIds", required = false) List<String> articleIds,
                             @RequestParam(value = "status",required = false)Integer status,
                             @RequestParam(value = "review",required = false)Integer review) throws Exception {
        try {
            List<Blog> rows = new ArrayList<>();
            //读取数据(更改数据时更换此处)
            if (articleIds != null && articleIds.size() > 0){
                for (String id : articleIds){
                    Blog blog = blogService.findById(id);
                    rows.add(blog);
                }
            }else {
                rows = blogService.findAllByStatusAndReview(status,review);
            }

            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String dateFormat = format.format(date);
            String fileName = dateFormat+"已审核文章.xlsx";

            //转换Excel Workbook 是HSSFWorkbook(2003版)还是XSSFWorkbook(2007版)
            Workbook workbook = ExcelUtil.ExcelConversion(fileName);
            //创建工作表
            Sheet sheet = workbook.createSheet();
            log.info("sheet========{}",sheet);

            CellRangeAddress cellAddresses1 = new CellRangeAddress(0,0,2,4);
            sheet.addMergedRegion(cellAddresses1);

            //设置表头
            createTitles(workbook,sheet);
            /**
             * 判断数据是否为空
             */
            if (rows.size() == 0) {
                throw new ExcelException(ExcelEnum.EXCEL_DATA_NOT_FOUND);
            }
            log.info("size{}", rows.size());
            //创建表格格式
            CellStyle cellStyle = ExcelUtil.cellStyle(workbook);

            //新增数据行，并且设置单元格数据
            int number = 1;
            for (Blog blog : rows) {
                //输入数据
                Row row = sheet.createRow(number);
                Cell cell;

                cell = row.createCell(0);
                cell.setCellValue(blog.getTitle());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(1);
                cell.setCellValue(blog.getSummary());
                cell.setCellStyle(cellStyle);


                String content = blog.getContent();
                if (content.length() < 65534 && content.length() > 32767){
                    cell = row.createCell(2);
                    cell.setCellValue(content.substring(0,32767));
                    cell = row.createCell(3);
                    cell.setCellValue(content.substring(32767));
                }else if (content.length() >= 65534){
                    log.info("【文章长度】={}",content.length());
                    log.info("【文章名称】={}",blog.getTitle());
                    cell = row.createCell(2);
                    cell.setCellValue(content.substring(0,32767));
                    cell = row.createCell(3);
                    cell.setCellValue(content.substring(32767,65534));
                    cell = row.createCell(4);
                    cell.setCellValue(content.substring(65534));
                }else{
                    cell = row.createCell(2);
                    cell.setCellValue(content);
                }

                cell.setCellStyle(cellStyle);

                cell = row.createCell(5);
                cell.setCellValue(blog.getUser().getUsername());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(6);
                cell.setCellValue(blog.getCreateTime());
                cell.setCellStyle(cellStyle);

                CellRangeAddress cellAddresses = new CellRangeAddress(number,number,2,4);
                sheet.addMergedRegion(cellAddresses);

                number++;
            }

            ExcelUtil.buildExcelDocument(fileName,workbook,response);
        } catch (Exception e) {
            log.info("数据为空{}", e);
        }
    }
}
