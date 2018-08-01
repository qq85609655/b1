package com.gtafe.data.center.dataetl.datatask.service.impl;

import com.framework.entity.ScheduleJobEntity;
import com.gtafe.data.center.dataetl.datatask.service.ScheduleJobService;
import com.gtafe.data.center.dataetl.datatask.vo.Constant;
import com.gtafe.framework.base.schedule.ScheduleUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 定时任务实现类
 */
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {
	@Autowired
	private Scheduler scheduler;
//	@Autowired
	//private ScheduleJobDao schedulerJobDao;

	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init() {
	/*	List<ScheduleJobEntity> scheduleJobList = schedulerJobDao.queryList(new HashMap<String, Object>());
		for (ScheduleJobEntity scheduleJob : scheduleJobList) {
			CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
			// 如果不存在，则创建
			if (cronTrigger == null) {
				ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
			} else {
				ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
			}
		}*/
	}

	/**
	 * 根据ID，查询定时任务
	 */
	@Override
	public ScheduleJobEntity queryObject(Long jobId) {
	//	return schedulerJobDao.queryObject(jobId);
		return  new ScheduleJobEntity();
	}

	/**
	 * 查询定时任务列表
	 */
	@Override
	public List<ScheduleJobEntity> queryList(Map<String, Object> map) {
		//return schedulerJobDao.queryList(map);
		return new ArrayList<ScheduleJobEntity>();
	}

	/**
	 * 查询总数
	 */
	@Override
	public int queryTotal(Map<String, Object> map) {
	//	return schedulerJobDao.queryTotal(map);
		return 1;
	}

	/**
	 * 保存定时任务
	 */
	@Override
	@Transactional
	public void save(ScheduleJobEntity scheduleJob) {
		scheduleJob.setCreateTime(new Date());
		scheduleJob.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
	//	schedulerJobDao.save(scheduleJob);

		ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
	}

	/**
	 * 更新定时任务
	 */
	@Override
	@Transactional
	public void update(ScheduleJobEntity scheduleJob) {
		ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);

		//schedulerJobDao.update(scheduleJob);
	}

	/**
	 * 批量删除定时任务
	 */
	@Override
	@Transactional
	public void deleteBatch(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.deleteScheduleJob(scheduler, jobId);
		}

		// 删除数据
		//schedulerJobDao.deleteBatch(jobIds);
	}

	/**
	 * 批量更新定时任务状态
	 */
	@Override
	public int updateBatch(Long[] jobIds, int status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", jobIds);
		map.put("status", status);
	//	return schedulerJobDao.updateBatch(map);
		return 1;
	}

	/**
	 * 立即执行
	 */
	@Override
	@Transactional
	public void run(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.run(scheduler, queryObject(jobId));
		}
	}

	/**
	 * 暂停运行
	 */
	@Override
	@Transactional
	public void pause(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.pauseJob(scheduler, jobId);
		}

	//	updateBatch(jobIds, ScheduleStatus.PAUSE.getValue());
	}

	/**
	 * 恢复运行
	 */
	@Override
	@Transactional
	public void resume(Long[] jobIds) {
		for (Long jobId : jobIds) {
	  	ScheduleUtils.resumeJob(scheduler, jobId);
		}

		//updateBatch(jobIds, ScheduleStatus.NORMAL.getValue());
	}
}
