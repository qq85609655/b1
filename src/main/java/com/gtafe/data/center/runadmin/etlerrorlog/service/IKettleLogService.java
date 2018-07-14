package com.gtafe.data.center.runadmin.etlerrorlog.service;

import com.gtafe.data.center.runadmin.etlerrorlog.vo.ErrorLogVo;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;

import java.util.List;

public interface IKettleLogService {

    /**
     * @param pageNum
     * @param pageSize
     * @param orgIds
     * @param transName
     * @return
     * @history
     * @author gang.zhou
     * @since JDK 1.7
     */
    List<KettleLogVO> list(int businessType, String startTime, String endTime, int pageNum, int pageSize, String transName, String orgIds);

    /**
     * @param pageNum
     * @param pageSize
     * @param channel_id
     * @return
     */
    List<ErrorLogVo> queryErrorList(int pageNum, int pageSize, String channel_id);

    boolean clearLogs();
    boolean clearLogsDetail();
}
