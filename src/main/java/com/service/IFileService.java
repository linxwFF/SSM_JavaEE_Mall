package com.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by LINxwFF on 2017/11/21.
 */

public interface IFileService {

    public String upload(MultipartFile file,String path);
}
