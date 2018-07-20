package com.gtafe.data.center.dataetl.schedule;

import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import com.gtafe.data.center.dataetl.schedule.mapper.EtlMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.mapper.KettleLogMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;
import com.gtafe.data.center.runadmin.nodewatch.vo.EtlTaskStatus;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.utils.DateUtil;
import com.gtafe.framework.base.utils.ReadFileUtil;
import com.gtafe.framework.base.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.util.List;
import java.util.logging.FileHandler;

/**
 * 内部定时任务 扫描ktr kjb 文件的定时任务
 */
@Component
public class ScanTransFileJob {
    Logger logger = LoggerFactory.getLogger(ScanTransFileJob.class);
    @Autowired
    private SysConfigMapper sysConfigMapper;


    @Autowired
    DataTaskService dataTaskServiceImpl;

    @Scheduled(cron = "0 0 0/30 * * *")
    public void ScanTransFileJob() {
     //   System.out.println("開始執行任務....");
      this.doTask();
    }

    private void doTask() {
        SysConfigVo sysConfigVo = sysConfigMapper.queryEntity(false);
        if (sysConfigVo != null) {
            String ktrpath = sysConfigVo.getKtrFilesPath();
            String kjbPath = sysConfigVo.getKjbFilesPath();
            if (StringUtil.isBlank(ktrpath)) {
                logger.info("需要联系管理员 配置 ktr文件保存路径!");
                return;
            }
            if (StringUtil.isBlank(kjbPath)) {
                logger.info("需要联系管理员 配置 kjb文件保存路径!");
                return;
            }
            dataTaskServiceImpl.flushTransFileVo(kjbPath,"kjb");
            dataTaskServiceImpl.flushTransFileVo(ktrpath,"ktr");
        }
      //  System.out.println("扫描任务完毕!");
    }
}
