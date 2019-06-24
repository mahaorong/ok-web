package com.geek.okweb.controller.admin;

import com.alibaba.fastjson.JSON;
import com.geek.okweb.domain.FileUpload;
import com.geek.okweb.domain.Worktable;
import com.geek.okweb.service.FileService;
import com.geek.okweb.utils.FileUtil;
import com.geek.okweb.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * Create by Gai on 2018/10/30 14:33
 */
@Controller
@Slf4j
@RequestMapping("/file")
public class FileUploadController {


    @Autowired
    private FileService fileService;



    /**
     * 多文件上传
     *
     * @param request
     * @return
     */
    @PostMapping("/multiUpload")
    public String multiUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        if (!files.isEmpty()) {
            String filePath = FileUtil.getPath() + "/fileupload/";
            //判断文件是否存在
            FileUtil.fileExist(filePath);
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file.isEmpty()) {
                    return "上传第" + (i++) + "个文件失败";
                }
                String fileName = file.getOriginalFilename();
                log.info("fileName========{}", fileName);
                String name = file.getName();
                log.info("name============{}", name);
                long size = file.getSize();
                //得到文件后缀名
                String fileSufix = FileUtil.getFileSufix(fileName);
                log.info("fileSufix==================={}", fileSufix);
                //得到文件真实名字
                String realName = FileUtil.getFileRealName(fileName);
                String uuid = KeyUtil.UUID();
                String name1 = uuid + "." + fileSufix;
                log.info("size============{}", size);
                File dest = new File(filePath + name1);
                log.info("dest============{}", dest);
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                try {
                    file.transferTo(dest);
                    log.info("第" + (i + 1) + "个文件上传成功");
                } catch (IOException e) {
                    log.error(e.toString(), e);
                    return "上传第" + (i++) + "个文件失败";
                }
                FileUpload fileUpload = new FileUpload();
                fileUpload.setId(uuid);
                fileUpload.setSize(size);
                fileUpload.setExtname(fileSufix);
                fileUpload.setFileName(fileName);
                fileUpload.setName(realName);
                fileUpload.setStatus(0);
                log.info("filesName=============={}", fileName);
                fileService.saveFile(fileUpload);

            }
        }
        return "redirect:/admin/findFile";

    }

    /**
     * 下载文件
     *
     * @param fileName
     * @param response
     */
    @GetMapping("/fileDownload")
    public void downloadFileByOutputStream(String fileUrl,String fileName, HttpServletResponse response)throws  IOException {
        String filepath = FileUtil.getPath() + "/fileupload/";
        //判断文件是否存在
        FileUtil.fileExist(filepath);
        // 1.获取要下载的文件的绝对路径
        File file = new File(fileUrl);
        log.info("filepath============{}", filepath);
        log.info("fileName==================={}", fileName);
        // 3.设置content-disposition响应头控制浏览器以下载的形式打开文件
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        log.info("file=================={}", file);
        // 4.根据文件路径获取要下载的文件输入流
        try {
            InputStream in = new FileInputStream(file);
            int len = 0;
            // 5.创建数据缓冲区
            byte[] buffer = new byte[1024];
            // 6.通过response对象获取OutputStream流
            OutputStream out = response.getOutputStream();
            // 7.将FileInputStream流写入到buffer缓冲区
            while ((len = in.read(buffer)) > 0) {
                // 8.使用OutputStream将缓冲区的数据输出到客户端浏览器
                out.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 添加文件分类
     * @param
     * @param
     * @return
     */
    @ResponseBody
    @PostMapping("/addFileCategory")
    public String addFileCategory(@RequestParam(name = "fileIds")String mids, @RequestParam(name = "cateids")String cids){
        List<String> fileIds = JSON.parseArray(mids, String.class);
        List<String> cateids = JSON.parseArray(cids, String.class);
        log.info("【fileIds】={}",fileIds);
        log.info("【cateids】={}",cateids);
        log.info("【进入】={}",cateids);
        fileIds.stream().forEach(x -> { FileUpload fileUpload = fileService.findById(x);fileUpload.setCateids(cateids);fileService.update(fileUpload);});
        return "redirect:/admin/findFile";
    }


    @GetMapping("/batchDeleteFile")
    @ResponseBody
    public String batchDeleteFile(@RequestParam("fileIds")List<String> fileIds){
        log.info("文件id={}", fileIds);
       fileIds.stream().forEach((x) -> fileService.delete(x));
       return "success";
    }

}


