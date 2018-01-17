package com.njpes.www.action.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.njpes.www.action.BaseController;
import com.njpes.www.entity.util.CommonAttachment;
import com.njpes.www.service.util.CommonAttachmentServiceI;

/**
 * @Description: 文件附件
 * @author zhangchao
 * @Date 2015-5-18 上午9:30:03
 */
@Controller
@RequestMapping(value = "/util/commonAttachment")
public class CommonAttachmentController extends BaseController {
    public static final String UPLOAD_FILE_DIR = "file";
    public static final String SLASH = "/";

    @Autowired
    private CommonAttachmentServiceI commonAttachmentService;

    /*
     * 保存附件
     */
    @RequestMapping(value = { "/save" })
    @ResponseBody
    public String save(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest mul = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = mul.getFiles("file");
        String path = request.getRealPath("/");
        Map<String, Object> map = commonAttachmentService.saveFile(files, path);
        String json = JSON.toJSONString(map);
        return json;
    }

    @RequestMapping(value = { "/onlySave" })
    @ResponseBody
    public String onluSave(@RequestParam(value = "upload", required = false) MultipartFile[] upload) {
        return commonAttachmentService.saveFileOnly(upload);
    }

    /*
     * 删除附件
     */
    @RequestMapping(value = { "/delete" })
    public String delete(CommonAttachment commonAttachment, HttpServletRequest request) {
        int str = commonAttachmentService.delCommonAttachment(commonAttachment);
        return "success";
    }

    /*
     * 下载附件
     */
    @RequestMapping(value = { "/downloadFile" })
    @ResponseBody
    public void downloadFile(@RequestParam("id") long id, HttpServletRequest request, HttpServletResponse response) {

        if (id > 0) {
            try {
                CommonAttachment commonAttachment = commonAttachmentService.selectByPrimaryKey(id);
                File file = null;
                if (commonAttachment != null) {
                    // path是指欲下载的文件的路径。
                    file = new File(request.getRealPath("/") + UPLOAD_FILE_DIR + SLASH + commonAttachment.getSaveName()
                            + commonAttachment.getMineType());
                }
                InputStream fis = null;
                // 图片不存在换成默认图片
                if (commonAttachment == null || !file.exists() && (commonAttachment.getMineType().equals(".png")
                        || commonAttachment.getMineType().equals(".jpg"))) {
                    file = new File(request.getRealPath("/") + "themes/theme1/images/nopictrue.jpg");
                    fis = new BufferedInputStream(
                            new FileInputStream(request.getRealPath("/") + "themes/theme1/images/nopictrue.jpg"));
                } else {
                    fis = new BufferedInputStream(new FileInputStream(request.getRealPath("/") + UPLOAD_FILE_DIR + SLASH
                            + commonAttachment.getSaveName() + commonAttachment.getMineType()));
                }
                // 以流的形式下载文件。
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                String fileName = URLEncoder.encode(commonAttachment != null ? commonAttachment.getName() : "文件没找到",
                        "UTF-8");
                /*
                 * see http://support.microsoft.com/default.aspx?kbid=816868
                 */
                if (fileName.length() > 150) {
                    String guessCharset = "gb2312"; /*
                                                     * 根据request的locale
                                                     * 得出可能的编码，中文操作系统通常是
                                                     */
                    fileName = new String(commonAttachment.getName().getBytes(guessCharset), "ISO8859-1");
                }
                response.setHeader("Content-Disposition", "commonAttachment; filename=" + fileName);

                response.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
