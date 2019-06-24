package com.geek.okweb.domain;

import com.geek.okweb.utils.FileUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by Gai on 2018/11/20 16:36
 */

@Entity
@Data
public class FileUpload {
      @Id
      private String id;
      //文件大小
      private long size;
      //文件后缀名
      private String extname;
      //原文件名字
      private String name;

      private Integer status = 0;

      @CreationTimestamp
      private Timestamp createTime;
      @Type(type = "json")
      @Column(columnDefinition = "json")
      private List<String> cateids = new ArrayList<String>();
      @Transient
      //id+文件类型
      private String fileName;

      public String getFileName() {
            return id+"."+extname;
      }

      public String getDownloadFileName() {
            return FileUtil.getPath()+"/fileupload/"+getFileName();
      }

      public String formatSize(long size){
            return FileUtil.getNetFileSizeDescription(size);
      }

      public String getRealName(){
            return name + "." + extname;
      };
}
