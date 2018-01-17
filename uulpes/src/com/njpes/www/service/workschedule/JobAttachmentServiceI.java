package com.njpes.www.service.workschedule;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.njpes.www.entity.workschedule.JobAttachment;

public interface JobAttachmentServiceI {

    /**
     * 保存附件信息
     * 
     * @param 关联的实体id
     * @param files
     *            文件列表
     * @param webRealPath
     *            存储的真实路径
     * @return
     * @author 赵忠诚
     */
    public List<JobAttachment> saveFile(List<MultipartFile> files, String webRealPath);

    /**
     * 仅仅保存附件，存储附件表在业务之外
     */
    public void saveFilesOnly(List<MultipartFile> files, String webRealPath);

    /**
     * 删除附件信息
     * 
     * @param 关联的实体id
     * @param savePath
     *            文件保存路径
     * @return 是否删除
     * @author 赵忠诚
     */
    public boolean delFiles(String savePath);

    public int delrecord(Long id);

    public int delrecord(String uuid);

    /**
     * 插入附件信息记录表
     * 
     * @param record
     *            附件信息
     * @return int
     * @author 赵忠诚
     */
    public int insert(JobAttachment record);

    public int update(JobAttachment record);

    public JobAttachment selectByUUid(String uuid);
}
