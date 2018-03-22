package com.gtafe.data.center.dataetl.schedule;

import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import com.gtafe.data.center.dataetl.schedule.mapper.EtlMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.mapper.KettleLogMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;
import com.gtafe.data.center.runadmin.nodewatch.vo.EtlTaskStatus;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
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

    @Scheduled(cron = "0 0 0/1 * * *")
    // @Scheduled(cron = "0 02 17 ? * *")
    public void ScanTransFileJob() {
        System.out.println("開始執行任務....");
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

            List<File> ktrfileList = ReadFileUtil.getFileList(ktrpath, "ktr");
            logger.info("扫描到ktr文件的数量为" + ktrfileList.size() + " 个。");
            if (ktrfileList.size() > 0) {
                int i = 1;
                for (File a : ktrfileList) {
                    Path p = Paths.get(a.getAbsolutePath());
                    TransFileVo transFileVo = new TransFileVo();
                    try {
                        BasicFileAttributes att = Files.readAttributes(p, BasicFileAttributes.class);//获取文件的属性
                        String createtime = att.creationTime().toString();
                        String accesstime = att.lastAccessTime().toString();
                        String lastModifiedTime = att.lastModifiedTime().toString();
                        String name = a.getName();
                        String createUserName = "admin";
                        transFileVo.setFileId(i);
                        transFileVo.setFileName(name);
                        transFileVo.setCreateTime(DateUtil.parseDate(createtime));
                        transFileVo.setFilePath(a.getCanonicalPath());
                        transFileVo.setFileType("ktr");
                        transFileVo.setUpdateTime(DateUtil.parseDate(lastModifiedTime));
                        transFileVo.setAccessTime(DateUtil.parseDate(accesstime));
                        transFileVo.setCreateUserInfo(createUserName);
                        i++;
                        this.sysConfigMapper.saveTransFile(transFileVo);
                    } catch (IOException e1) {
                        logger.info(e1.getLocalizedMessage());
                    }
                }
            }
            List<File> kjbfileList = ReadFileUtil.getFileList(kjbPath, "kjb");
            logger.info("扫描到  kjb 文件的数量为" + ktrfileList.size() + " 个。");
            if (kjbfileList.size() > 0) {
                int i = 1;
                for (File a : kjbfileList) {
                    Path p = Paths.get(a.getAbsolutePath());
                    TransFileVo transFileVo = new TransFileVo();
                    try {
                        BasicFileAttributes att = Files.readAttributes(p, BasicFileAttributes.class);//获取文件的属性
                        String createtime = att.creationTime().toString();
                        String accesstime = att.lastAccessTime().toString();
                        String lastModifiedTime = att.lastModifiedTime().toString();
                        String name = a.getName();
                        String createUserName = "admin";
                        transFileVo.setFileId(i);
                        transFileVo.setFileName(name);
                        transFileVo.setCreateTime(DateUtil.parseDate(createtime));
                        transFileVo.setFilePath(a.getCanonicalPath());
                        transFileVo.setFileType("kjb");
                        transFileVo.setUpdateTime(DateUtil.parseDate(lastModifiedTime));
                        transFileVo.setAccessTime(DateUtil.parseDate(accesstime));
                        transFileVo.setCreateUserInfo(createUserName);
                        i++;
                        this.sysConfigMapper.saveTransFile(transFileVo);
                    } catch (IOException e1) {
                        // e1.printStackTrace();
                        logger.info(e1.getLocalizedMessage());
                    }
                }
            }
        }
        System.out.println("扫描任务完毕!");
    }
}
