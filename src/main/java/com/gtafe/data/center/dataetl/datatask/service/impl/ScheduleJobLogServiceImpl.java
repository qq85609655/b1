package com.gtafe.data.center.dataetl.datatask.service.impl;

import com.gtafe.data.center.dataetl.datatask.service.ScheduleJobLogService;
import com.gtafe.framework.base.schedule.ScheduleJobLogEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务日志实现类
 */
@Service
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {


	/**
	 * 根据ID，查询定时任务日志
	 */
	@Override
	public ScheduleJobLogEntity queryObject(Long jobId) {
		return new ScheduleJobLogEntity();
		/*return scheduleJobLogDao.queryObject(jobId);*/
	}

	/**
	 * 查询定时任务日志列表
	 */
	@Override
	public List<ScheduleJobLogEntity> queryList(Map<String, Object> map) {
	//	return scheduleJobLogDao.queryList(map);
		return new ArrayList<ScheduleJobLogEntity>();
	}

	/**
	 * 查询总数
	 */
	@Override
	public int queryTotal(Map<String, Object> map) {
		return 1;
		//return scheduleJobLogDao.queryTotal(map);
	}

	/**
	 * 保存定时任务日志
	 */
	@Override
	public void save(ScheduleJobLogEntity log) {
		//scheduleJobLogDao.save(log);
	}
}
