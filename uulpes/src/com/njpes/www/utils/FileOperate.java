package com.njpes.www.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class FileOperate {
    private String message;
    private static long file_max_size = 3000000;// 3m
    public static String typeStr = "typeStr";

    public boolean isFailfile(long file_size) {

        if (file_size > file_max_size) {
            return true;
        }
        return false;

    }

    public FileOperate() {
    }

    /**
     * 读取文本文件内容
     * 
     * @param filePathAndName
     *            带有完整绝对路径的文件名
     * @param encoding
     *            文本文件打开的编码方式
     * @return 返回文本文件的内容
     */

    public static final String baseDir = "/UserFiles/";

    public String readTxt(String filePathAndName, String encoding) throws IOException {
        encoding = encoding.trim();
        StringBuffer str = new StringBuffer("");
        String st = "";
        try {
            FileInputStream fs = new FileInputStream(filePathAndName);
            InputStreamReader isr;
            if (encoding.equals("")) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encoding);
            }
            BufferedReader br = new BufferedReader(isr);
            try {
                String data = "";
                while ((data = br.readLine()) != null) {
                    str.append(data + " ");
                }
            } catch (Exception e) {
                str.append(e.toString());
            }
            st = str.toString();
        } catch (IOException es) {
            st = "";
        }
        return st;
    }

    /**
     * 新建目录
     * 
     * @param folderPath
     *            目录
     * @return 返回目录创建后的路径
     */
    public String createFolder(String folderPath) {
        String txt = folderPath;
        try {
            java.io.File myFilePath = new java.io.File(txt);
            txt = folderPath;
            if (!myFilePath.exists()) {
                myFilePath.mkdir();
            }
        } catch (Exception e) {
            message = "创建目录操作出错";
        }
        return txt;
    }

    /**
     * 多级目录创建
     * 
     * @param folderPath
     *            准备要在本级目录下创建新目录的目录路径 例如 c:myf
     * @param paths
     *            无限级目录参数，各级目录以单数线区分 例如 a|b|c
     * @return 返回创建文件后的路径 例如 c:myfa c
     */
    public String createFolders(String folderPath, String paths) {
        String txts = folderPath;
        try {
            String txt;
            txts = folderPath;
            StringTokenizer st = new StringTokenizer(paths, "/");
            for (int i = 0; st.hasMoreTokens(); i++) {
                txt = st.nextToken().trim();
                if (txts.lastIndexOf("/") != -1) {
                    txts = createFolder(txts + txt);
                } else {
                    txts = createFolder(txts + txt + "/");
                }
            }
        } catch (Exception e) {
            message = "创建目录操作出错！";
        }
        return txts;
    }

    public String createFolders(String path) {

        File file = new File(path);
        if (!file.isDirectory()) {
            file.mkdirs();
        }

        return path;
    }

    /**
     * 新建文件
     * 
     * @param filePathAndName
     *            文本文件完整绝对路径及文件名
     * @param fileContent
     *            文本文件内容
     * @return
     */
    public void createFile(String filePathAndName, String fileContent) {

        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            FileWriter resultFile = new FileWriter(myFilePath);
            PrintWriter myFile = new PrintWriter(resultFile);
            String strContent = fileContent;
            myFile.println(strContent);
            myFile.close();
            resultFile.close();
        } catch (Exception e) {
            message = "创建文件操作出错";
        }
    }

    /**
     * 有编码方式的文件创建
     * 
     * @param filePathAndName
     *            文本文件完整绝对路径及文件名
     * @param fileContent
     *            文本文件内容
     * @param encoding
     *            编码方式 例如 GBK 或者 UTF-8
     * @return
     */
    public void createFile(String filePathAndName, String fileContent, String encoding) {

        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }

            PrintWriter myFile = new PrintWriter(myFilePath, encoding);
            String strContent = fileContent;
            myFile.println(strContent);
            myFile.close();
        } catch (Exception e) {
            message = "创建文件操作出错";
        }
    }

    public static void saveFile(String saveFilePath, String newFileName, MultipartFile filedata) {
        // TODO Auto-generated method stub
        // 根据配置文件获取服务器图片存放路径
        String picDir = "";
        /* 构建文件目录 */
        File fileDir = new File(saveFilePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            FileOutputStream out = new FileOutputStream(saveFilePath + "\\" + newFileName);
            // 写入文件
            out.write(filedata.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除文件
     * 
     * @param filePathAndName
     *            文本文件完整绝对路径及文件名
     * @return Boolean 成功删除返回true遭遇异常返回false
     */
    public boolean delFile(String filePathAndName) {
        boolean bea = false;
        try {
            String filePath = filePathAndName;
            System.out.println("aaa" + filePath);
            File myDelFile = new File(filePath);
            if (myDelFile.exists()) {
                myDelFile.delete();
                bea = true;
                // System.out.println("删除成功"+filePath);
            } else {
                bea = false;
                message = (filePathAndName + "删除文件操作出错");
            }
        } catch (Exception e) {
            message = e.toString();
        }
        return bea;
    }

    /**
     * 删除文件夹
     * 
     * @param folderPath
     *            文件夹完整绝对路径
     * @return
     */
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            message = ("删除文件夹操作出错");
        }
    }

    /**
     * 删除指定文件夹下所有文件
     * 
     * @param path
     *            文件夹完整绝对路径
     * @return
     * @return
     */
    public boolean delAllFile(String path) {
        boolean bea = false;
        File file = new File(path);
        if (!file.exists()) {
            return bea;
        }
        if (!file.isDirectory()) {
            return bea;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            }

            else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                bea = true;
            }
        }
        return bea;
    }

    /**
     * 复制单个文件
     * 
     * @param oldPathFile
     *            准备复制的文件源
     * @param newPathFile
     *            拷贝到新绝对路径带文件名
     * @return
     */
    public void copyFile(String oldPathFile, String newPathFile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPathFile); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            message = ("复制单个文件操作出错");
        }
    }

    /**
     * 复制整个文件夹的内容
     * 
     * @param oldPath
     *            准备拷贝的目录
     * @param newPath
     *            指定绝对路径的新目录
     * @return
     */
    public void copyFolder(String oldPath, String newPath) {
        try {
            new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            message = "复制整个文件夹内容操作出错";
        }
    }

    public static List findFile(File file, List fileList, String exp) {
        if (fileList == null)
            fileList = new ArrayList();
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().toLowerCase().equals(exp)) {
                System.out.println(files[i].getName());
                fileList.add(files[i]);
            }
            if (files[i].isDirectory()) {

                findFile(files[i], fileList, exp);
            }

        }
        return fileList;

    }

    /**
     * 移动文件
     * 
     * @param oldPath
     * @param newPath
     * @return
     */
    public void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    /**
     * 移动目录
     * 
     * @param oldPath
     * @param newPath
     * @return
     */
    public void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }

    public String getMessage() {
        return this.message;
    }

    public String getTimeNameString(String name) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssms");
        String timestr = format.format(new Date());
        return timestr + name;

    }

    /****************/
    public static int countFileNum(String path, String exp, FilenameFilter filter) {
        int f_num = 0;
        try {
            File file = new File(path);
            File[] files = null;

            List<File> fileList = new ArrayList<File>();
            List objList = findFile(file, fileList, exp);
            for (File f : fileList) {
                int num = 0;
                num = f.listFiles(filter).length;
                f_num += num;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return f_num;

    }

    public static long getFile_max_size() {

        return file_max_size;
    }

    /**
     * 设置下载文件中文件的名称
     * 
     * @param filename
     * @param request
     * @return
     */
    public static String encodeFilename(String filename, HttpServletRequest request) {
        /**
         * 获取客户端浏览器和操作系统信息 firefox：Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0)
         * Gecko/20100101 Firefox/43.0 IE: Mozilla/5.0 (Windows NT 6.1; WOW64;
         * Trident/7.0; rv:11.0) like Gecko Chrome: Mozilla/5.0 (Windows NT 6.1;
         * WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69
         * Safari/537.36 360: Mozilla/5.0 (Windows NT 6.1; WOW64)
         * AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69
         * Safari/537.36
         */
        String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (-1 != agent.indexOf("Chrome"))) {
                return MimeUtility.encodeText(filename, "UTF-8", "B");
            }
            if ((agent != null) && (-1 != agent.indexOf("Firefox")))
                return MimeUtility.encodeText(filename, "UTF-8", "B");

            if ((agent != null) && (-1 != agent.indexOf("Gecko"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            return filename;
        } catch (Exception ex) {
            return filename;
        }
    }

    public static String encodeFilename1(String filename, HttpServletRequest request) {
        /**
         * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE
         * 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
         * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1;
         * zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
         */
        String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (-1 != agent.indexOf("Gecko"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (-1 != agent.indexOf("Chrome"))) {
                return MimeUtility.encodeText(filename, "UTF-8", "B");
            }
            if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
                return MimeUtility.encodeText(filename, "UTF-8", "B");

            return filename;
        } catch (Exception ex) {
            return filename;
        }
    }
}
