package com.gtafe.data.center.dataetl.schedule;

import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.datatask.vo.EtlTaskNoteVo;
import com.gtafe.data.center.dataetl.datatask.vo.TaskFieldDetailsVo;
import com.gtafe.data.center.dataetl.schedule.mapper.EtlMapper;
import com.gtafe.data.center.dataetl.trans.ConstantValue;
import com.gtafe.data.center.dataetl.trans.OutputTable;
import com.gtafe.data.center.dataetl.trans.Utils;
import com.gtafe.framework.base.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 扫描task 并把所有的任务的输出字段 保存入库
 */
@PropertySource("classpath:config.properties")
@Component
public class ScanTaskFieldDetailSchedule {


    @Autowired
    EtlMapper etlMapper;

    @Autowired
    DataTaskMapper dataTaskMapper;

    @Scheduled(cron = "${ScanSchedule}")
    public void executeJob() {
        List<DataTaskVo> dataTasks = etlMapper.getAllTask2();
        System.out.println("查询出来 总任务数量为：" + dataTasks.size());
        System.out.println("开始循环...");

        for (DataTaskVo dataTask : dataTasks) {

            System.out.println("dataTask.getTaskId()........" + dataTask.getTaskId());
            //源表名和目标表名
            String sourceDBName = "", targetDBName = "";
            EtlTaskNoteVo taskFieldsVo = new EtlTaskNoteVo();

            //#################################################################################
            taskFieldsVo.setBusType(dataTask.getBusinessType() == 1 ? "发布" : "订阅");
            taskFieldsVo.setTaskId(dataTask.getTaskId());//主键
            taskFieldsVo.setRemark(dataTask.getRunEexpression());
            taskFieldsVo.setTaskName(dataTask.getTaskName());
            //##################################################################################

            if (dataTask.getBusinessType() == 1) {//发布 从 第三方库 表数据 到 中心库 表数据
                sourceDBName = dataTask.getThirdTablename().split("#")[0];
                targetDBName = dataTask.getCenterTablename();
            } else if (dataTask.getBusinessType() == 2) { // 订阅 从中心库表数据 到 第三方库表数据
                targetDBName = dataTask.getThirdTablename().split("#")[0];
                sourceDBName = dataTask.getCenterTablename();
            }
            //######################################
            taskFieldsVo.setSourceTable(sourceDBName);
            taskFieldsVo.setSourceTableName(sourceDBName);
            taskFieldsVo.setTargetTable(targetDBName);
            taskFieldsVo.setTargetTableName(targetDBName);
            //#######################################
            System.out.println(dataTask.toString());
            List<TaskFieldDetailsVo> detailsVos = new ArrayList<TaskFieldDetailsVo>();
            List<TaskFieldDetailsVo> detailsVos_ = new ArrayList<TaskFieldDetailsVo>();
            List<String> steps = this.etlMapper.getStepById(dataTask.getTaskId());
            System.out.println("当前任务的步骤有：" + steps.size() + "个");
            for (String stepstr : steps) {
                List stepInfo = Utils.getStepInfo(stepstr);
                if ((int) stepInfo.get(2) == ConstantValue.STEP_OUTPUTTABLE) {
                    //初始化表输出
                    System.out.println(stepstr);
                    OutputTable outputTable = new OutputTable(0, 100, (String) stepInfo.get(1), null, targetDBName, stepstr);
                    detailsVos = outputTable.getTaskFieldDetailsVoList(stepstr);
                    for (TaskFieldDetailsVo r : detailsVos) {
                        TaskFieldDetailsVo rd = new TaskFieldDetailsVo();
                        String ignoreProperties = new String("taskId");
                        BeanUtils.copyProperties(r, rd, ignoreProperties);
                        rd.setTaskId(taskFieldsVo.getTaskId());
                        detailsVos_.add(rd);
                    }
                    taskFieldsVo.setTaskFieldDetailsVoList(detailsVos_);
                }
            }
            dataTaskMapper.deleteTaskNoteAndDetail2(dataTask.getTaskId());//detail
            dataTaskMapper.deleteTaskNoteAndDetail1(dataTask.getTaskId());//note
            //保存入库
            dataTaskMapper.saveTaskFieldNoteInfo(taskFieldsVo);
            for (TaskFieldDetailsVo taskFieldDetailsVo : taskFieldsVo.getTaskFieldDetailsVoList()) {
                dataTaskMapper.saveTaskFieldNoteDetail(taskFieldDetailsVo);
            }
            System.out.println("保存 " + dataTask.getTaskId() + " fields info over");
        }
    }
}
