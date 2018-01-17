package com.njpes.www.action.homepage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.njpes.www.utils.FTPUtil;  

/**
 * 文件上传和接受的接口--上传到北京服务器。
 * @author ludus-pm
 *
 */
@Controller
@RequestMapping(value = "/upload")
public class UploadDataFile {
	//private static final String FAR_SERVICE_DIR = "http://123.56.239.224:8089/pes/upload/receive";// 远程服务器接受文件的路由
	private static final String FAR_SERVICE_DIR = "http://192.168.0.138:8089/pes";
    private static final long yourMaxRequestSize = 10000000;  
    //接收文件使用
    private static final String STORE_FILE_DIR="/WEB-INF/uploadwork";//文件保存的路径
  
    @RequestMapping("/index")  
    public String index(Model model) throws IOException {  
  
        return "upload/uploadfile";  
    }  
  
    @RequestMapping(value="/uploaddata") 
    @ResponseBody
    public String uploaddata(HttpServletRequest request) throws Exception { 
    	MultipartHttpServletRequest httpServletRequest = (MultipartHttpServletRequest)request;
    	List<MultipartFile> files = httpServletRequest.getFiles("file");
    	MultipartFile multipartFile = files.get(0);
    	System.out.println(multipartFile.getName());
    	Date date = new Date();

    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	String imageupdatename = sdf.format(date);
    	/*String uuid = UUID.randomUUID().toString().replaceAll("-", "");*/
    	String filename = multipartFile.getOriginalFilename();
    	System.out.println(multipartFile.getOriginalFilename());
    	InputStream inputStream = multipartFile.getInputStream();
    	String split=filename.substring(filename.lastIndexOf(".")+1);
    	if (split.equals("xlsx") || split.equals("xls") || split.equals("txt")) {
    		//上传至ftp服务器
    		//上传后的名称为日期+uuid+原本名称
    		boolean success = FTPUtil.uploadFile("123.56.239.224", 21, "test", "Ludusadmin123", "", imageupdatename+filename, inputStream);
    		if (success) {
    			return "1";
    		}
		}
		return "0";
    }  
  
    private void uploadToFarServiceByHttpClient(HashMap<String, FileItem> files) {  
        HttpClient httpclient = new DefaultHttpClient();  
        //httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        try {  
            HttpPost httppost = new HttpPost(FAR_SERVICE_DIR);  
            MultipartEntity reqEntity = new MultipartEntity();  
            Iterator iter = files.entrySet().iterator();  
            while (iter.hasNext()) {  
                Map.Entry entry = (Map.Entry) iter.next();  
                String key = (String) entry.getKey();  
                FileItem val = (FileItem) entry.getValue();  
                FileBody filebdody = new FileBody(inputstreamtofile(  
                        val.getInputStream(), key));  
                reqEntity.addPart(key, filebdody);  
            }
            httppost.setEntity(reqEntity);  
  
            HttpResponse response = httpclient.execute(httppost);  
  
            int statusCode = response.getStatusLine().getStatusCode();  
  
            if (statusCode == HttpStatus.SC_OK) {  
                System.out.println("服务器正常响应.....");  
                HttpEntity resEntity = response.getEntity();  
                System.out.println(EntityUtils.toString(resEntity,"UTF-8"));// httpclient自带的工具类读取返回数据  
                EntityUtils.consume(resEntity);  
            }  else {
            	//服务器连接失败
            	System.out.println("服务器响应失败,错误码为:"+statusCode);  
            }
  
        } catch (ParseException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } finally {  
            try {  
                httpclient.getConnectionManager().shutdown();  
            } catch (Exception ignore) {  
  
            }  
        }  
  
    }  
  
    public File inputstreamtofile(InputStream ins, String filename)  
            throws IOException {  
        File file = new File(filename);  
        OutputStream os = new FileOutputStream(file);  
        int bytesRead = 0;  
        byte[] buffer = new byte[8192];  
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {  
            os.write(buffer, 0, bytesRead);  
        }  
        os.close();  
        ins.close();  
        return file;  
    } 
    
    @RequestMapping(value="/receive", method = RequestMethod.POST)
    public String receive(HttpServletRequest request,HttpServletResponse response) throws Exception {    
        // 判断enctype属性是否为multipart/form-data    
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);    
        if (!isMultipart)    
            throw new IllegalArgumentException(    
                    "上传内容不是有效的multipart/form-data类型.");    
    
        // Create a factory for disk-based file items    
        DiskFileItemFactory factory = new DiskFileItemFactory();    
    
        // Create a new file upload handler    
        ServletFileUpload upload = new ServletFileUpload(factory);    
    
        // Parse the request    
        List<?> items = upload.parseRequest(request);    
    
        Iterator iter = items.iterator();    
        while (iter.hasNext()) {    
            FileItem item = (FileItem) iter.next();    
            if (item.isFormField()) {    
                // 如果是普通表单字段    
                String name = item.getFieldName();    
                String value = item.getString();    
                // ...    
            } else {    
                // 如果是文件字段    
                String fieldName = item.getFieldName();    
                String fileName = item.getName();    
                String contentType = item.getContentType();    
                boolean isInMemory = item.isInMemory();    
                long sizeInBytes = item.getSize();    
                    
                String filePath=STORE_FILE_DIR+fileName;    
                //写入文件到当前服务器磁盘    
                File uploadedFile = new File(filePath);    
                // File uploadedFile = new File("D:\haha.txt");    
                item.write(uploadedFile);    
            }    
        }    
        response.setCharacterEncoding("UTF-8");      
         response.getWriter().println("上传成功");    
        return "1";    
    }    
}
