package com.gtafe.data.center.runadmin.etlerrorlog.service.impl;


import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.runadmin.etlerrorlog.mapper.KettleLogMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.service.IKettleLogService;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.ErrorLogVo;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;
import com.gtafe.framework.base.service.BaseService;
import com.gtafe.framework.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KettleLogServiceImpl extends BaseService implements IKettleLogService {

    @Autowired
    private KettleLogMapper kettleLogMapper;

    @Override
    public List<KettleLogVO> list(int businessType, String startTime, String endTime, int pageNum, int pageSize, String transName, String orgIds) {
        List<Integer> orgIdList = StringUtil.splitListInt(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, KettleLogVO.class);
        }
        return this.kettleLogMapper.list(businessType, startTime, endTime, pageNum, pageSize, transName, orgIdList);
    }

    @Override
    public List<ErrorLogVo> queryErrorList(int pageNum, int pageSize, String channel_id) {
        return this.kettleLogMapper.queryErrorList(pageNum, pageSize, channel_id);
    }

    @Override
    public boolean clearLogs() {
        return this.kettleLogMapper.clearLogs();
    }

    @Override
    public boolean clearLogsDetail() {
        return this.kettleLogMapper.clearLogsDetail();
    }

}
