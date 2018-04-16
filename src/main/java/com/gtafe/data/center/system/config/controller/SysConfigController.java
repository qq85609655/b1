package com.gtafe.data.center.system.config.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(path = "/config")
@Api(value = "SysConfigController")
@CrossOrigin
public class SysConfigController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysConfigController.class);

    @Resource
    private SysConfigService sysConfigServiceImpl;

    @RequestMapping("queryEntity")
    public SysConfigVo queryEntity() {
       // return this.sysConfigServiceImpl.getCacheSysConfigVO();
        return this.sysConfigServiceImpl.getBasicSysConfigVO();
      //  return this.sysConfigServiceImpl.getSysConfigVO();
    }




    @RequestMapping(path = "/updateEmail", method = RequestMethod.POST)
    public boolean updateEmail(@RequestBody SysConfigVo vo) throws Exception {
        boolean result =  this.sysConfigServiceImpl.updateEmail(vo);
        sysConfigServiceImpl.flushSystemInfo(false);
        return result;
    }





    @RequestMapping(path = "/updateCenterDb", method = RequestMethod.POST)
    public boolean updateCenterDb(@RequestBody SysConfigVo vo) throws Exception {
        boolean result =  this.sysConfigServiceImpl.updateCenterDb(vo);
        sysConfigServiceImpl.flushSystemInfo(false);
        return result;
    }



    @RequestMapping(path = "/updateSys", method = RequestMethod.POST)
    public boolean updateSys(@RequestBody SysConfigVo vo) throws Exception {
        boolean result = sysConfigServiceImpl.updateSys(vo);
        sysConfigServiceImpl.flushSystemInfo(false);
        return result;
    }

    @RequestMapping(path = "/updatelogo", method = RequestMethod.POST)
    public boolean updatelogo(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        boolean result = sysConfigServiceImpl.updateSystemLogo(file);
        sysConfigServiceImpl.flushSystemInfo(true);
        return result;
    }


    @RequestMapping(path = "/initCenterDataBase", method = RequestMethod.POST)
    public boolean initCenterDataBase() throws Exception {
         boolean result = sysConfigServiceImpl.initCenterDataBase();
        LOGGER.info("初始化结束!!!!!!");
        if (result) {
            int sfinit = 1;
            SysConfigVo sysConfigVo =this.sysConfigServiceImpl.getCacheSysConfigVO();
            sysConfigVo.setSfInit(sfinit);
            this.sysConfigServiceImpl.updateSys(sysConfigVo);
        } 

       // boolean result = sysConfigServiceImpl.addPrimaryKeys();

        return result;
    }


}
