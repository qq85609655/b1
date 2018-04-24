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
import com.gtafe.data.center.dataetl.datatask.vo.OSinfo;
import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.ConvertRuleValuemapper;
import com.gtafe.data.center.dataetl.datatask.vo.rule.rulevo.ValuemapperVo;
import com.gtafe.data.center.dataetl.trans.Utils;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.utils.*;
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

    ObjectMapper mapper;

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

        boolean hsValueMapping = false;

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String valueMappStepStr = "";
        for (String stepstr : taskVo.getSteps()) {
            List stepInfo = Utils.getStepInfo(stepstr);
            if (stepInfo == null) {
                throw new OrdinaryException(this.getName(businessType) + " 没有有效的转换步骤！");
            }
            int stepType = (int) stepInfo.get(2);
            if (stepType == 7) {//判断是否 有7 的步骤
                hsValueMapping = true;
                valueMappStepStr = stepstr;
                break;
            }

        }
        if (hsValueMapping && StringUtil.isNotBlank(valueMappStepStr)) {
            List<ValuemapperVo> valuemapperVos;
            try {
                valuemapperVos = mapper.readValue(valueMappStepStr, ConvertRuleValuemapper.class).getDataList();
            } catch (IOException e) {

            }
        }


        if (hsValueMapping) {

        }

        //如果有 找到对应的 字段 有没有关联的code类

        //找到类之后 把对应的代码放到list 里面

        //然后 再查询 用户输入的 是否合理


    }


    @Override
    public int insertDataTaskVo(int businessType, DataTaskVo taskVo) {
        this.checkValueMapper4TaskVo(businessType, taskVo);
        this.revisionDataTaskVo(businessType, taskVo);
        if (this.dataTaskMapper.checkTaskNameRepeat(null,
                taskVo.getTaskName(), taskVo.getOrgId(), businessType) > 0) {
            throw new OrdinaryException(this.getName(businessType) + "资源名称已存在");
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
        if (this.dataTaskMapper.checkTaskNameRepeat(dbVo.getTaskId(),
                taskVo.getTaskName(), taskVo.getOrgId(), businessType) > 0) {
            throw new OrdinaryException(this.getName(businessType) + "资源名称已存在");
        }
        int userId = this.getUserId();
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
        System.out.println(kettleInstallPath);
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

    @Override
    public void flushTransFileVo(String ktrpath, String type) {
        this.sysConfigMapper.truncateTransFile(type);
        List<File> fileList = ReadFileUtil.getFileList(ktrpath, type);
        if (fileList.size() > 0) {
            for (File a : fileList) {
                Path p = Paths.get(a.getAbsolutePath());
                TransFileVo transFileVo = new TransFileVo();
                try {
                    BasicFileAttributes att = Files.readAttributes(p, BasicFileAttributes.class);//获取文件的属性
                    String createtime = att.creationTime().toString();
                    String accesstime = att.lastAccessTime().toString();
                    String lastModifiedTime = att.lastModifiedTime().toString();
                    String name = a.getName();
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
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public TransFileVo findEtlFileInfoById(String fileName) {
        return this.dataTaskMapper.findEtlFileInfoById(fileName);
    }

    @Override
    public boolean runItem(String fileName) {
        TransFileVo vo = this.dataTaskMapper.findEtlFileInfoById(fileName);
        if (vo != null) {
            String filePath = vo.getFilePath();
            String type = vo.getFileType();
            if (StringUtil.isBlank(filePath)) {
                throw new OrdinaryException("文件路径不存在!");
            }
            if (StringUtil.isBlank(type) || !type.equals("ktr") || !type.equals("kjb")) {
                throw new OrdinaryException("文件类型有异常!");
            }
            File ktrFile = new File(filePath);
            if (!ktrFile.exists()) {
                throw new OrdinaryException("文件不存在!!!");
            }
            int errors = 0;
            KettleExecuUtil execuUtil = new KettleExecuUtil();
            if (type.equals("ktr")) {
                errors = execuUtil.runTrans(filePath);
            } else if (type.equals("kjb")) {
                errors = execuUtil.runJob(filePath);
            }
            if (errors > 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;
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
                    System.out.println(line);
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
