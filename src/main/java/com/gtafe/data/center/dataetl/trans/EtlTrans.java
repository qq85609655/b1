package com.gtafe.data.center.dataetl.trans;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.schedule.mapper.EtlMapper;
import com.gtafe.data.center.runadmin.alertpush.mapper.AlertPushMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.mapper.KettleLogMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.ErrorLogVo;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.EmailSendLog;
import com.gtafe.data.center.system.user.mapper.SysUserMapper;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.DateUtil;
import com.gtafe.framework.base.utils.MailSender;
import com.gtafe.framework.base.utils.StringUtil;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransHopMeta;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepErrorMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EtlTrans {

    private static final Logger LOGGER = LoggerFactory.getLogger(EtlTrans.class);

    private int locationX = 100;

    private TransMeta transMeta;

    //源步骤，表输入步骤，输出步骤
    private StepMeta fromStep = null, inputStep = null, outputStep = null;

    @Value("${db.jdbc.logusername}")
    private String logusername;//= "root";

    @Value("${db.jdbc.logpassword}")
    private String logpassword;// = "GTA01230!!!";

    @Value("${db.jdbc.logip}")
    private String logip;//= "10.10.130.147";

    @Value("${db.jdbc.logport}")
    private int logport;//= 3306;

    @Value("${db.jdbc.logdbname}")
    private String logdbname;

    @Autowired
    EtlMapper etlMapper;

    @Autowired
    private KettleLogMapper kettleLogMapper;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private AlertPushMapper alertPushMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private LogService logServiceImpl;

    /**
     * 执行转换任务 并对转换过程中的异常情况进行发邮件通知
     *
     * @param taskId
     */
    public void execute(int taskId) {

        //任务
        DataTaskVo dataTask = etlMapper.getDataTaskById(taskId);

        if (dataTask == null) {
            etlMapper.stopErrorTask(taskId);
            return;
        }
        if (dataTask.getSteps().size() == 0) {
            etlMapper.stopErrorTask(taskId);
            return;
        }

        //启动kettle
        try {
            KettleEnvironment.init();
        } catch (KettleException e) {
            return;
        }

        //定义转换
        transMeta = new TransMeta();
        transMeta.setName(String.valueOf(taskId));
        Trans trans = new Trans(transMeta);

        //源数据源和目标数据源
        DatasourceVO sourceDS, targetDS;

        //日志数据源
        DatasourceVO logDS = new DatasourceVO();
        logDS.setName("prjDS");
        logDS.setDbType(1);
        logDS.setHost(logip);
        logDS.setPort(logport);
        logDS.setUsername(logusername);
        logDS.setPassword(logpassword);
        logDS.setDbName(logdbname);

        int busType = dataTask.getBusinessType();
        //源表名和目标表名
        String sourceDBName, targetDBName;
        SysConfigVo vo = etlMapper.getCenterDS();
        //根据业务类型定义数据源
        if (busType == 1) {//发布 从 第三方库 表数据 到 中心库 表数据
            sourceDS = etlMapper.getDSById(dataTask.getThirdConnectionId());
            targetDS = StringUtil.getBySysConfig(vo);
            sourceDBName = dataTask.getThirdTablename().split("#")[0];
            targetDBName = dataTask.getCenterTablename();
        } else if (busType == 2) { // 订阅 从中心库表数据 到 第三方库表数据
            targetDS = etlMapper.getDSById(dataTask.getThirdConnectionId());
            sourceDS = StringUtil.getBySysConfig(vo);
            targetDBName = dataTask.getThirdTablename().split("#")[0];
            sourceDBName = dataTask.getCenterTablename();
        } else {
            etlMapper.stopErrorTask(taskId);
            return;
        }

        //数据源加入转换
        transMeta.addDatabase(Utils.InitDatabaseMeta(sourceDS));
        transMeta.addDatabase(Utils.InitDatabaseMeta(targetDS));
        transMeta.addDatabase(Utils.InitDatabaseMeta(logDS));

        //input
        for (String stepstr : dataTask.getSteps()) {

            //判断步骤类型
            List stepInfo = Utils.getStepInfo(stepstr);
            if (stepInfo == null) {
                etlMapper.stopErrorTask(taskId);
                return;
            }
            if ((int) stepInfo.get(2) == ConstantValue.STEP_INPUTTABLE) {
                //初始化表输入
                InputTable inputTable = new InputTable(locationX, 100, (String) stepInfo.get(1), sourceDS, sourceDBName);
                inputStep = inputTable.inputStep();
                transMeta.addStep(inputStep);
                fromStep = inputStep;
                locationX += 100;
                //增加日志id
                Constant constant = new Constant(locationX, 100, "日志id", "");
                StepMeta logidStep = constant.constantStep(new String[]{"channel_id_6666"}, new String[]{"String"}, new String[]{trans.getLogChannelId()});
                transMeta.addStep(logidStep);
                transMeta.addTransHop(new TransHopMeta(fromStep, logidStep));
                fromStep = logidStep;
                locationX += 100;
                break;
            }
        }

        //转换处理
        for (String stepstr : dataTask.getSteps()) {

            List stepInfo = Utils.getStepInfo(stepstr);

            if ((int) stepInfo.get(2) == ConstantValue.STEP_INPUTTABLE || (int) stepInfo.get(2) == ConstantValue.STEP_OUTPUTTABLE) {
                continue;
            }

            int stepType = (int) stepInfo.get(2);

            List<StepMeta> stepMeta = tranStep(stepType, String.valueOf(stepInfo.get(1)) + String.valueOf(stepInfo.get(0)), String.valueOf(stepInfo.get(0)), stepstr, busType, targetDS);

            if (stepMeta.size() == 0) {
                etlMapper.stopErrorTask(taskId);
                return;
            }

            if (stepType == 11) {
                StepError stepError = new StepError(locationX, 200, "类型转换错误处理" + String.valueOf(stepInfo.get(0)), logDS);
                StepMeta logStep = stepError.stepError();
                StepErrorMeta stepErrorMeta = new StepErrorMeta(stepError.variables(), stepMeta.get(0), logStep, "nr", "description", "field", "code");
                stepErrorMeta.setEnabled(true);
                stepMeta.get(0).setStepErrorMeta(stepErrorMeta);

                transMeta.addStep(stepMeta.get(0));
                transMeta.addTransHop(new TransHopMeta(fromStep, stepMeta.get(0)));

                transMeta.addStep(logStep);
                transMeta.addTransHop(new TransHopMeta(stepMeta.get(0), logStep));

                fromStep = stepMeta.get(0);
                locationX += 100;

            } else if (stepType == 13) {
                SelectValues caculateConvert = new SelectValues(locationX - 50, 100, String.valueOf(stepInfo.get(1)) + String.valueOf(stepInfo.get(0)) + "_add", stepstr);
                StepMeta cacStep = caculateConvert.caculateStep();

                StepError stepError = new StepError(locationX - 50, 200, "类型转换错误处理" + String.valueOf(stepInfo.get(0)), logDS);
                StepMeta logStep = stepError.stepError();
                StepErrorMeta stepErrorMeta = new StepErrorMeta(stepError.variables(), cacStep, logStep, "nr", "description", "field", "code");
                stepErrorMeta.setEnabled(true);
                cacStep.setStepErrorMeta(stepErrorMeta);

                transMeta.addStep(cacStep);
                transMeta.addTransHop(new TransHopMeta(fromStep, cacStep));

                transMeta.addStep(logStep);
                transMeta.addTransHop(new TransHopMeta(cacStep, logStep));

                transMeta.addStep(stepMeta.get(0));
                transMeta.addTransHop(new TransHopMeta(cacStep, stepMeta.get(0)));

                fromStep = stepMeta.get(0);
                locationX += 100;

            } else {
                for (StepMeta stepMeta1 : stepMeta) {
                    transMeta.addStep(stepMeta1);
                    transMeta.addTransHop(new TransHopMeta(fromStep, stepMeta1));
                    fromStep = stepMeta1;
                    locationX += 100;
                }
            }
        }

        //output
        for (String stepstr : dataTask.getSteps()) {

            List stepInfo = Utils.getStepInfo(stepstr);
            if ((int) stepInfo.get(2) == ConstantValue.STEP_OUTPUTTABLE) {

                //初始化表输出
                OutputTable outputTable = new OutputTable(locationX, 100, (String) stepInfo.get(1), targetDS, targetDBName, stepstr);
                outputStep = outputTable.outputStep();
                break;
            }
        }

        //表输出错误处理
        StepError stepError = new StepError(locationX, 200, "错误处理", logDS);
        StepMeta logStep = stepError.stepError();
        StepErrorMeta stepErrorMeta = new StepErrorMeta(stepError.variables(), outputStep, logStep, "nr", "description", "field", "code");
        stepErrorMeta.setEnabled(true);
        outputStep.setStepErrorMeta(stepErrorMeta);

        transMeta.addStep(outputStep);
        transMeta.addTransHop(new TransHopMeta(fromStep, outputStep));

        transMeta.addStep(logStep);
        transMeta.addTransHop(new TransHopMeta(outputStep, logStep));

        //转换日志处理
        transMeta.getTransLogTable().setConnectionName(logDS.getName());
        transMeta.getTransLogTable().setSchemaName(logDS.getDbName());
        transMeta.getTransLogTable().setTableName("kettle_log");
        transMeta.getTransLogTable().setStepInput(inputStep);
        transMeta.getTransLogTable().setStepOutput(outputStep);
        transMeta.getTransLogTable().setStepRead(outputStep);
        transMeta.getTransLogTable().setStepWritten(outputStep);
        transMeta.getTransLogTable().setStepUpdate(outputStep);
        transMeta.getTransLogTable().setStepRejected(outputStep);

        // ktr文件保存至本地
        Utils.outputktr(transMeta);

        // 执行处理
        try {
            trans.execute(null);
        } catch (KettleException e) {
        }

        trans.waitUntilFinished();

        KettleLogVO kettleLogVO = etlMapper.taskStatus(trans.getLogChannelId());
        if (kettleLogVO != null && kettleLogVO.getErrors() > 0) {
            etlMapper.stopErrorTask(taskId);
        }
        LOGGER.info("channelId===", trans.getLogChannelId());
        sendMail(trans.getLogChannelId());
        LOGGER.info("邮件发送完成");

    }


    private void sendMail(String logChannelId) {
        SysConfigVo configVo = this.sysConfigMapper.queryEntity(false);
        KettleLogVO vo = this.kettleLogMapper.queryKettleVOByChannelId(logChannelId);
        if (vo != null && vo.getErrors() > 0) {
            List<Integer> userIds = this.alertPushMapper.queryUserIdsByTransName(vo.getTransname());
            for (Integer u : userIds) {
                SysUserVo userVo = this.sysUserMapper.getUserVoByuserId(u);
                if (userVo != null) {
                    if (StringUtil.isNotBlank(userVo.getEmail())) {
                        StringBuffer content = new StringBuffer("【" + configVo.getSysName() + "】").
                                append("" + userVo.getRealName() + "您好，\n\n" +
                                        "您有一条【" + configVo.getSysName() + "】转换的问题待处理，\n\n" +
                                        "任务名称：" + vo.getTransname() + ",问题详情：\n\n").
                                append("<font color=red>" + vo.getLog_field()).append("</font> ");

                        List<ErrorLogVo> errorLogVos = this.kettleLogMapper.queryErrorList(vo.getChannel_id());
                        if (errorLogVos.size() > 0) {
                            content.append("\n\n 涉及到错误的业务数据信息（可能是业务表主键）:");
                            for (ErrorLogVo errorLogVo : errorLogVos) {
                                content.append("\n\n");
                                content.append(errorLogVo.getPkValue()).append(";");
                            }
                            LOGGER.info(content.substring(0, content.length() - 1));
                        }
                        content = content.append("\n\n请登录【" + configVo.getSysName() + "】进行处理！\n\n\n\n");
                        try {
                            boolean result = mailSender.sendEmail(userVo.getEmail(), "转换错误信息[" + vo.getTransname() + "]", content.toString());
                            LOGGER.info(result ? "发送成功" : "发送失败");
                            if (result) {
                                EmailSendLog emailSendLog = new EmailSendLog();
                                emailSendLog.setModule(4);
                                emailSendLog.setSendTime(DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                                emailSendLog.setUserNo("admin");
                                emailSendLog.setModuleName("转换错误日志");
                                emailSendLog.setSendSuccess(1);
                                emailSendLog.setContent("任务名称【" + vo.getTransname() + "】的错误信息给用户【" + userVo.getRealName() + "】发送邮件.");
                                this.logServiceImpl.saveEmailSendLog(emailSendLog);
                            }
                        } catch (Exception ee) {
                            throw new OrdinaryException("请检查邮件服务器配置...!");
                        }
                    }
                }
            }
        }
    }

    //json步骤处理


    private List<StepMeta> tranStep(int stepType, String name, String stepId, String stepstr, int busType, DatasourceVO targetDS) {

        List<StepMeta> stepMeta = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        switch (stepType) {

            //字符串操作
            case ConstantValue.STEP_STRINGOPERATIONS:
                StringOperations stringOperations = new StringOperations(locationX, 100, name, stepstr);
                stepMeta.add(stringOperations.strOpe());
                break;

            //字符串剪切
            case ConstantValue.STEP_STRINGCUT:
                StringCut stringCut = new StringCut(locationX, 100, name, stepstr);
                stepMeta.add(stringCut.strCut());
                break;

            //字符串替换
            case ConstantValue.STEP_REPLACESTRING:
                ReplaceString replaceString = new ReplaceString(locationX, 100, name, stepstr);
                stepMeta.add(replaceString.replace());
                break;

            //增加常量
            case ConstantValue.STEP_CONSTANT:
                Constant cons = new Constant(locationX, 100, name, stepstr);
                stepMeta.add(cons.constantStep());
                break;

            //值映射
            case ConstantValue.STEP_VALUEMAPPER:
                ValueMapper valueMapper = new ValueMapper(locationX, 100, name, stepstr);
                stepMeta.addAll(valueMapper.valueMapperStep());
                break;

            //拆分
            case ConstantValue.STEP_SPLIT:
                SelectValues addValues = new SelectValues(locationX, 100, name + "_add", stepstr);
                locationX += 100;
                Split split = new Split(locationX, 100, name, stepstr);

                stepMeta.add(addValues.addValueStep(stepId, split.fieldName()));
                transMeta.addStep(stepMeta.get(0));
                transMeta.addTransHop(new TransHopMeta(fromStep, stepMeta.get(0)));
                fromStep = stepMeta.get(0);

                stepMeta.clear();
                stepMeta.addAll(split.splitStep(stepId));

                break;

            //合并
            case ConstantValue.STEP_CONCAT:
                Concat concat = new Concat(locationX, 100, name, stepstr);
                stepMeta.addAll(concat.concatStep());
                break;

            //数值范围
            case ConstantValue.STEP_NUMBERRANGE:
                NumberRange numberRange = new NumberRange(locationX, 100, name, stepstr);
                stepMeta.addAll(numberRange.nrStep());
                break;

            //字段选择
            case ConstantValue.STEP_SELECTVALUES:
                SelectValues selectValues = new SelectValues(locationX, 100, name, stepstr);
                stepMeta.add(selectValues.selectValueStep());
                break;

            //列转多行
            case ConstantValue.STEP_SPLITFIELDTOROWS:
                SplitFieldToRows splitFieldToRows = new SplitFieldToRows(locationX, 100, name, stepstr);
                stepMeta.add(splitFieldToRows.ftrStep());
                break;

            //计算器
            case ConstantValue.STEP_CALCULATOR:
                Calculator calculator = new Calculator(locationX, 100, name, stepstr);
                stepMeta.add(calculator.ca());
                break;

            //去除重复记录
            case ConstantValue.STEP_UNIQUEROWSBYHASHSET:
                UniqueRowsByHashSet uniqueRowsByHashSet = new UniqueRowsByHashSet(locationX, 100, name, stepstr);
                stepMeta.add(uniqueRowsByHashSet.unique());
                break;

            //动态值映射
            case ConstantValue.STEP_DYNAMIC_VALUEMAPPING:
                DynamicValueMapper dynamicValueMapper = new DynamicValueMapper(locationX, 100, name, stepstr, targetDS);
                stepMeta.addAll(dynamicValueMapper.valueMapperStep());
                break;

            // 执行sql 脚本
            case ConstantValue.STEP_EXECUTESQL:
                ExecuteSql executeSql = new ExecuteSql(locationX, 100, name, stepstr, busType);
                stepMeta.addAll(executeSql.executeSqlStep());
                break;

            default:

        }

        return stepMeta;
    }
}
