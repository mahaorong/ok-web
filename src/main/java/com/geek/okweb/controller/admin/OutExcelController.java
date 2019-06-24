
package com.geek.okweb.controller.admin;

import com.geek.okweb.domain.Product;
import com.geek.okweb.domain.ProductData;
import com.geek.okweb.enums.ExcelEnum;
import com.geek.okweb.exception.ExcelException;
import com.geek.okweb.service.ProductService;
import com.geek.okweb.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 批量导入
 */
@Slf4j
@Controller
@RequestMapping(value = "/excel")
public class OutExcelController {

    @Autowired
    private ProductService productService;

    /**
     * 创建表头
     * @param workbook excel表格中的内容
     * @param sheet    sheet中的设置
     */
    private void createTitle(Workbook workbook, Sheet sheet, List<Product> product) {
        Row row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第一个参数为第几列,第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1, 12 * 256);
        sheet.setColumnWidth(4, 18 * 256);

        //创建格式对象
        CellStyle cellStyle = ExcelUtil.cellStyle(workbook);

        /**
         * 创建表头的标题名称以及设置格式
         */
        Cell cell;
        //添加第一行，以此类推
        cell = row.createCell(0);
        cell.setCellValue("产品序列");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("产品名称");
        cell.setCellStyle(cellStyle);

        int length = 2;
        Product product1 = product.get(0);
        for (int j = 0 ; j < product1.getJson().size() ; j++){
            ProductData productData = product1.getJson().get(j);
            cell = row.createCell(length);
            cell.setCellValue(productData.getProauctName());
            cell.setCellStyle(cellStyle);
            length++;
        }
    }

    /**
     * 生成Product表的excel
     * @param response 设置HTTP状态码和管理Cookie。
     * @return
     * @throws Exception
     */
    @GetMapping("/getProduct")
    public String getProduct(HttpServletResponse response, @RequestParam("fileName") String fileName, @RequestParam("wid") String wid) throws Exception {
        try {
            //读取数据(更改数据时更换此处)
            List<Product> rows = productService.findByWidProduct(wid);
            //转换Excel Workbook 是HSSFWorkbook(2003版)还是XSSFWorkbook(2007版)
            Workbook workbook = ExcelUtil.ExcelConversion(fileName);
            //创建工作表
            Sheet sheet = workbook.createSheet();
            log.info("sheet========{}",sheet);
            //设置表头
            createTitle(workbook,sheet,rows);
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
            int rowNum = 1;
            int lenth = 2;
            for (Product product : rows) {
                //输入数据
                Row row = sheet.createRow(rowNum);
                Cell cell;

                cell = row.createCell(0);
                cell.setCellValue(product.getId());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(1);
                cell.setCellValue(product.getName());
                cell.setCellStyle(cellStyle);

                if (product.getJson() == null && product.getJson().size() == 0){
                }else {
                    for (ProductData productData : product.getJson()) {
                        cell = row.createCell(lenth);
                        cell.setCellValue(productData.getProductMsg());
                        cell.setCellStyle(cellStyle);
                        lenth++;
                     }
                }
                lenth=2;
                rowNum++;
            }
            log.info("fileName=={}",fileName);
            ExcelUtil.buildExcelDocument(fileName,workbook,response);
        } catch (Exception e) {
            log.info("数据为空{}", e);
            log.info("下载失败，导致的原因可能是:" + e.getMessage());
            return "error";
        }
        return "download excel";
    }






}
