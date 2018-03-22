package com.gtafe.data.center.system.config.service;

import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import org.springframework.web.multipart.MultipartFile;

import com.gtafe.data.center.system.config.vo.SysConfigVo;

/**
 * 系統參數設置
 */
public interface SysConfigService {

    SysConfigVo getCacheSysConfigVO();

    /**
     * 普通用户可以查看的系统信息，可直接返回给前端的方法
     *
     * @author 汪逢建
     * @date 2017年11月9日
     */
    SysConfigVo getBasicSysConfigVO();

    boolean updateEmail(SysConfigVo vo);

    boolean updateSys(SysConfigVo vo);

    boolean updateSystemLogo(MultipartFile file) throws Exception;

    /**
     * 刷新系统缓存信息
     *
     * @param flushImage 是否刷新图片信息
     * @author 汪逢建
     * @date 2017年11月9日
     */
    boolean flushSystemInfo(boolean flushImage) throws Exception;

    boolean initCenterDataBase();

    SysConfigVo getSysConfigVO();

    boolean addPrimaryKeys();

    void saveTransFile(TransFileVo transFileVo);

    void deleteAllFilesInfo(String fileType);
}
