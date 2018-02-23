/**
 * Project Name: gtacore
 * File Name:	<#%modlue%#>ServiceImpl.java
 * Description: This is writen by tools
 * Date: 		2017-08-14 17:52:20
 * Author: 		Xiang Zhiling
 * History:
 * Copyright (c) 2017, GTA All Rights Reserved.
 */

package com.gtafe.data.center.dataetl.datatask.service.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleSource;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleTarget;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.SourceTargetVo;
import com.gtafe.data.center.dataetl.schedule.EtlSchedule;
import com.gtafe.data.center.dataetl.trans.EtlTrans;
import com.gtafe.data.center.information.data.service.DataStandardService;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.exception.ParamInvalidException;
import com.gtafe.framework.base.utils.StringUtil;


@Service
public class DataTaskServiceImpl extends BaseController implements DataTaskService {
    @Autowired
    private DataTaskMapper dataTaskMapper;
    @Autowired
    private LogService logServiceImpl;
    @Resource
    private DataStandardService dataStandardServiceImpl;

    /**
     * @param orgIds
     * @param dataTypeValue   数据类型
     * @param status          业务类型
     * @param sourceTableName 来源表名
     * @param pageNum
     * @param pageSize
     * @param businessType
     * @return
     */
    public List<DataTaskVo> queryList(int collectionId, String orgIds, Integer dataTypeValue,
                                      Integer status, String sourceTableName,
                                      int pageNum, int pageSize,
                                      Integer businessType) {
        List<Integer> orgIdList = StringUtil.splitListInt(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, DataTaskVo.class);
        }
        return this.dataTaskMapper.queryList(collectionId, orgIdList, status,
                sourceTableName, pageNum, pageSize, businessType);
    }

    @Override
    public DataTaskVo getDataTaskVo(Integer taskId, boolean stepsFlag) {
        DataTaskVo taskVo = dataTaskMapper.getDataTaskVo(taskId);
        if (taskVo == null) {
            throw new OrdinaryException("数据资源不存在会已被删除！");
        }
        taskVo.setSteps(this.dataTaskMapper.getTaskSteps(taskId));
        if (taskVo.getSteps() == null || taskVo.getSteps().size() < 2) {
            throw new OrdinaryException("数据资源任务数据格式错误！");
        }
        return taskVo;
    }

    @Override
    public int insertDataTaskVo(int businessType, DataTaskVo taskVo) {
        this.revisionDataTaskVo(businessType, taskVo);
        if (this.dataTaskMapper.checkTaskNameRepeat(null,
                taskVo.getTaskName(), taskVo.getOrgId(), businessType) > 0) {
            throw new OrdinaryException(this.getName(businessType)+"资源名称已存在");
        }
        int userId = this.getUserId();
        //插入数据
        this.dataTaskMapper.insertDataTask(taskVo, userId);
        int taskId = taskVo.getTaskId();
        //插入步骤
        this.dataTaskMapper.insertTaskSteps(taskId, taskVo.getSteps(), userId);
        //保存日志
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getMoudleId(businessType));
        logInfo.setOperType("新增");
        logInfo.setOperContent("新增"+this.getName(businessType)+"资源：" + taskVo.getTaskName()+(taskVo.isRunStatus()?"，并启动":"，未启动"));
        this.logServiceImpl.saveLog(logInfo);
        if (taskVo.isRunStatus()) {
            //启动任务
        }
        return taskId;
    }

    @Override
    public boolean updateDataTaskVo(int businessType, DataTaskVo taskVo) {
        DataTaskVo dbVo = this.dataTaskMapper.getDataTaskVo(taskVo.getTaskId());
        if (dbVo == null) {
            throw new OrdinaryException(this.getName(businessType)+"资源不存在会已被删除！");
        }
        this.revisionDataTaskVo(businessType, taskVo);
        if (this.dataTaskMapper.checkTaskNameRepeat(dbVo.getTaskId(),
                taskVo.getTaskName(), taskVo.getOrgId(), businessType) > 0) {
            throw new OrdinaryException(this.getName(businessType)+"资源名称已存在");
        }
        int userId = this.getUserId();
        int taskId = taskVo.getTaskId();
        //插入数据
        this.dataTaskMapper.updateDataTask(taskVo, userId);
        
        if(taskVo.isRunStatus()) {
            //立即启动是，需要启动任务，否则不更新状态
            this.dataTaskMapper.updateDataTaskStatus(Arrays.asList(new Integer[] {taskVo.getTaskId()}), 1 , userId);
        }
        //插入步骤
        this.dataTaskMapper.deleteTaskSteps(taskId);
        this.dataTaskMapper.insertTaskSteps(taskId, taskVo.getSteps(), userId);
        //保存日志
        String content = dbVo.getTaskName();
        if (!dbVo.getTaskName().equals(taskVo.getTaskName())) {
            content += " -> " + taskVo.getTaskName();
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getMoudleId(businessType));
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改"+this.getName(businessType)+"资源：" + content);
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean batchUpdateState(String taskIds, int state) {
        if(state<0 || state>1 || StringUtils.isEmpty(taskIds)) {
            throw new ParamInvalidException();
        }
        List<Integer> taskIdList = new ArrayList<Integer>();
        String[] taskid_ = taskIds.split(",");
        for (String s : taskid_) {
            taskIdList.add(Integer.parseInt(s));
        }
        List<DataTaskVo> list = this.dataTaskMapper.getDataTaskVos(taskIdList);
        if(list.isEmpty()) {
            return true;
        }
        taskIdList.clear();
        List<String> nameList = new ArrayList<String>();
        for (DataTaskVo vo : list) {
            taskIdList.add(vo.getTaskId());
            nameList.add(vo.getTaskName());
        }
        int businessType = list.get(0).getBusinessType();
        this.dataTaskMapper.updateDataTaskStatus(taskIdList, state, this.getUserId());
        
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getMoudleId(businessType));
        logInfo.setOperType("修改");
        logInfo.setOperContent((state==1?"启动":"停止")+""+this.getName(businessType)+"资源：" + StringUtil.join(nameList, ","));
        this.logServiceImpl.saveLog(logInfo);
        //日志
        return true;
    }

    @Override
    public boolean deleteTasks(List<Integer> taskIds) {
        List<DataTaskVo> list = this.dataTaskMapper.getDataTaskVos(taskIds);
        if(list.isEmpty()) {
            return true;
        }
        List<String> nameList = new ArrayList<String>();
        for (DataTaskVo vo : list) {
            if(vo.isRunStatus()) {
                nameList.add(vo.getTaskName());
            }
        }
        int businessType = list.get(0).getBusinessType();
        if(!nameList.isEmpty()) {
            throw new OrdinaryException("部分"+this.getName(businessType)+"资源["+StringUtil.join(nameList, ",")+"]已启动，操作失败！") ;
        }
        nameList.clear();
        for (DataTaskVo vo : list) {
            int taskId = vo.getTaskId();
            this.dataTaskMapper.deleteTaskSteps(taskId);
            this.dataTaskMapper.deleteTaskById(taskId);
            nameList.add(vo.getTaskName());
        }
        
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getMoudleId(businessType));
        logInfo.setOperType("修改");
        logInfo.setOperContent("删除"+this.getName(businessType)+"资源：" + StringUtil.join(nameList, ","));
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    private boolean revisionDataTaskVo(int businessType, DataTaskVo taskVo) {
        //中心库连接ID
        //List<DatasourceVO> dvos = this.datasourceMapper.queryCenterData();
        if (taskVo.getSteps() == null || taskVo.getSteps().size() < 2) {
            throw new ParamInvalidException();
        }
        JSONObject source = JSONObject.parseObject(taskVo.getSteps().get(0));
        JSONObject target = JSONObject.parseObject(taskVo.getSteps().get(taskVo.getSteps().size() - 1));
        if (source.getIntValue("type") != 1 || target.getIntValue("type") != 2) {
            throw new ParamInvalidException();
        }
        ConvertRuleSource sourceObj = JSONObject.toJavaObject(source, ConvertRuleSource.class);
        ConvertRuleTarget targetObj = JSONObject.toJavaObject(target, ConvertRuleTarget.class);
        SourceTargetVo third = sourceObj.getData();
        SourceTargetVo center = targetObj.getData();
        if (businessType != 1) {
            third = targetObj.getData();
            center = sourceObj.getData();
        }
        taskVo.setBusinessType(businessType);
        taskVo.setCenterTablename(center.getCenterTablename());
        taskVo.setSubsetCode(center.getSubsetCode());
        taskVo.setClassCode(center.getClassCode());
        taskVo.setSubclassCode(center.getSubclassCode());
        taskVo.setThirdConnectionId(third.getThirdConnectionId());
        taskVo.setThirdTablename(third.getThirdTablename());

        DataStandardVo subclassVO = dataStandardServiceImpl.getDataStandardVo(taskVo.getSubclassCode());
        if (subclassVO == null) {
            throw new OrdinaryException("数据子类不存在或已被删除，请重新选择！");
        }
        //计算 表达式
        //taskVo.setRunTime(new Date());
        this.calculateExpression(taskVo);
        return true;
    }

    private void calculateExpression(DataTaskVo taskVo) {
        String expression = "";
        if (taskVo.getRunType() == 1) {
            expression += "0";
            expression += " 0/" + taskVo.getRunSpace();
            expression += " * * * ? *";
        } else if (taskVo.getRunType() == 2) {
            expression += "0 0";
            expression += " 0/" + taskVo.getRunSpace();
            expression += " * * ? *";
        } else if (taskVo.getRunType() >= 2) {
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(taskVo.getRunTime()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            expression += "0 ";
            expression += cal.get(Calendar.MINUTE);
            expression += " ";
            expression += cal.get(Calendar.HOUR_OF_DAY);
            if (taskVo.getRunType() == 3) {
                //按天
                expression += " * * ? *";
            } else if (taskVo.getRunType() == 4) {
                //按周
                expression += " ? * ";
                expression += taskVo.getRunSpaces();
                expression += " *";
            } else if (taskVo.getRunType() == 5) {
                //按月
                expression += " ";
                expression += taskVo.getRunSpaces();
                expression += " * ? *";
            } else if (taskVo.getRunType() == 6) {
                //按年
                expression += " ";
                expression += cal.get(Calendar.DAY_OF_MONTH);
                expression += " ";
                expression += taskVo.getRunSpaces();
                expression += " ? *";
            } else {
                //默认按天计算
                expression += " * * ? *";
            }
        }
        taskVo.setRunEexpression(expression);
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
    public boolean startNow(int taskId) {
        DataTaskVo vo = this.dataTaskMapper.getDataTaskVo(taskId);
        if(vo == null) {
            throw new OrdinaryException("数据资源不存在，会已被删除!");
        }
        try {
            EtlSchedule.taskIdQueue.offer(taskId);
        } catch (Exception e) {
            throw new OrdinaryException("立即执行已经完成，但是有异常存在，请去日志列表查看!");
        }
        //保存日志
        int businessType = vo.getBusinessType();
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getMoudleId(businessType));
        logInfo.setOperType("修改");
        logInfo.setOperContent("手动同步"+this.getName(businessType)+"资源：" + vo.getTaskName());
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }
}
