package com.gtafe.data.center.dataetl.datajob.service.impl;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datajob.mapper.DataJobMapper;
import com.gtafe.data.center.dataetl.datajob.service.DataJobService;
import com.gtafe.data.center.dataetl.datajob.vo.JobEntity;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.ParamInvalidException;
import com.gtafe.framework.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataJobServiceImpl extends BaseController implements DataJobService {

    @Autowired
    private DataJobMapper dataJobMapper;

    @Autowired
    private LogService logServiceImpl;


    @Override
    public List<JobEntity> queryList(Integer busType, String orgIds, Integer status, String jobName, int pageNum, int pageSize) {
        List<Integer> orgIdList = StringUtil.splitListInt(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, JobEntity.class);
        }
        return this.dataJobMapper.queryList(busType, orgIdList, status, jobName, pageNum, pageSize);
    }

    @Override
    public JobEntity getEntityDetail(int jobId, boolean b) {
        return null;
    }

    @Override
    public void insertDataJob(int businessType, JobEntity taskVO) {

    }

    @Override
    public void updateDataJob(int businessType, JobEntity jobEntity) {

    }

    @Override
    public boolean batchUpdateState(String jobIds, int state) {
        if (state < 0 || state > 1 || StringUtils.isEmpty(jobIds)) {
            throw new ParamInvalidException();
        }
        List<Integer> jobList = new ArrayList<Integer>();
        String[] ids = jobIds.split(",");
        for (String s : ids) {
            jobList.add(Integer.parseInt(s));
        }
        List<JobEntity> list = this.dataJobMapper.getJobVos(jobList);
        if (list.isEmpty()) {
            return true;
        }
        jobList.clear();
        List<String> nameList = new ArrayList<String>();
        for (JobEntity vo : list) {
            jobList.add(vo.getJobId());
            nameList.add(vo.getJobName());
        }
        int businessType = list.get(0).getBusinessType();
        this.dataJobMapper.updateStatus(jobList, state, this.getUserId());

        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getMoudleId(businessType));
        logInfo.setOperType("修改");
        logInfo.setOperContent((state == 1 ? "启动" : "停止") + "" + this.getName(businessType) + "资源：" + StringUtil.join(nameList, ","));
        this.logServiceImpl.saveLog(logInfo);
        //日志
        return true;
    }


    private int getMoudleId(int businessType) {
        if (businessType == 1) {
            return LogConstant.Module_Data_issue;
        } else {
            return LogConstant.Module_Data_subscribe;
        }
    }
    private String getName(int businessType) {
        if (businessType == 1) {
            return "发布";
        } else {
            return "订阅";
        }
    }
    @Override
    public boolean deleteJobs(List<Integer> integers) {
        return false;
    }
}
