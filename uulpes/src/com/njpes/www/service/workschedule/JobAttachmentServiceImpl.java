package com.njpes.www.service.workschedule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.njpes.www.constant.Constants;
import com.njpes.www.dao.workschedule.JobAttachmentMapper;
import com.njpes.www.entity.workschedule.JobAttachment;

import heracles.web.config.ApplicationConfiguration;

@Service("jobAttachmentService")
public class JobAttachmentServiceImpl implements JobAttachmentServiceI {
    @Autowired
    private JobAttachmentMapper jobAttachmentMapper;

    @Override
    public List<JobAttachment> saveFile(List<MultipartFile> files, String webRealPath) {
        if (files == null)
            return null;
        List<JobAttachment> list = new ArrayList<JobAttachment>();
        for (MultipartFile f : files) {
            // 取得当前上传文件的文件名称
            String fileName = f.getOriginalFilename();

            JobAttachment attachment = new JobAttachment();
            attachment.setName(fileName);
            String saveName = UUID.randomUUID().toString();
            attachment.setUuid(saveName);
            attachment.setMineType(fileName.substring(fileName.lastIndexOf(".")));
            attachment.setSavePath(
                    Constants.READ_FILE_SERVER_URL + "/" + webRealPath + "/" + saveName + attachment.getMineType());
            // 定义上传路径
            // String path = Constants.UPLOAD_SERVER_PATH + "/" + webRealPath +
            // "/" + saveName + attachment.getMineType();
            String path = ApplicationConfiguration.getInstance().getWorkDir() + "/" + webRealPath + "/" + saveName
                    + attachment.getMineType();

            File localFile = new File(path);
            attachment.setLocationpath(path);
            // 文件夹不存在创建
            if (!localFile.exists() && !localFile.isDirectory()) {
                localFile.mkdirs();
            }
            try {
                f.transferTo(localFile);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            insert(attachment);
            list.add(attachment);
        }
        return list;
    }

    @Override
    public int insert(JobAttachment record) {
        return jobAttachmentMapper.insertSelective(record);
    }

    @Override
    public boolean delFiles(String savePath) {
        File f = new File(savePath);// 定义文件路径
        if (!f.exists())
            return false;
        if (f.isDirectory()) {
            try {
                FileUtils.deleteDirectory(f);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return f.delete();
        }
    }

    @Override
    public void saveFilesOnly(List<MultipartFile> files, String webRealPath) {

    }

    @Override
    public int delrecord(Long id) {
        JobAttachment entity = jobAttachmentMapper.selectByPrimaryKey(id);
        if (entity.getLocationpath() != null)
            this.delFiles(entity.getLocationpath());
        return jobAttachmentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delrecord(String uuid) {
        JobAttachment entity = jobAttachmentMapper.selectIdByUUid(uuid);
        if (entity.getLocationpath() != null)
            this.delFiles(entity.getLocationpath());
        return jobAttachmentMapper.deleteByUUid(uuid);
    }

    @Override
    public JobAttachment selectByUUid(String uuid) {
        return jobAttachmentMapper.selectIdByUUid(uuid);
    }

    @Override
    public int update(JobAttachment record) {
        return jobAttachmentMapper.updateByPrimaryKeySelective(record);
    }

}
