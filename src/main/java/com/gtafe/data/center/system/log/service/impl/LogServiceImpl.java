package com.gtafe.data.center.system.log.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.gtafe.data.center.system.log.vo.EmailSendLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.log.mapper.LogMapper;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.data.center.system.log.vo.LogVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.utils.IpUtil;
import com.gtafe.framework.base.utils.StringUtil;

@Service
public class LogServiceImpl extends BaseController implements LogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogServiceImpl.class);
    @Resource
    private LogMapper logMapper;

    private void saveEntity(LogVo logVo) {
        this.logMapper.saveEntity(logVo);
    }

    @Override
    public void saveLog(LogInfo logInfo) {
        LogVo logVo = this.getLoginLog(logInfo.getModuleId(), logInfo.getOperType(), logInfo.getOperContent(), 1, false);
        this.saveEntity(logVo);
    }

    @Override
    public void saveEmailSendLog(EmailSendLog emailSendLog) {
        this.logMapper.saveEmailSendLog(emailSendLog);
    }

    @Override
    public void saveErrorLog(int moduleId, String operType, String operContent) {
        LogVo logVo = this.getLoginLog(moduleId, operType, operContent, 2, false);
        this.saveEntity(logVo);
    }

    @Override
    public void saveLogNoLogin(LogInfo logInfo) {
        LogVo logVo = this.getLoginLog(logInfo.getModuleId(), logInfo.getOperType(), logInfo.getOperContent(), 1, true);
        this.saveEntity(logVo);
    }

    @Override
    public List<LogVo> queryListLogin(int pageNum, int pageSize, String startTime, String endTime, int operRes, String orgIds, String keyWord, String operModuleIds) {
        List<Integer> orgIdList = new ArrayList<Integer>();
        orgIdList = StringUtil.splitListInt(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, LogVo.class);
        }
        return logMapper.queryList4Login(pageNum, pageSize, startTime, endTime, operRes, orgIdList, keyWord);
    }
    
    @Override
    public List<LogVo> queryListOper(int pageNum, int pageSize, String startTime, String endTime, int operRes, String orgIds, String keyWord, String operModuleIds) {
        List<Integer> operModuleIdList = new ArrayList<Integer>();
        operModuleIdList = StringUtil.splitListInt(operModuleIds);
        if (operModuleIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, LogVo.class);
        }
        return logMapper.queryList4Oper(pageNum, pageSize, startTime, endTime, operRes, operModuleIdList, keyWord);
    }

    @Override
    public List<EmailSendLog> queryEmailSendLogList(int pageNum, int pageSize, String startTime, String endTime, String module, String keyWord) {
        return logMapper.queryList4EmailSend(pageNum, pageSize, startTime, endTime, module, keyWord);
    }


    /**
     * 组织日志信息
     *
     * @param moduleId    模块id产地id
     * @param operType    操作类型
     * @param operContent 操作内容
     * @param operRes     正常 异常
     * @return
     */
    private LogVo getLoginLog(int moduleId, String operType, String operContent, int operRes, boolean noLogin) {
        LogVo logVo = new LogVo();
        logVo.setOperIp(IpUtil.getIpAddr(getRequest()));
        logVo.setOperModuleId(moduleId);
        String moduleName = "";
        if (moduleId == 0) {
            moduleName = "用户登录";
        } else {
            moduleName = "系统模块";
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(moduleName);
        }
        logVo.setOperModuleName(moduleName);
        logVo.setOperContent(operContent);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logVo.setOperTime(sdf.format(new Date()));
        logVo.setOperType(operType);
        if (!noLogin) {
            UserLoginInfo userVo = this.getUserInfo();
            logVo.setOperOrgId(userVo.getOrgId());
            logVo.setOperOrgName(userVo.getOrgName());
            logVo.setOperUserNo(userVo.getUserNo() + "");
            logVo.setOperUserName(userVo.getRealName());
        } else {
            logVo.setOperOrgId(0);
            logVo.setOperOrgName("");
            logVo.setOperUserNo("admin");
            logVo.setOperUserName("admin");
        }
        logVo.setOperRes(operRes);
        return logVo;
    }
}
