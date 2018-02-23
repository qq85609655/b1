package com.gtafe.data.center.system.user.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import com.gtafe.data.center.system.user.vo.MessageVo;

public interface SysUserFileService {
    
    public Workbook exportUserInfos();
    
    public MessageVo importUserInfos(MultipartFile file) throws Exception;
}
