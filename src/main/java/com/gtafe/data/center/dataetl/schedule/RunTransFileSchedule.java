package com.gtafe.data.center.dataetl.schedule;


import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.utils.StringUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 定时任务 逻辑：
 * <p>
 * 隔一段时间 扫描磁盘 是否有新的文件，如果有就加入到数据库表中，并刷新quartz 的job
 */
@Component
public class RunTransFileSchedule {
    Logger logger = LoggerFactory.getLogger(RunTransFileSchedule.class);


    private Scheduler scheduler;

    public static LinkedBlockingQueue<Integer> taskIdQueue = new LinkedBlockingQueue<>();

    @Autowired
    DataTaskService dataTaskServiceImpl;
    @Autowired
    SysConfigMapper sysConfigMapper;

    @Scheduled(initialDelay = 200000, fixedRate =900000)
    private void doRunTransTask() throws SchedulerException {

        SysConfigVo sysConfigVo = sysConfigMapper.queryEntity(false);
        if (sysConfigVo != null) {
            String ktrpath = sysConfigVo.getKtrFilesPath();
            String kjbPath = sysConfigVo.getKjbFilesPath();
            if (StringUtil.isBlank(ktrpath)) {
                logger.info("需要联系管理员 配置 ktr文件保存路径!");
                return;
            }
            dataTaskServiceImpl.flushTransFileVo(ktrpath, "ktr");
            //此时查询出所有的ktr 信息
            List<TransFileVo> transFileVoList = this.dataTaskServiceImpl.queryKfileListAll();
            if (transFileVoList.size() > 0) {
                Set<JobKey> dbjobKeys = new HashSet<>();
                for (TransFileVo dataTask : transFileVoList) {
                    JobKey jobKey = new JobKey(String.valueOf(dataTask.getFileName()), "ktr");
                    dbjobKeys.add(jobKey);
                }
                GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals("ktr");
                Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
                for (JobKey jobKey : jobKeys) {
                    if (!dbjobKeys.contains(jobKey)) {
                        scheduler.deleteJob(jobKey);
                    }
                }
            }

            //添加任务
            for (TransFileVo dataTask : transFileVoList) {
                JobKey jobKey = new JobKey(String.valueOf(dataTask.getFileName()), "ktr");
                if (!scheduler.checkExists(jobKey)) {

                    JobDetail jobDetail = newJob(EtlJob.class)
                            .withIdentity(String.valueOf(dataTask.getFileName()), "ktr")
                            .build();

                    jobDetail.getJobDataMap().put("ktrFileName", dataTask.getFileName());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    logger.info(sdf.format(dataTask.getUpdateTime()));
                    jobDetail.getJobDataMap().put("updateTime", dataTask.getUpdateTime());
                    Trigger trigger = newTrigger()
                            .withIdentity(String.valueOf(dataTask.getFileName()), "ktr")
                            .withSchedule(cronSchedule(dataTask.getScheduleInfo()))
                            .build();

                    scheduler.scheduleJob(jobDetail, trigger);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date taskdt;
                    try {
                        taskdt = sdf.parse(scheduler.getJobDetail(jobKey).getJobDataMap().getString("updateTime"));
                    } catch (ParseException e) {
                        scheduler.deleteJob(jobKey);
                        continue;
                    }
                    if (dataTask.getUpdateTime().getTime() > taskdt.getTime()) {
                        scheduler.deleteJob(jobKey);
                    }
                }
            }
            if (StringUtil.isBlank(kjbPath)) {
                logger.info("需要联系管理员 配置 kjb文件保存路径!");
                return;
            }

        }

    }

    @PostConstruct
    public void init() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

        } catch (SchedulerException e) {
            return;
        }
    }

    @PreDestroy
    public void despose() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            return;
        }
    }
}
