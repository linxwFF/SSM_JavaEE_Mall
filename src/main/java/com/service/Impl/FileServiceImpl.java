package com.service.Impl;

import com.google.common.collect.Lists;
import com.service.IFileService;
import com.utils.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by LINxwFF on 2017/11/21.
 */
@Service
public class FileServiceImpl implements IFileService {

    //日志监控
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);


    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();

        //扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFile = UUID.randomUUID().toString()+"."+fileExtensionName;

        logger.info("开始上传文件,上传文件名：{},上传路径:{},新文件名:{}",fileName,path,uploadFile);

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);  //设置可写权限
            fileDir.mkdirs();           //创建文件夹
        }

        File targetFile = new File(path,uploadFile);

        try {
            file.transferTo(targetFile);    //复制文件到指定目录
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return null;
        }

        return targetFile.getName();
    }
}
