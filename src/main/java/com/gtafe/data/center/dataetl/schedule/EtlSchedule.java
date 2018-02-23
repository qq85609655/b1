package com.gtafe.data.center.dataetl.schedule;

import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.schedule.mapper.EtlMapper;
import com.gtafe.data.center.dataetl.trans.EtlTrans;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class EtlSchedule {

    Logger logger= LoggerFactory.getLogger(EtlSchedule.class);

    @Autowired
    EtlMapper etlMapper;

    @Autowired
    EtlTrans etlTrans;

    private Scheduler scheduler;

    public static LinkedBlockingQueue<Integer> taskIdQueue=new LinkedBlockingQueue<>();

    /**
     * 清空当前任务
     */
    public void clearSchedule()  {
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            logger.error("clear schedule fail",e);
        }
    }

    /**
     * 获取当前运行中的JobKey
     * @return
     */
    public Set<JobKey> getSchedules()  {
        GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals("kettle");
        try {
            return scheduler.getJobKeys(matcher);
        } catch (SchedulerException e) {
            logger.error("get schedule jobkey fail",e);
            return  null;
        }
    }

    /**
     * 刷新任务
     * 新增、启动、停止、删除时调用此方法
     * @throws SchedulerException
     */
    private void refreshJobs() throws SchedulerException {

        List<DataTaskVo> dataTasks=etlMapper.getAllTask();

        if (dataTasks.size()>0) {

            //删除任务
            Set<JobKey> dbjobKeys=new HashSet<>();
            for (DataTaskVo dataTask : dataTasks) {
                JobKey jobKey=new JobKey(String.valueOf(dataTask.getTaskId()),"kettle");
                dbjobKeys.add(jobKey);
            }
            GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals("kettle");
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                if (!dbjobKeys.contains(jobKey)) {
                    scheduler.deleteJob(jobKey);
                }
            }

            //添加任务
            for (DataTaskVo dataTask : dataTasks) {
                JobKey jobKey=new JobKey(String.valueOf(dataTask.getTaskId()),"kettle");
                if (!scheduler.checkExists(jobKey)) {

                    JobDetail jobDetail=newJob(EtlJob.class)
                            .withIdentity(String.valueOf(dataTask.getTaskId()), "kettle")
                            .build();

                    jobDetail.getJobDataMap().put("taskId",dataTask.getTaskId());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    jobDetail.getJobDataMap().put("updateTime",sdf.format(dataTask.getUpdateTime()));
                    Trigger trigger=newTrigger()
                            .withIdentity(String.valueOf(dataTask.getTaskId()),"kettle")
                            .withSchedule(cronSchedule(dataTask.getRunEexpression()))
                            .build();

                    scheduler.scheduleJob(jobDetail,trigger);
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date taskdt;
                    try {
                        taskdt= sdf.parse(scheduler.getJobDetail(jobKey).getJobDataMap().getString("updateTime"));
                    } catch (ParseException e) {
                        scheduler.deleteJob(jobKey);
                        continue;
                    }
                    if (dataTask.getUpdateTime().getTime() > taskdt.getTime()) {
                        scheduler.deleteJob(jobKey);
                    }
                }
            }
        }else {
            scheduler.clear();
        }

    }

//    public Properties iniProp(int taskId) {
//
//        DataTaskVo dataTask=etlMapper.getDataTaskById(taskId);
//
//        Properties prop = new Properties();
//        prop.put("taskId",taskId);
//        prop.put("dataTask",dataTask);
//        prop.put("DS", etlMapper.getDSById(dataTask.getThirdConnectionId()));
//        prop.put("centerDS", etlMapper.getCenterDS());
//        prop.put("prjDBip", logip);
//        prop.put("prjDBport", logport);
//        prop.put("prjDBusername", logusername);
//        prop.put("prjDBpassword", logpassword);
//        prop.put("prjDBname", logdbname);
//
//        return prop;
//    }

    @Scheduled(initialDelay=20000,fixedRate=10000)
    public void sched()  {
        try {
            refreshJobs();
        } catch (SchedulerException e) {
            logger.error("start quartz fail",e);
        }

        if (taskIdQueue.size() > 0) {
            etlTrans.execute(taskIdQueue.poll());
        }

    }

    @PostConstruct
    public void init() {
        try {
            scheduler= StdSchedulerFactory.getDefaultScheduler();
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
