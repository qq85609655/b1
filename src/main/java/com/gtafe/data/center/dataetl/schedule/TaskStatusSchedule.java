package com.gtafe.data.center.dataetl.schedule;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.schedule.mapper.EtlMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.mapper.KettleLogMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;
import com.gtafe.data.center.runadmin.nodewatch.vo.EtlTaskStatus;
import com.gtafe.framework.base.utils.StringUtil;

/**
 * 内部定时任务 扫描task 的状态
 */
public class TaskStatusSchedule {

	@Autowired
	EtlMapper etlMapper;

	@Autowired
	private KettleLogMapper kettleLogMapper;

	@Resource
	private DatasourceMapper datasourceMapper;
	Logger logger = LoggerFactory.getLogger(EtlSchedule.class);
	
	@Autowired
	private DataTaskMapper dataTaskMapper;
	

	/**
	 * 定时任务 每十分钟扫描一次转换任务： 针对机器和运行任务进行组装结果
	 * 
	 */
	@Scheduled(cron = "0 0/10 * * * ? *")
	public void refrashTaskStatus() {
		
		logger.info("開始執行任務....");
		// 清空状态表数据
		etlMapper.cleanAllStatus();
		// 扫描所有有效的转换任务
		List<DataTaskVo> dataTaskVolist = etlMapper.getAllTask();
		List<DatasourceVO> centerList = this.datasourceMapper.queryCenterData();
		// 中心库
		DatasourceVO centerVo = centerList.get(0);
		int centerStatus = 2;
		int thirdStatus = 2;
		ConnectDB tDb = StringUtil.getEntityBy(centerVo);
		Connection connection = null;
		try {
			connection = tDb.getConn();
			if (connection != null) {
				centerStatus = 1;// ok
			} else {
				boolean machineFlag = StringUtil.ping(centerVo.getHost(), 1, 2);
				if (machineFlag) {
					centerStatus = 2; // database is ok
				} else {
					centerStatus = 3; // all is error
				}
			}
		} finally {
			// close connection
			tDb.closeDbConn(connection);
		}

		// 1:首先判断是否数据库能取得链接 如果可以 则机器也ok了，然后需要读取转换日志，获取最近一次异常日志
		// 如果没有异常日志，则显示 一起正常 
		for (DataTaskVo vo : dataTaskVolist) {
			EtlTaskStatus etlTaskStatus = new EtlTaskStatus();
			etlTaskStatus.setTaskId(vo.getTaskId());
			etlTaskStatus.setBusType(vo.getBusinessType());
			etlTaskStatus.setOrgId(vo.getOrgId());
			StringBuffer error = new StringBuffer("");
			StringBuffer sourceDesc = new StringBuffer("");
			StringBuffer targetDesc = new StringBuffer("");
			if (vo.getBusinessType() == 1) {
				etlTaskStatus.setSourceTableName(vo.getThirdTablename());
				etlTaskStatus.setTagertTableName(vo.getCenterTablename());
			} else {
				etlTaskStatus.setSourceTableName(vo.getCenterTablename());
				etlTaskStatus.setSourceTableName(vo.getThirdTablename());
			}

			DatasourceVO dvo = this.datasourceMapper.queryDatasourceInfoById(vo.getThirdConnectionId());
			tDb = StringUtil.getEntityBy(dvo);
			connection = null;
			try {
				connection = tDb.getConn();
				if (connection != null) {
					thirdStatus = 1;// ok
				} else {
					boolean machineFlag = StringUtil.ping(dvo.getHost(), 1, 2);
					if (machineFlag) {
						thirdStatus = 2; // database is ok
					} else {
						thirdStatus = 3; // all is error
					}
				}
			} finally {
				// close connection
				tDb.closeDbConn(connection);
			}

			List<KettleLogVO> kettleLogVOS = this.kettleLogMapper.queryLstErrorList(vo.getTaskId());
			if (kettleLogVOS != null && kettleLogVOS.size() > 0) {
				KettleLogVO vo2 = kettleLogVOS.get(0);
				if (vo2.getErrors() > 0) {
					error.append("[" + vo.getTaskName() + "]任务运行异常！最后一次执行时间为" + vo2.getLogdate() + ",执行的错误信息为："
							+ vo2.getLog_field());
				}
			}
			//訂閲
			if (vo.getBusinessType() == 2) {
				if (centerStatus == 1) {
					sourceDesc.append("源数据库正常!");
				} else if (centerStatus == 3) {
					sourceDesc.append("源数据库不通!源数据库服务器异常，无法连接!");
				} else {
					sourceDesc.append("源数据库连接异常!");
				}

				if (thirdStatus == 1) {
					targetDesc.append("目标数据库正常!");
				} else if (thirdStatus == 3) {
					targetDesc.append("目标数据库不通!目标数据库服务器异常，无法连接!");
				} else {
					targetDesc.append("目标数据库连接异常!");
				}
				etlTaskStatus.setSourceStatus(centerStatus);
				etlTaskStatus.setSourceStatusName(sourceDesc.append(error).toString());
				etlTaskStatus.setTargetStatus(thirdStatus);
				etlTaskStatus.setTagertTableName(targetDesc.append(error).toString());
				etlTaskStatus.setSourceConnectionId(centerVo.getId());
				etlTaskStatus.setTargetConnectionId(vo.getThirdConnectionId());
			} else {
				//發佈
				if (centerStatus == 1) {
					sourceDesc.append("目标数据库正常!");
				} else if (centerStatus == 3) {
					sourceDesc.append("目标数据库不通!目标数据库服务器异常，无法连接!");
				} else {
					sourceDesc.append("目标数据库连接异常!");
				}

				if (thirdStatus == 1) {
					targetDesc.append("源数据库正常!");
				} else if (thirdStatus == 3) {
					targetDesc.append("源数据库不通!源数据库服务器异常，无法连接!");
				} else {
					targetDesc.append("源数据库连接异常!");
				}
				etlTaskStatus.setTargetStatus(centerStatus);
				etlTaskStatus.setTagertTableName(sourceDesc.append(error).toString());
				etlTaskStatus.setSourceStatus(thirdStatus);
				etlTaskStatus.setSourceStatusName(targetDesc.append(error).toString());
				etlTaskStatus.setSourceConnectionId(vo.getThirdConnectionId());
				etlTaskStatus.setTargetConnectionId(centerVo.getId());
			}
 
			dataTaskMapper.saveEtlTaskStatus(etlTaskStatus); 
		}

		logger.info("任務....完畢,共計掃描任務:"+dataTaskVolist.size()+"條");

	}
}
