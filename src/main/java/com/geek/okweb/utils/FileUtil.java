package com.geek.okweb.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.DecimalFormat;

@Slf4j
public class FileUtil {

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static void fileExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getPath() {
        String path = System.getProperty("user.dir");
//        path = path + "/www";
        return path;
    }

/*    public static String getPath() {
        File directory = new File("");// 参数为空
        String path = null;
        try {
            path = directory.getCanonicalPath();
            path = path + "/www";
            log.info("【classpath路径】={}", path);
        } catch (IOException e) {
            log.info("【FileUtils Error】= {}", e);
        }
        return path;
    }*/


    /**
     * 获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getTrimFileName(String filePath) {
        File tempFile = new File(filePath.trim());
        String fileName = tempFile.getName();
        return fileName;
    }

    /**
     * 获取文件hash值
     *
     * @param filePath
     * @return
     */
    public static String getFileHashCode(String filePath) {
        String hashCode = SHAUtil.SHAHashCode(filePath);
        return hashCode;
    }

    /**
     * 获取文件大小
     *
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            return 0;
        }
    }

    /**
     * byte转化为KB、MB、GB
     *
     * @param size
     * @return
     */
    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    /**
     * 创建文件目录
     *
     * @param destDirName
     * @return
     */
    public static File createDir(String destDirName) {
        File dir = new File(destDirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 文件复制
     *
     * @param sourcePath
     * @param destPath
     */
    public static void copyFile(String sourcePath, String destPath) {
        InputStream fis = null;
        OutputStream fos = null;
        try {
            File source = FileUtil.createFile(sourcePath);
            int flag = 0;
            if (sourcePath.contains(File.separator)) {
                flag = sourcePath.lastIndexOf(File.separator);
            }
            String txts = sourcePath.substring(flag);
            File target = FileUtil.createFile(destPath + File.separator + txts);
            fis = new FileInputStream(source);
            fos = new FileOutputStream(target);
            byte[] buf = new byte[4096];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.flush();
                    fos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件按指定编码复制
     *
     * @param inputFile
     * @param inputCharset
     * @param outputFile
     * @param outputCharset
     * @throws Exception
     */
    public static void copyFile(String inputFile, String inputCharset,
                                String outputFile, String outputCharset) throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream(new File(
                inputFile)), inputCharset);
        Writer writer = new OutputStreamWriter(new FileOutputStream(new File(
                outputFile)), outputCharset);
        int temp = 0;
        try {
            while ((temp = reader.read()) != -1) {
                writer.write(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建文件
     *
     * @param filePath
     */
    public static File createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                // 先得到文件的上级目录，并创建上级目录，在创建文件
                file.getParentFile().mkdir();
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return String
     */
    public static String getFileSufix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }

    /**
     * 获取文件名
     *
     * @param fileName
     * @return String
     */
    public static String getFileRealName(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex);
    }

    public static String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    public static String getFileName(String resource) {
        return resource.substring(0, resource.lastIndexOf("."));
    }

    public static String getRandomFileName(String fileName) {
        return KeyUtil.UUID() + fileName;
    }

}
