package com.gtafe.data.center.system.log.service;

import java.util.List;

import com.gtafe.data.center.system.log.vo.EmailSendLog;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.data.center.system.log.vo.LogVo;

public interface LogService {

    void saveLog(LogInfo logInfo);

    void saveEmailSendLog(EmailSendLog emailSendLog);

    void saveErrorLog(int moduleId, String operType, String operContent);

    void saveLogNoLogin(LogInfo logInfo);

    List<LogVo> queryListLogin(int pageNum, int pageSize, String startTime, String endTime, int operRes, String orgIds, String keyWord, String operModuleIds);
    List<LogVo> queryListOper(int pageNum, int pageSize, String startTime, String endTime, int operRes, String orgIds, String keyWord, String operModuleIds);

    List<EmailSendLog> queryEmailSendLogList(int pageNum, int pageSize, String startTime, String endTime, String module, String keyWord);
}
