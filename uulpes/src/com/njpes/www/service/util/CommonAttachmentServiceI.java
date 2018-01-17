package com.njpes.www.service.util;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.njpes.www.entity.util.CommonAttachment;

public interface CommonAttachmentServiceI {

    CommonAttachment selectByPrimaryKey(long id);

    int saveCommonAttachment(CommonAttachment commonAttachment);

    int updateAttachment(CommonAttachment commonAttachment);

    int delCommonAttachment(CommonAttachment commonAttachment);

    /**
     * @Description:
     * @param request
     * @param response
     * @return
     * @author 张超
     */
    Map<String, Object> saveFile(List<MultipartFile> files, String webRealPath);

    /**
     * 仅仅上传附件不保存数据库，主要应用于编辑器图片上传
     * 
     * @param files
     *            长传文件
     * @param webRealPath
     *            保存路径
     * @return 返回信息
     * @author 赵忠诚
     */
    String saveFileOnly(MultipartFile[] files);

}
