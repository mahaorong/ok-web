package com.geek.okweb.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DatabaseBackupUtil {

    //Linux 备份数据库文件存储的路径
    private static final String BASE_PATH_LINUX = "/www/enroo/database/";

    /**
     * Linux环境下备份数据库
     */
    public static void backupForLinux() {
        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
            String date = dateFormat.format(new Date());

            String sqlname = BASE_PATH_LINUX + date + "enroo.sql";

            String mysql = "/usr/bin/mysqldump -uenroo -pcHhhAh3usQru enroo > " + sqlname;

            Runtime.getRuntime().exec(new String[] { "sh", "-c", mysql });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
