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


import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtafe.data.center.dataetl.datatask.vo.*;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleValuemapper;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.TargetMappingVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.ValuemapperVo;
import com.gtafe.data.center.dataetl.schedule.mapper.EtlMapper;
import com.gtafe.data.center.dataetl.trans.Utils;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleSource;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleTarget;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.SourceTargetVo;
import com.gtafe.data.center.dataetl.schedule.EtlSchedule;
import com.gtafe.data.center.information.data.service.DataStandardService;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.exception.ParamInvalidException;


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
        List<String> orgIdList = StringUtil.splitListString(orgIds);
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

    ObjectMapper mapper;

    @Autowired
    EtlMapper etlMapper;

    /**
     * 针对 值映射 情况比较特殊
     * 如果 发布的时候选了 值映射 那么 如果对应中心库的表字段 有对应的代码范围的话 需要根据去检束 是否存在
     *
     * @param businessType
     * @param taskVo
     */
    public void checkValueMapper4TaskVo(int businessType, DataTaskVo taskVo) {
        //循环判断是否存在 值映射的步骤
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<String> valueMappStepStrList = new ArrayList<String>();
        String targetStep = "";
        //循环所有的步骤 找到 值映射的步骤
        for (String stepstr : taskVo.getSteps()) {
            List stepInfo = Utils.getStepInfo(stepstr);
            if (stepInfo == null) {
                throw new OrdinaryException(this.getName(businessType) + " 没有有效的转换步骤！");
            }
            int stepType = (int) stepInfo.get(2);
            String stepName = (String) stepInfo.get(1);
            if (stepType == 7) {//判断是否 有7 的步骤
                valueMappStepStrList.add(stepstr + "######" + stepName);
            }

            if (stepType == 2) {
                targetStep = stepstr;
            }

        }
        //一个值隐射步骤里面 可能会有很多 组 隐射 所以需要循环
        StringBuilder errorMessge = new StringBuilder("");

        //如果有 找到对应的 字段 有没有关联的code类
        //循环所有的 值映射步骤 进行逐个验证
        List<ValuemapperVo> valuemapperVos = new ArrayList<ValuemapperVo>();
        for (int k = 0; k < valueMappStepStrList.size(); k++) {
            String vmstep = valueMappStepStrList.get(k).split("######")[0];
            String stepName = valueMappStepStrList.get(k).split("######")[1];
            try {
                valuemapperVos = mapper.readValue(vmstep, ConvertRuleValuemapper.class).getDataList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int j = 0; j < valuemapperVos.size(); j++) {
                ValuemapperVo valuemapperVo = valuemapperVos.get(j);
                //这个是 配置的值映射的 输出字段， 不管发布或者订阅 都是针对中心库 而言 需要 到 targetStep 里找到对应的 字段
                String souField = "";

                // busType 为1 时
                String targetField = valuemapperVo.getTargetField(); //ww
                // busType 为2 时  需要 反过来
                String sourceField = valuemapperVo.getSourceField();

                if (businessType == 1) {
                    souField = targetField;
                } else {
                    souField = sourceField;
                }
                //根据targetField 和busType 来查询其规则  此时还需要 源数据库 和对应的表
                //这里有需要注意： 在bustype 为1 发布任务时候 需要找到 最后一步 里面 与 值映射字段对应的字段
                String fieldValue = "";
                //从对应的步骤里面找到对应的字段名称
                fieldValue = this.getMapperField(targetStep, souField);

                JSONObject target = JSONObject.parseObject(targetStep);
                ConvertRuleTarget targetObj = JSONObject.toJavaObject(target, ConvertRuleTarget.class);
                //循环 最后一步 目标步骤 来取得 对应关系


              //  System.out.println("对应的字段名称为===========" + fieldValue);
                List<String> rules = etlMapper.queryCodes(taskVo.getCenterTablename(), fieldValue);

                List<String> targetStrs = new ArrayList<String>();
                List<String> sourceStrs = new ArrayList<String>();
                for (int i = 0; i < valuemapperVo.getMappings().size(); i++) {
                    targetStrs.add(valuemapperVo.getMappings().get(i).getTargetValue());
                    sourceStrs.add(valuemapperVo.getMappings().get(i).getSourceValue());
                }
                //然后 再查询 用户输入的 是否合理
                //需要验证 code 是否 与 用户输入 的一致
                //不一致 就抛异常 提示用户检查输入
                String errorMsg = this.checkRuleByParams(rules, sourceStrs, targetStrs, businessType);
                if (StringUtil.isNotBlank(errorMsg.trim())) {
                    errorMessge.append("步骤名称:").append(stepName).append("存在隐射错误!请检查:").append(errorMsg);
                }
            }
        }
        if (errorMessge.length() > 0) {
            throw new OrdinaryException("值隐射异常:" + errorMessge.toString());
        }
    }

    private String getMapperField(String targetStep, String souField) {
        String str = "";


        return str;
    }

    //效验
    private String checkRuleByParams(List<String> rules, List<String> sourceStrs, List<String> targetStrs, int businessType) {
        StringBuilder message_ = new StringBuilder("");
        StringBuilder ss = new StringBuilder();
        if (rules.size() > 0) {
            if (businessType == 1) {
                message_.append("目标值[");
                //发布  比较 目标值 和代码标准的是否一致
                for (String s : targetStrs) {
                    if (!rules.contains(s)) {
                        ss.append(s).append("、");
                    }
                }
            } else {
                message_.append("源值[");
                //订阅 比较 原值  和代码 标准 的 是否一致
                for (String s : sourceStrs) {
                    if (!rules.contains(s)) {
                        ss.append(s).append("、");
                    }
                }
            }
            if (ss.length() > 0) {
                message_.append(ss.toString().substring(0, ss.length() - 1));
                message_.append("对应不上 !");
            } else {
                message_ = new StringBuilder("");
            }
        }
        return message_.toString();
    }


    @Override
    public int insertDataTaskVo(int businessType, DataTaskVo taskVo) {
        this.revisionDataTaskVo(businessType, taskVo);
        // this.checkValueMapper4TaskVo(businessType, taskVo);
        if (this.dataTaskMapper.checkTaskNameRepeat(null,
                taskVo.getTaskName(), taskVo.getOrgId(), businessType) > 0) {
            throw new OrdinaryException(this.getName(businessType) + "资源名称已存在");
        }
        String userId = this.getUserId();
        //插入数据
        this.dataTaskMapper.insertDataTask(taskVo, userId);
        int taskId = taskVo.getTaskId();
        //插入步骤
        this.dataTaskMapper.insertTaskSteps(taskId, taskVo.getSteps(), userId);
        //保存日志
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getMoudleId(businessType));
        logInfo.setOperType("新增");
        logInfo.setOperContent("新增" + this.getName(businessType) + "资源：" + taskVo.getTaskName() + (taskVo.isRunStatus() ? "，并启动" : "，未启动"));
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
            throw new OrdinaryException(this.getName(businessType) + "资源不存在会已被删除！");
        }
        this.revisionDataTaskVo(businessType, taskVo);
        //add by zhougang  2018-4-27 for check valuemapper 数据清洗的一步
        // this.checkValueMapper4TaskVo(businessType, taskVo);
        if (this.dataTaskMapper.checkTaskNameRepeat(dbVo.getTaskId(),
                taskVo.getTaskName(), taskVo.getOrgId(), businessType) > 0) {
            throw new OrdinaryException(this.getName(businessType) + "资源名称已存在");
        }
        String userId = this.getUserId();
        int taskId = taskVo.getTaskId();
        //插入数据
        this.dataTaskMapper.updateDataTask(taskVo, userId);

        if (taskVo.isRunStatus()) {
            //立即启动是，需要启动任务，否则不更新状态
            this.dataTaskMapper.updateDataTaskStatus(Arrays.asList(new Integer[]{taskVo.getTaskId()}), 1, userId);
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
        logInfo.setOperContent("修改" + this.getName(businessType) + "资源：" + content);
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean batchUpdateState(String taskIds, int state) {
        if (state < 0 || state > 1 || StringUtils.isEmpty(taskIds)) {
            throw new ParamInvalidException();
        }
        List<Integer> taskIdList = new ArrayList<Integer>();
        String[] taskid_ = taskIds.split(",");
        for (String s : taskid_) {
            taskIdList.add(Integer.parseInt(s));
        }
        List<DataTaskVo> list = this.dataTaskMapper.getDataTaskVos(taskIdList);
        if (list.isEmpty()) {
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
        logInfo.setOperContent((state == 1 ? "启动" : "停止") + "" + this.getName(businessType) + "资源：" + StringUtil.join(nameList, ","));
        this.logServiceImpl.saveLog(logInfo);
        //日志
        return true;
    }

    @Override
    public boolean deleteTasks(List<Integer> taskIds) {
        List<DataTaskVo> list = this.dataTaskMapper.getDataTaskVos(taskIds);
        if (list.isEmpty()) {
            return true;
        }
        List<String> nameList = new ArrayList<String>();
        for (DataTaskVo vo : list) {
            if (vo.isRunStatus()) {
                nameList.add(vo.getTaskName());
            }
        }
        int businessType = list.get(0).getBusinessType();
        if (!nameList.isEmpty()) {
            throw new OrdinaryException("部分" + this.getName(businessType) + "资源[" + StringUtil.join(nameList, ",") + "]已启动，操作失败！");
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
        logInfo.setOperContent("删除" + this.getName(businessType) + "资源：" + StringUtil.join(nameList, ","));
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    /**
     * 数据处理
     *
     * @param businessType
     * @param taskVo
     * @return
     */
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
        this.calculateExpression(taskVo);


        /**
         * 做针对值映射步骤的 数据代码值的效验工作
         * 思路：
         * 1.检索出当前任务中配置了 几个 值映射的 步骤
         * 2 若有值映射 步骤 ，循环每个步骤  （每个步骤里面 可能有多个输入、输出、映射规则（源和目标值））
         * 3 如果有值映射步骤 ，从最后一步骤里面 找到 所有的映射关系
         * 4 进行匹配效验 抛异常
         */
     /*   List<String> valueMappingSteps = getStepsByType(ConstantValue.STEP_VALUEMAPPER, taskVo, businessType);
        if (valueMappingSteps.size() > 0) {
            Map<String, String> targetMappers = getTargetMappers(taskVo);
            Map<String, Map<String, aa>> outMappers = getOutMappers(valueMappingSteps, businessType);
            checkMappers(outMappers, targetMappers);
        }*/
        return true;
    }


    /**
     * 获取值映射步骤里面 所有的输出字段 及其 映射关系
     * ps : 一个任务有多个值映射步骤 每个值映射可以配置多个输出 ，每个输出 有一套对应的 源值 和目标值。
     *
     * @param valueMappingSteps
     * @return
     */
    private Map<String, Map<String, aa>> getOutMappers(List<String> valueMappingSteps, int busType) {
        Map<String, Map<String, aa>> m = new HashMap<>();
        for (int k = 0; k < valueMappingSteps.size(); k++) {
            String vmstep = valueMappingSteps.get(k).split("######")[0];
            String stepName = valueMappingSteps.get(k).split("######")[1]; //步骤名称
            String targetField_ = "";
            Map<String, aa> pmap = new HashMap<>();
            try {
                List<ValuemapperVo> valuemapperVos = mapper.readValue(vmstep, ConvertRuleValuemapper.class).getDataList();
                //循环每一个输出 找到 对应的原值 和目标值关系
                for (int j = 0; j < valuemapperVos.size(); j++) {
                    ValuemapperVo valuemapperVo = valuemapperVos.get(j);
                    String sourceField = valuemapperVo.getSourceField();
                    String targetField = valuemapperVo.getTargetField();//输出字段
                    if (busType == 1) {
                        targetField_ = targetField;
                    } else {
                        targetField_ = sourceField;
                    }

                    List<String> targetStrs = new ArrayList<String>();
                    List<String> sourceStrs = new ArrayList<String>();
                    //源值 和目标值 后面针对 效验的时候 就是用这两个值 跟 rule 来对应
                    for (int i = 0; i < valuemapperVo.getMappings().size(); i++) {
                        targetStrs.add(valuemapperVo.getMappings().get(i).getTargetValue());
                        sourceStrs.add(valuemapperVo.getMappings().get(i).getSourceValue());
                    }
                    if (sourceStrs.size() > 0 && targetStrs.size() > 0) {
                        aa a = new aa();
                        a.setSourceStrs(sourceStrs);
                        a.setTargetStrs(targetStrs);
                        //以输出字段为key  对应 映射值 为value
                        pmap.put(targetField_, a);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            m.put(stepName, pmap);
        }
        return m;
    }

    class aa {
        List<String> sourceStrs;
        List<String> targetStrs;

        public List<String> getSourceStrs() {
            return sourceStrs;
        }

        public void setSourceStrs(List<String> sourceStrs) {
            this.sourceStrs = sourceStrs;
        }

        public List<String> getTargetStrs() {
            return targetStrs;
        }

        public void setTargetStrs(List<String> targetStrs) {
            this.targetStrs = targetStrs;
        }
    }

    /**
     * 验证核心代码
     * Map map = new HashMap();
     * <p>
     * 　　Iterator iter = map.entrySet().iterator();
     * <p>
     * 　　while (iter.hasNext()) {
     * <p>
     * 　　Map.Entry entry = (Map.Entry) iter.next(); Object key = entry.getKey();
     * <p>
     * 　　Object val = entry.getValue();
     * <p>
     * 　　}
     *
     * @param outMappers
     * @param targetMappers
     */
    private void checkMappers(Map<String, Map<String, aa>> outMappers, Map<String, String> targetMappers) {
        Iterator iter = outMappers.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String stepName = (String) entry.getKey();//值映射步骤名
           // System.out.println("当前的步骤名称：===" + stepName);
            Map<String, aa> relationMap = (Map<String, aa>) entry.getValue(); //对应的 输出和 映射值对象
            Iterator ralationIterator = relationMap.entrySet().iterator();
            while (ralationIterator.hasNext()) {
                Map.Entry r_entry = (Map.Entry) ralationIterator.next();
                String field = (String) r_entry.getKey();
                if (targetMappers.containsKey(field)) {
                    //如果  。。。目标对应的字段里面有 与 值映射输出的字段 有相等
                    System.out.println("存在" + field + ",被映射使用!");
                }
            }
        }
    }

    /**
     * 这一步是从 映射字段里面 获取所有的映射 源字段 和目标字段
     *
     * @param taskVo
     * @return
     */
    private Map<String, String> getTargetMappers(DataTaskVo taskVo) {
        Map<String, String> mmps = new HashMap<String, String>();
        if (taskVo.getSteps().size() > 0) {
            JSONObject target = JSONObject.parseObject(taskVo.getSteps().get(taskVo.getSteps().size() - 1));
            if (!target.isEmpty()) {
                ConvertRuleTarget targetObj = JSONObject.toJavaObject(target, ConvertRuleTarget.class);
                if (targetObj != null) {
                    SourceTargetVo center = targetObj.getData();
                    if (center != null) {
                        List<TargetMappingVo> mappingVos = center.getMappings();
                        for (TargetMappingVo vo : mappingVos) {
                         //   System.out.println("----S----" + vo.getSourceField() + "----T----" + vo.getTargetField());
                            //注意 此处是 以 目标字段为key  源字段为值  ：因为 目标字段不可能重复 ，但 源字段 可能重复
                            mmps.put(vo.getSourceField(), vo.getTargetField());
                        }
                    }
                }
            }
        }
        return mmps;
    }

    private List<String> getStepsByType(int stepValuemapper, DataTaskVo taskVo, int businessType) {
        List<String> valueMappingSteps = new ArrayList<String>();
        for (String stepstr : taskVo.getSteps()) {
            List stepInfo = Utils.getStepInfo(stepstr);
            if (stepInfo == null) {
                throw new OrdinaryException(this.getName(businessType) + " 没有有效的转换步骤！");
            }
            int stepType = (int) stepInfo.get(2);
            String stepName = (String) stepInfo.get(1);
            if (stepType == stepValuemapper) {//判断是否 有7 的步骤
                valueMappingSteps.add(stepstr + "######" + stepName);
            }
        }
        return valueMappingSteps;
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
        if (vo == null) {
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
        logInfo.setOperContent("手动同步" + this.getName(businessType) + "资源：" + vo.getTaskName());
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public boolean startLocalKettleNow() {
        String kettleInstallPath = "";
        SysConfigVo sysConfigVo = this.sysConfigMapper.queryEntity(false);
        if (sysConfigVo != null) {
            kettleInstallPath = sysConfigVo.getKettleInstallPath();
        }
        //System.out.println(kettleInstallPath);
        if (StringUtil.isNotBlank(kettleInstallPath)) {
            String spoonFilePath = kettleInstallPath + File.separator + "Spoon.bat";
            File s = new File(spoonFilePath);
            if (!s.exists()) {
                System.out.println("本地kettle安裝路徑配置有誤!请联系管理员!");
                return false;
            }
        }
        Runtime rt = Runtime.getRuntime();
        String strcmd = "cmd  /c start " + kettleInstallPath + "/Spoon.bat";
        Process ps = null;  //Process可以控制该子进程的执行或获取该子进程的信息。
        try {
            ps = rt.exec(strcmd);   //该对象的exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
            ps.waitFor();  //等待子进程完成再往下执行。
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = ps.exitValue();  //接收执行完毕的返回值
        if (i == 0) {
            System.out.println("执行完成.");
        } else {
            System.out.println("执行失败.");
            return false;
        }

        ps.destroy();  //销毁子进程
        ps = null;
        return true;
    }

    @Override
    public List<TransFileVo> queryKfileList(String fileType, String fileName, int pageNum, int pageSize) {
        return this.dataTaskMapper.queryKfileList(fileName, fileType, pageNum, pageSize);
    }

    @Override
    public List<TransFileVo> queryKfileListAll() {
        return this.dataTaskMapper.queryKfileListAll();
    }

    private void cleanData(String type) {
       // System.out.println("-----------先清理数据库表数据------------------" + type);

        this.sysConfigMapper.truncateTransFile(type);

       // System.out.println("------------end----------------" + type);
    }

    @Override
    public void flushTransFileVo(String ktrpath, String type) {
        cleanData(type);
        List<File> fileList = ReadFileUtil.getFileList(ktrpath, type);
        TransFileVo transFileVo = new TransFileVo();
        for (File a : fileList) {
            Path p = Paths.get(a.getAbsolutePath());
            try {
             //   System.out.println(a.getCanonicalPath());
                BasicFileAttributes att = Files.readAttributes(p, BasicFileAttributes.class);//获取文件的属性
                String createtime = att.creationTime().toString();
                String accesstime = att.lastAccessTime().toString();
                String lastModifiedTime = att.lastModifiedTime().toString();
                String name = a.getName();
            //    System.out.println("扫描到" + type + "文件：" + name);
                String createUserName = "admin";
                transFileVo.setFileName(name);
                transFileVo.setCreateTime(DateUtil.parseDate(createtime));
                transFileVo.setFilePath(a.getCanonicalPath());
                transFileVo.setFileType(type);
                transFileVo.setUpdateTime(DateUtil.parseDate(lastModifiedTime));
                transFileVo.setAccessTime(DateUtil.parseDate(accesstime));
                transFileVo.setCreateUserInfo(createUserName);
                transFileVo.setScheduleInfo("0 0/60 * * * ? *");
                this.sysConfigMapper.saveTransFile(transFileVo);
            //    System.out.println("插入了一条了:" + transFileVo.getFileName());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public TransFileVo findEtlFileInfoById(String fileName) {
        return this.dataTaskMapper.findEtlFileInfoById(fileName);
    }

    @Override
    public boolean runItem(List<String> vlist, String fileType) {
        for (String fileName : vlist) {
            String dfile = fileName + "." + fileType;
            System.out.println(dfile);
            TransFileVo vo = this.dataTaskMapper.findEtlFileInfoById(dfile);
            if (vo != null) {
                String filePath = vo.getFilePath();
                if (StringUtil.isBlank(filePath)) {
                    throw new OrdinaryException("文件路径不存在!");
                }

                File ktrFile = new File(filePath);
                if (!ktrFile.exists()) {
                    throw new OrdinaryException("文件不存在!!!");
                }

                int errors = 0;
                KettleExecuUtil execuUtil = new KettleExecuUtil();
                if (fileType.equals("ktr")) {
                    errors = execuUtil.runTrans(filePath);
                } else if (fileType.equals("kjb")) {
                    errors = execuUtil.runJob(filePath);
                }
                if (errors > 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 这个操作 是把选择的task 复制一份到目标部门里面。
     *
     * @param vv
     * @return
     */
    @Override
    public boolean cloneTasksTo(TaskOrgsInfoVo vv) {
        if (EmptyUtil.isEmpty(vv.getOrgList())) {
            throw new OrdinaryException("目标机构信息不完整，请重新选择！");
        }
        System.out.println(vv.toString());
        String ids = vv.getIds();
        String[] ids_ = ids.split("#");
        List<String> orgList = vv.getOrgList();
        /**
         * 循环已经选择的 被 克隆的 转换任务ids
         *
         */
        for (String taskId : ids_) {
            int taskId_ = Integer.parseInt(taskId);
            DataTaskVo taskVo = dataTaskMapper.getDataTaskVo(taskId_);
            int thirdConnectionId = 0;
            if (taskVo == null) {
                throw new OrdinaryException("数据资源不存在会已被删除！");
            }
            //  List<TaskStepVo> taskStepVos = this.dataTaskMapper.getTaskStepsAll(taskId_);
            List<String> taskStepStrings = this.dataTaskMapper.getTaskSteps(taskId_);
           // System.out.println("当前转换有======" + taskStepStrings.size() + " 步");
            for (String orgId : orgList) {
                Integer thridconnId = 0;
                List<Integer> thirdConnectionId_ = this.dataTaskMapper.getTopThirdConnectionId(orgId);
                if (thirdConnectionId_.size() > 0) {
                    thridconnId = thirdConnectionId_.get(0);
                }
            //    System.out.println("thridconnId======" + thridconnId);
                DataTaskVo taskVo1 = new DataTaskVo();
                BeanUtils.copyProperties(taskVo, taskVo1, new String[]{"id", "taskName", "description", "orgId", "thirdConnectionId"});
                taskVo1.setOrgId(orgId);
                taskVo1.setThirdConnectionId(thridconnId);
                String taskName = taskVo.getTaskName() + "_复制_" + orgId;
                taskVo1.setTaskName(taskName);
                taskVo1.setDescription(taskName);
                this.dataTaskMapper.insertDataTask(taskVo1, "1");
                taskVo1.setSteps(taskStepStrings);
                int tid = taskVo1.getTaskId();
                this.dataTaskMapper.insertTaskSteps(tid, taskVo1.getSteps(), "1");

                //保存日志
                LogInfo logInfo = new LogInfo();
                logInfo.setModuleId(getMoudleId(taskVo.getBusinessType()));
                logInfo.setOperType("新增");
                logInfo.setOperContent("新增" + this.getName(taskVo1.getBusinessType()) + "资源：" + taskVo1.getTaskName());
                this.logServiceImpl.saveLog(logInfo);
            }
        }
        return true;
    }


    @Override
    public List<DataTaskVo> queryTasks(int busType, String orgId) {
        return this.dataTaskMapper.queryTasks(busType, orgId);
    }

    private static String OS = System.getProperty("os.name").toLowerCase();


    /**
     * 判断操作系统类型 如果是window 则先根据规则 删除定时任务，然后再创建一个新的定时任务
     *
     * @param filePath
     * @return
     */
    public boolean sendInTask(String filePath) {
        boolean flag = false;
        if (OSinfo.getOSname().equals("Windows")) {
            File tempFile = new File(filePath.trim());
            String fileName = tempFile.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            StringBuffer taskNameBuffer = new StringBuffer(fileName);
            taskNameBuffer = taskNameBuffer.append(UUID.randomUUID().toString().replace("-", ""));
            String command = "schtasks /delete /tn " + taskNameBuffer + " /f";
            StringBuilder sb = new StringBuilder();
            Runtime runtime = Runtime.getRuntime();
            try {
                String line = null;
                Process process = runtime.exec(command);
                BufferedReader bufferedReader = new BufferedReader
                        (new InputStreamReader(process.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                    //System.out.println(line);
                }
                command = "";

            } catch (IOException e) {
                e.printStackTrace();
            }
            //再插入这个job
        } else if (OSinfo.getOSname().equals("Linux")) {

        }

        return flag;
    }


    public void ss() throws IOException {

        Runtime runtime = Runtime.getRuntime();
        String t = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //获取发布运行路径

        //解决路径中空格的问题
        t = URLDecoder.decode(t, "utf-8");

        int num = t.indexOf("test");//查找此项目跟目录位置

        String path = t.substring(1, num).replace('/', '\\') + "test\\static\\bat\\1.bat";//写bat文件路径

        String data = "";//"\"C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump\" -u \"root\" -p\"root\" -P\"3306\" \"test\"> \""+ mysqlPathString + "\\" + fileNameString + ".sql\"";

//bat内容

        data.replace("\'", "\"");

        File txt = new File(path);

        txt.createNewFile();//创建文件

        byte bytes[] = new byte[512];
        bytes = data.getBytes();//写文件

        int b = data.length();

        FileOutputStream fos = new FileOutputStream(txt);

        fos.write(bytes, 0, b);

        fos.close();

        runtime.exec(path);
    }
}
