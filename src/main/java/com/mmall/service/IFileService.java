package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author fudada
 * @date 2019/5/20 - 16:53
 */
public interface IFileService {

    String upload(MultipartFile multipartFile, String path);
}
