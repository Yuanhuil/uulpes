package com.njpes.www.service.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.util.CommonAttachmentMapper;
import com.njpes.www.entity.util.CommonAttachment;

@Service("commonAttachmentService")
public class CommonAttachmentServiceImpl implements CommonAttachmentServiceI {

    public static final String UPLOAD_FILE_DIR = "file";
    public static final String SLASH = "/";
    @Autowired
    private CommonAttachmentMapper commonAttachmentMapper;

    @Override
    public CommonAttachment selectByPrimaryKey(long id) {
        // TODO Auto-generated method stub
        return commonAttachmentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int saveCommonAttachment(CommonAttachment commonAttachment) {
        // TODO Auto-generated method stub
        return commonAttachmentMapper.insertSelective(commonAttachment);
    }

    @Override
    public int updateAttachment(CommonAttachment commonAttachment) {
        // TODO Auto-generated method stub
        return commonAttachmentMapper.updateByPrimaryKey(commonAttachment);
    }

    @Override
    public int delCommonAttachment(CommonAttachment commonAttachment) {
        // TODO Auto-generated method stub
        commonAttachment = this.selectByPrimaryKey(commonAttachment.getId());
        if (commonAttachment != null) {
            String path = this.getClass().getClassLoader().getResource("/").getPath();
            path = path.substring(1, path.indexOf("WEB-INF")) + UPLOAD_FILE_DIR + SLASH + commonAttachment.getSaveName()
                    + commonAttachment.getMineType();
            deleteDirectory(path);
            return commonAttachmentMapper.deleteByPrimaryKey(commonAttachment.getId());
        }
        return 0;

    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     * 
     * @param sPath
     *            要删除的目录或文件
     */
    public void deleteDirectory(String sPath) {
        File f = new File(sPath);
        if (f.exists()) {
            if (f.isDirectory()) {
                File[] fs = f.listFiles();
                if (fs.length > 0) {
                    for (File file : fs) {
                        deleteDirectory(file.getAbsolutePath());
                    }
                }
            }
            f.delete();
        }
    }

    /**
     * 将原有模式改为springmvc的方式，提高数据存储效率
     */
    @Override
    public Map<String, Object> saveFile(List<MultipartFile> files, String webRealPath) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Long> fileidList = new ArrayList<Long>();
        String fileids = "";
        String message = "success";
        try {
            for (MultipartFile f : files) {
                // 取得当前上传文件的文件名称
                String fileName = f.getOriginalFilename();

                CommonAttachment commonAttachment = new CommonAttachment();
                commonAttachment.setName(fileName);
                String saveName = UUID.randomUUID().toString();
                commonAttachment.setSaveName(saveName);
                commonAttachment.setMineType(fileName.substring(fileName.lastIndexOf(".")));
                commonAttachment.setSavePath(UPLOAD_FILE_DIR);
                // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                if (!fileName.trim().equals("")) {
                    // 重命名上传后的文件名
                    fileName = UUID.randomUUID().toString();
                    // 定义上传路径
                    String path = webRealPath + UPLOAD_FILE_DIR + SLASH + saveName + commonAttachment.getMineType();
                    File localFile = new File(path);
                    // 文件夹不存在创建
                    if (!localFile.exists() && !localFile.isDirectory()) {
                        localFile.mkdirs();
                    }
                    f.transferTo(localFile);
                }

                saveCommonAttachment(commonAttachment);
                fileids += commonAttachment.getId() + ",";
                fileidList.add(commonAttachment.getId());
            }
        } catch (Exception e) {
            message = "文件传输异常";
            e.printStackTrace();
        }
        map.put("message", message);// 异常输出结果
        map.put("fileids", fileids);// 返回id拼接的字符串“，”间隔
        map.put("fileidList", fileidList);// 返回id的list

        return map;
    }

    @Override
    public String saveFileOnly(MultipartFile[] files) {
        String message = "上传成功";
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        try {
            for (MultipartFile f : files) {
                // 取得当前上传文件的文件名称
                String fileName = f.getOriginalFilename();
                String saveName = UUID.randomUUID().toString();
                String mineType = fileName.substring(fileName.lastIndexOf("."));
                // 如果名称不为“”,说明该文件存在，否则说明该文件不存在
                if (!fileName.trim().equals("")) {
                    // 重命名上传后的文件名
                    fileName = UUID.randomUUID().toString();
                    // 定义上传路径
                    String path = Constants.UPLOAD_SERVER_PATH + SLASH + Constants.UPLOAD_FILE_PATH + SLASH + year
                            + SLASH + month + SLASH + day + SLASH + saveName + mineType;
                    File localFile = new File(path);
                    // 文件夹不存在创建
                    if (!localFile.exists() && !localFile.isDirectory()) {
                        localFile.mkdirs();
                    }
                    f.transferTo(localFile);
                }
            }
        } catch (Exception e) {
            message = "文件传输异常";
            e.printStackTrace();
        }
        return message;
    }

}
