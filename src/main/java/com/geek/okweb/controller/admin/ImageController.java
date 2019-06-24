package com.geek.okweb.controller.admin;

import com.alibaba.fastjson.JSON;
import com.geek.okweb.domain.Category;
import com.geek.okweb.domain.Image;
import com.geek.okweb.domain.Product;
import com.geek.okweb.service.CategoryService;
import com.geek.okweb.service.ImageService;
import com.geek.okweb.service.ProductService;
import com.geek.okweb.utils.FileUtil;
import com.geek.okweb.utils.KeyUtil;
import com.geek.okweb.utils.MyPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    /**
     * 多文件上传
     * @param files 文件
     * @return
     */
    @ResponseBody
    @PostMapping("/save")
    public String save(@RequestParam("file")MultipartFile[] files){
        log.info("【图片上传数量】={}", files.length);
        if (files != null && files.length >= 1) {
            for (int i = 0; i < files.length; i++) {
                String resourceFilename = files[i].getOriginalFilename(); //获取文件名： xxx.jpg 带文件后缀
                String fileName = FileUtil.getFileName(resourceFilename); //获取不带后缀的文件名
                log.info("【图片名字】={}",fileName);
                String path = FileUtil.getPath()+"/upload/";  //上传路径
                String randomFileName = FileUtil.getRandomFileName(resourceFilename); //获取带uuid的文件名字
                try {
                    FileUtil.uploadFile(files[i].getBytes(),path,randomFileName);
                }catch (Exception e){
                    log.error("【上传失败】={}",e);
                }
                String imageUrl = "/imagePath/"+randomFileName;  //图片路径
                Image image = new Image();
                image.setImageName(fileName); //设置图片名字
                image.setId(KeyUtil.UUID());//设置图片id
                image.setImageUrl(imageUrl);//设置图片路径

                imageService.save(image); //保存
            }
            return "success";
        }else {
            return "fail";
        }

    }

    /**
     * 查询所有图片
     * @param model
     * @return
     */
    @GetMapping("/findAll")
    public String findAll(Model model,
                          @RequestParam(name = "page",required = false,defaultValue = "1")Integer page,@RequestParam(name = "pageSize",required = false,defaultValue = "10") Integer pageSize){
        MyPage<Image> imagePage = imageService.findAllByStatus(0,page,pageSize);
        Category category = categoryService.findOne();
        model.addAttribute("category", category);
        model.addAttribute("imagePage",imagePage);
        return "admin/new_web/image_admin";
    }

    /**
     * 删除图片到回收站
     * @param id
     * @return
     */
    @GetMapping("/delete")
    public String delete(@RequestParam("id")String id){
        Image image = imageService.findById(id);
        image.setStatus(1);
        imageService.update(image);
        return  "redirect:/image/findAll";
    }

    /**
     * 批量删除图片到回收站
     * @param id
     * @return
     */
    @GetMapping("/batchDelete")
    @ResponseBody
    public String delete(@RequestParam("id")List<String> id){
        id.stream().forEach(x -> {
            Image image = imageService.findById(x);
            image.setStatus(1);
            imageService.update(image);
        });
        return  "success";
    }


    /**
     * 添加图片分类
     * @param mids
     * @param cids
     * @return
     */
    @ResponseBody
    @PostMapping("/addImageCategory")
        public String addImageCategory(@RequestParam(name = "imageIds")String mids, @RequestParam(name = "cateids")String cids){
        List<String> imageIds = JSON.parseArray(mids, String.class);
        List<String> cateids = JSON.parseArray(cids, String.class);
        log.info("【imageIds】={}",imageIds);
        log.info("【cateids】={}",cateids);
        log.info("【进入】={}",cateids);
        imageIds.stream().forEach(x -> {Image image = imageService.findById(x);image.setCateids(cateids);imageService.update(image);});
        return "success";
    }

    @PostMapping("/editName")
    public String editName(String id,String name){
        Image image = imageService.findById(id);
        image.setImageName(name);
        imageService.update(image);
        return "redirect:/image/findAll";
    }

    @GetMapping("/categoryImage")
    public String categoryImage(@RequestParam("id")String id,@RequestParam(name = "page",defaultValue = "1",required = false)Integer page,
                                @RequestParam(name = "pageSize",defaultValue = "10",required = false)Integer pageSize,Model model){
        Set<String> ids = new HashSet<>();
        ids.add(id);
        MyPage<Image> imagePage = imageService.findImageByCategroys(ids, page, pageSize);
        Category category = categoryService.findOne();
        model.addAttribute("imagePage", imagePage);
        model.addAttribute("id", id);
        model.addAttribute("category", category);
        return "admin/new_web/image_admin";
    }


    @PostMapping("/editDescription")
    public String editDescription(@RequestParam String id,String description){
        Image image = imageService.findById(id);
        image.setDescription(description);
        imageService.update(image);
        return "redirect:/image/findAll";
    }

    @PostMapping("/editHref")
    public String editHref(@RequestParam String id, String hrefUrl) {
        Image image = imageService.findById(id);
        image.setHrefUrl(hrefUrl);
        imageService.update(image);
        return "redirect:/image/findAll";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                         @RequestParam(value = "keyword", required = false) String keyword, @RequestParam(value = "url", required = false) String url,
                         @RequestParam("id") String id,@RequestParam(name = "cateids",required = false)Set<String> cateid,
                         Model model){
        Product product = productService.findById(id);
        MyPage<Image> imgPage = imageService.search(page, pageSize, keyword);
        log.info("【查询】 = {}", cateid);
        Category category = categoryService.findOne(); //分类
        model.addAttribute("category",category);
        model.addAttribute("imgPage", imgPage);
        model.addAttribute("product", product);
        model.addAttribute("keyword", keyword);
        model.addAttribute("url", url);

        return "admin/new_web/" + url;
    }

    @GetMapping("/cate")
    public String searchByCate(@RequestParam(value = "page", defaultValue = "1", required = false) int page,
                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                         @RequestParam(value = "url", required = false) String url,
                         @RequestParam("id") String id,@RequestParam(name = "cateids",required = false)Set<String> cateids,
                         Model model){
        Product product = productService.findById(id);
        MyPage<Image> imgPage = imageService.findImageByCategroys(cateids, page, pageSize); //图片
        Category category = categoryService.findOne(); //分类
        model.addAttribute("category",category);
        model.addAttribute("imgPage", imgPage);
        model.addAttribute("product", product);
        model.addAttribute("url", url);
        model.addAttribute("cateids", cateids.iterator().next());

        return "admin/new_web/" + url;
    }


}
