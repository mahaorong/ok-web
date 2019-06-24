package com.geek.okweb.controller.admin;

import com.geek.okweb.domain.Product;
import com.geek.okweb.domain.ProductData;
import com.geek.okweb.domain.Worktable;
import com.geek.okweb.enums.ExcelEnum;
import com.geek.okweb.exception.ExcelException;
import com.geek.okweb.service.ProductService;
import com.geek.okweb.service.WorktableService;
import com.geek.okweb.utils.ExcelUtil;
import com.geek.okweb.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 批量导入
 */
@Controller
@Slf4j
@RequestMapping(value = "/excel")
public class InputExcelController {
    @Autowired
    private ProductService productService;

    @Autowired
    private WorktableService worktableService;

    @PostMapping("/uploadProduct")
    public String upload(@RequestParam(name = "language") String language , HttpServletRequest request) {
        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            if (!files.isEmpty()) {
                for(MultipartFile file : files){
                    //判断文件是否为空
                    if (file == null) {
                        log.info("文件为空");
                        throw new ExcelException(ExcelEnum.EXCEL_NOT_FOUND);
                    }
                    //判断文件是否为指定格式
                    if (ExcelUtil.isExcelFile(file.getOriginalFilename())) {
                    } else {
                        log.info("文件格式错误");
                        throw new ExcelException(ExcelEnum.EXCEL_INCORRECT_FORMAT);
                    }
                    log.info("【开始上传产品】，文件名为={}", file.getOriginalFilename());
                    //获取上传Excel文件名
                    String fileName = file.getOriginalFilename();

                    //根据fileName查询数据库中是否存在该Excel
                    Worktable worktable = worktableService.findByFileName(fileName);

                    //初始化状态
                    Integer status = 0;
                    if (null != worktable && StringUtils.equals(fileName,worktable.getFileName())){ //如果文件名相同则进行修改操作
                        //修改更新时间
                        if (worktable.getStatus() == 1){//判断状态是否为回收状态
                            status = worktable.getStatus();
                            worktable = new Worktable();
                            worktable.setFileName(fileName);
                            worktable.setCreateTime(new Date());
                            worktable.setUpdateTime(new Date());
                            worktable.setId(KeyUtil.UUID());
                        }
                    }else { //文件名不相同，创建新的工作簿对象
                        worktable = new Worktable();
                        worktable.setFileName(fileName);
                        worktable.setCreateTime(new Date());
                        worktable.setUpdateTime(new Date());
                        worktable.setId(KeyUtil.UUID());
                    }
                    worktableService.merge(worktable);

                    //初始化一个Workbook操作对象
                    Workbook wb = null;
                    //判断excel是那个版本号，创建对应的输入流
                    if (ExcelUtil.isExcel2007(file.getOriginalFilename())) {
                        wb = new XSSFWorkbook(file.getInputStream());
                    } else if (ExcelUtil.isExcel2003(file.getOriginalFilename())) {
                        /*new POIFSFileSystem(file.getInputStream())*/
                        wb = new HSSFWorkbook(file.getInputStream());
                    } else {
                        log.info("文件格式错误");
                        throw new ExcelException(ExcelEnum.EXCEL_INCORRECT_FORMAT);
                    }
                    //查看有多少张工作表
                    int sheets = wb.getNumberOfSheets();
                    String nwid = worktable.getId();//存放新的worktable表的id
                    String owid = null;//存放旧的worktable表的id
                    //遍历工作表
                    if (sheets != 0){
                        for (int i = 0; i < sheets; i++) {
                            //读取一张工作表
                            Sheet sheet = wb.getSheetAt(i);
                            if (sheet != null){
                                //获取一共有多少行
                                int rows = sheet.getPhysicalNumberOfRows();
                                //获取第一行(即:标题)
                                Row row1 = sheet.getRow(0);
                                int ofCells = 0;
                                if (row1 != null) {
                                    //根据第一行获取一共有多少列
                                    ofCells = row1.getPhysicalNumberOfCells();
                                    //log.info("ofCells=={}",ofCells);
                                }
                                if (rows != 0){
                                    //遍历每一行，注意：第 0 行为标题
                                    for (int j = 1; j < rows; j++) {
                                        //初始化Product数据
                                        Product product = null;
                                        //初始化List<ProductData>
                                        List<ProductData> list = new ArrayList<>();
                                        String value = null;//存放没个单元格的值
                                        //获得第 j 行
                                        Row row = sheet.getRow(j);
                                        if (row != null && ofCells != 0){
                                            //遍历每一列
                                            for (int k = 0 ; k < ofCells ; k ++){
                                                //初始化ProductData
                                                ProductData productData = new ProductData();
                                                //初始化titleValue(存标题：即第一行)
                                                String titleValue = null;
                                                //获取每一个单元格
                                                log.info("k=={}",k);
                                                Cell cell = row.getCell(k);
                                                //判断该单元格是否为空
                                                if (null == cell) {//如果为空,则将值设置为空字符
                                                } else {//如果不为空,则调用ExcelUtil中的getValue获取对应的值
                                                    value = ExcelUtil.getValue(cell);
                                                }
                                                log.info("value=={}", value);
                                                //判断第一列中是否有id
                                                if (k == 0 && StringUtils.isNotBlank(value)){//如果第一列有值
                                                    //查找出来，将其赋值给初始化的Product
                                                    product = productService.findById(value);
                                                    if (product == null || status == 1){
                                                        product = new Product();
                                                        product.setId(KeyUtil.UUID());
                                                    }
                                                    if (product !=  null && StringUtils.isNotBlank(product.getWid()) && j == 1 && k == 0){
                                                        if (StringUtils.equals(nwid,product.getWid())){
                                                            owid = null;
                                                        }else {
                                                            owid = product.getWid();
                                                        }
                                                    }
                                                }else if (k == 0 && StringUtils.isBlank(value)){//如果第一列无值
                                                    product = new Product();
                                                    product.setId(KeyUtil.UUID());
                                                }
                                                if (ofCells >= 1){
                                                    if (k == 1){//如果当前列为第一列,则将名字设值进去
                                                        product.setName(value);
                                                    }else if (k>1){//如果当前列大于第二列,则将单元格里的数据存放到json当中
                                                        titleValue = row1.getCell(k).getStringCellValue();
                                                        if (!titleValue.trim().isEmpty()) {
                                                            productData.setId(KeyUtil.UUID());
                                                            productData.setProauctName(titleValue);
                                                            productData.setProductMsg(value);
                                                            list.add(productData);
                                                        }
                                                    }
                                                }
                                            }
                                            product.setJson(list);//设置json值
                                            if (StringUtils.isNotBlank(owid)){//如果owid不为空，则删除旧的工作表
                                                worktableService.delete(owid);
                                                owid = null;
                                            }
                                            product.setWid(worktable.getId());//重新将新的worktable表中的id存到product表中
                                            product.setLanguage(language);
                                            productService.merge(product);//更新或保存
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            log.info("{}", e);
            return "redirect:/error";
        }
        return "redirect:/product/findWorktable";
    }
}
