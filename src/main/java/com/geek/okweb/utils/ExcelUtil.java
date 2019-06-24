package com.geek.okweb.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Slf4j
public class ExcelUtil {
    /**
     * 判断excel格式是否为2003
     *
     * @param filePath 文件名
     * @return
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 判断excel格式是否为2007
     *
     * @param filePath 文件名
     * @return
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    /**
     * 判断文件是否为excel
     *
     * @param fileName
     * @return
     */
    public static Boolean isExcelFile(String fileName) {
        String[] img_type = new String[]{".xlsx", ".xls"};
        // endsWith() 方法用于测试字符串是否以指定的后缀结束。
        fileName = fileName.toLowerCase();
        for (String type : img_type) {
            if (fileName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断Excel是2003还是2007, Workbook对应的是HSSFWorkbook还是XSSFWorkbook
     * @param fileName 文件名(必须带后缀)
     * @return
     */
    public static Workbook ExcelConversion(String fileName){
        Workbook workbook = null;
        if (ExcelUtil.isExcel2003(fileName)) {
            log.info("【*****excel转换2003*****】");
            workbook = new HSSFWorkbook();
        }else if (ExcelUtil.isExcel2007(fileName)){
            log.info("【*****excel转换2007*****】");
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }

    /**
     *生成excel文件 在此工作目录下生成excel表格
     * @param filename 文件名
     * @param workbook excel中的内容
     * @throws Exception
     */
    public static void buildExcelFile(String filename,Workbook workbook) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        //fos.flush();
        fos.close();
    }

    /**
     * 浏览器下载excel 在浏览器下下载生成excel
     *
     * @param filename 文件名
     * @param workbook excel中的内容
     * @param response 设置HTTP状态码和管理Cookie。
     * @throws Exception
     */
    public static void buildExcelDocument(String filename, Workbook workbook, HttpServletResponse response)throws Exception {
        try {
            //告诉浏览器输出内容为流
            response.setContentType("application/vnd.ms-excel");
        /*Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类
        型是按照文件的扩展名显示的，点保存后，
        文件以filename的值命名，保存类型以Content中设置的为准.并将编码设置为UTF-8*/
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
            //新建输出流
            OutputStream outputStream = response.getOutputStream();
            //将内容写入文件中
            workbook.write(outputStream);
            //刷新缓存
            outputStream.flush();
            //关闭输出流
            outputStream.close();
        }catch (Exception e){
            log.error("【导出错误】={}",e);
        }

    }

    /**
     * 类型转换
     */
    public static String getValue(Cell cell) {
        String value = null;
        switch (cell.getCellType()){
            case NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    //注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd HH:mm:ss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    value=sdf.format(HSSFDateUtil.getJavaDate(cell.
                            getNumericCellValue())).toString();
                    break;
                } else {
                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                }
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue() + "";
                break;
            case FORMULA: // 公式
                value = cell.getCellFormula() + "";
                break;
            case BLANK: // 空值
                value = "";
                break;
            case ERROR: // 故障
                value = "非法字符";
                break;
            default:
                value = "未知类型";
                break;
        }
        return value;
    }

    //设置格式
    public static CellStyle cellStyle(Workbook workbook) {
        //设置为居中加粗
        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("m/d/yy h:mm"));
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

}
