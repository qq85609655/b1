package com.gtafe.data.center.runadmin.nodewatch.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.schedule.mapper.EtlMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.mapper.KettleLogMapper;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;
import com.gtafe.data.center.runadmin.nodewatch.vo.EtlTaskStatus;
import com.gtafe.framework.base.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.gtafe.data.center.common.common.vo.IndexVo;
import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.runadmin.nodewatch.service.NodeWatchService;
import com.gtafe.data.center.runadmin.nodewatch.vo.NodeWatchVo;
import com.gtafe.data.center.system.org.service.IOrgService;
import com.gtafe.framework.base.controller.BaseController;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description: 检索 节点的 运行情况
 */
@Service
public class NodeWatchServiceImpl extends BaseController implements NodeWatchService {

    private static final Logger logger = LoggerFactory.getLogger(
            NodeWatchServiceImpl.class);
    @Resource
    private DataTaskMapper dataTaskMapper;
    @Autowired
    private IOrgService orgServiceImpl;


    @Override
    public List<EtlTaskStatus> queryTaskStatusList(String orgIds, int pageNum, int pageSize, int busType) {
        List<Integer> orgIdList = StringUtil.splitListInt(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, EtlTaskStatus.class);
        }
        return this.dataTaskMapper.queryTaskStatusList(orgIdList, pageNum, pageSize, busType);
    }

    /**
     * 根据数据任务资源 和业务类型 找其 源表 或 目标表 所在的数据库 运行 是否正常
     * <p>
     * //   * @param vlist
     *
     * @return
     */
   /* @Override
    public List<NodeWatchVo> list(List<DataTaskVo> vlist) {
        if (vlist.isEmpty()) {
            return new ArrayList<NodeWatchVo>();
        }
        List<EtlTaskStatus> etlTaskStatuses = this.dataTaskMapper.queryTaskStatusList(null, -1, -1);
        List<NodeWatchVo> nodeWatchVos = new ArrayList<NodeWatchVo>();
        for (EtlTaskStatus v : etlTaskStatuses) {
            NodeWatchVo vvv = new NodeWatchVo();
            vvv.setBusType(v.getBusType());
            vvv.setId(v.getTaskId());
            vvv.setOrgId(v.getOrgId() + "");
            vvv.setOrgName(v.getOrgName());
            vvv.setResourceName(v.getTaskName());
            vvv.setSourceTableName(v.getSourceTableName());
            vvv.setTagertTableName(v.getTagertTableName());
            int sourceFlag = v.getSourceStatus();
            int targetFlag = v.getTargetStatus();
            StringBuffer error = new StringBuffer("");
            if (sourceFlag == 1) {
                vvv.setStatus(sourceFlag);
                error.append(v.getSourceStatusName());
            } else if (sourceFlag == 3) {
                vvv.setStatus(sourceFlag);
                error.append(v.getSourceStatusName());
            } else {
                vvv.setStatus(sourceFlag);
                error.append(v.getSourceStatusName());
            }
            if (vvv.getStatus() == 1) {
                if (targetFlag == 1) {
                    vvv.setStatus(targetFlag);
                    error.append(v.getTargetStatusName());
                } else if (targetFlag == 3) {
                    vvv.setStatus(targetFlag);
                    error.append(v.getTargetStatusName());
                } else {
                    vvv.setStatus(targetFlag);
                    error.append(v.getTargetStatusName());
                }
            }
            vvv.setStatusName(error.toString());
            nodeWatchVos.add(vvv);
        }
        return nodeWatchVos;
    }*/
    @Override
    public IndexVo queryTaskRunStatus() {
        IndexVo result = new IndexVo();
        int successSize = 0;
        List<Integer> orgIds = orgServiceImpl.getUserAuthOrgIds(this.getUserId());
        if (orgIds.size() > 0) {
           /* List<DataTaskVo> list = dataTaskMapper.queryList(-1, orgIds, -1, null, 1, 19, -1);
            if (list.isEmpty()) {
                result.setTotalCounts(0);
                result.setErrorCounts(0);
                result.setNodeWatchVos(new ArrayList<NodeWatchVo>());
                return result;
            }*/
          /*  List<NodeWatchVo> nodeList = compactList(list);
            int size = nodeList.size();
            for (NodeWatchVo vo : nodeList) {
                if (vo.getStatus() == 1) {
                    successSize++;
                }
            }
            int errorSize = size - successSize;
            result.setNodeWatchVos(nodeList);*/

            List<EtlTaskStatus> statusList = this.dataTaskMapper.queryTaskStatusList(orgIds, -1, 19,0);
            if (statusList.isEmpty()) {
                result.setTotalCounts(0);
                result.setErrorCounts(0);
                // result.setNodeWatchVos(new ArrayList<NodeWatchVo>());
                result.setEtlTaskStatuses(new ArrayList<EtlTaskStatus>());
                return result;
            }
            int size = statusList.size();
            result.setEtlTaskStatuses(statusList);
            for (EtlTaskStatus vo : statusList) {
                if (vo.getSourceStatus() == 1 && vo.getTargetStatus() == 1) {
                    successSize++;
                }
            }
            int errorSize = size - successSize;
            result.setErrorCounts(errorSize);
            if (statusList instanceof Page) {
                @SuppressWarnings("resource")
                Page<?> page = (Page<?>) statusList;
                result.setTotalCounts((int) page.getTotal());
            } else {
                result.setTotalCounts(statusList.size());
            }
        } else {
            result.setTotalCounts(0);
            result.setErrorCounts(0);
            //  result.setNodeWatchVos(new ArrayList<NodeWatchVo>());
            result.setEtlTaskStatuses(new ArrayList<EtlTaskStatus>());
        }
        return result;
    }
    @Autowired
    EtlMapper etlMapper;

    @Autowired
    private KettleLogMapper kettleLogMapper;

    @Resource
    private DatasourceMapper datasourceMapper;
    @Override
    public String doRefrashTaskStatus() {
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
            System.out.println(vo.toString());
            if (vo.getBusinessType() == 1) {
                etlTaskStatus.setSourceTableName(vo.getThirdTablename());
                etlTaskStatus.setTagertTableName(vo.getCenterTablename());
            } else {
                etlTaskStatus.setSourceTableName(vo.getCenterTablename());
                etlTaskStatus.setTagertTableName(vo.getThirdTablename());
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
                etlTaskStatus.setSourceStatusName(sourceDesc.toString());
                etlTaskStatus.setTargetStatus(thirdStatus);
                etlTaskStatus.setTargetStatusName(targetDesc.toString());
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
                etlTaskStatus.setTargetStatusName(sourceDesc.toString());
                etlTaskStatus.setSourceStatus(thirdStatus);
                etlTaskStatus.setSourceStatusName(targetDesc.toString());
                etlTaskStatus.setSourceConnectionId(vo.getThirdConnectionId());
                etlTaskStatus.setTargetConnectionId(centerVo.getId());
            }

            etlTaskStatus.setErrorInfo(error.toString());
            etlTaskStatus.setTaskName(vo.getTaskName());
            dataTaskMapper.saveEtlTaskStatus(etlTaskStatus);
        }
        return "Y";
    }

   /* public List<NodeWatchVo> compactList(List<DataTaskVo> vlist) {
        Map<Integer, Object[]> connMap = new HashMap<Integer, Object[]>();
        List<Integer> connIds = new ArrayList<Integer>();
        List<DatasourceVO> centerList = this.datasourceMapper.queryCenterData();
        DatasourceVO centerVo = centerList.get(0);
        connMap.put(centerVo.getId(), new Object[]{centerVo, -1});
        for (DataTaskVo vo : vlist) {
            connIds.add(vo.getThirdConnectionId());
        }
        List<DatasourceVO> thirdList = null;
        if (!connIds.isEmpty()) {
            thirdList = this.datasourceMapper.queryDatasourceByIds(connIds);
            for (DatasourceVO vo : thirdList) {
                connMap.put(vo.getId(), new Object[]{vo, -1});
            }
        }
        List<NodeWatchVo> list = new ArrayList<NodeWatchVo>();
        for (DataTaskVo vo : vlist) {
            NodeWatchVo vvo = new NodeWatchVo();
            String sourceTableName = "";
            String targetTableName = "";
            int sourceConnectionId = 0;
            int targetConnectionId = 0;
            //发布是1 订阅是2
            if (vo.getBusinessType() == 1) {
                sourceTableName = vo.getThirdTablename();
                targetTableName = vo.getCenterTablename();
                sourceConnectionId = vo.getThirdConnectionId();
                targetConnectionId = centerVo.getId();
            } else {
                targetTableName = vo.getThirdTablename();
                sourceTableName = vo.getCenterTablename();
                targetConnectionId = vo.getThirdConnectionId();
                sourceConnectionId = centerVo.getId();
            }
            int sourceFlag = StringUtil.checkConnection(connMap, sourceConnectionId);
            int targetFlag = StringUtil.checkConnection(connMap, targetConnectionId);
            vvo.setId(vo.getTaskId());
            vvo.setResourceName(vo.getTaskName());
            vvo.setSourceTableName(sourceTableName);
            vvo.setBusType(vo.getBusinessType());
            vvo.setTagertTableName(targetTableName);
            vvo.setOrgId(vo.getOrgId() + "");
            vvo.setOrgName(vo.getOrgName());
            StringBuffer error = new StringBuffer("");
            if (sourceFlag == 1) {
                vvo.setStatus(sourceFlag);
                error.append("源数据库正常!");
            } else if (sourceFlag == 3) {
                vvo.setStatus(sourceFlag);
                error.append("源数据库不通!源数据库服务器异常，无法连接!");
            } else {
                vvo.setStatus(sourceFlag);
                error.append("源数据库连接异常!");
            }
            if (vvo.getStatus() == 1) {
                if (targetFlag == 1) {
                    vvo.setStatus(targetFlag);
                    error.append("目标数据库正常!");
                } else if (targetFlag == 3) {
                    vvo.setStatus(targetFlag);
                    error.append("目标数据库不通!目标数据库服务器异常，无法连接!");
                } else {
                    vvo.setStatus(targetFlag);
                    error.append("目标数据库连接异常!");
                }
            }
            List<KettleLogVO> kettleLogVOS = this.kettleLogMapper.queryLstErrorList(vo.getTaskId());
            if (kettleLogVOS != null && kettleLogVOS.size() > 0) {
                KettleLogVO vo2 = kettleLogVOS.get(0);
                if (vo2.getErrors() > 0) {
                    vvo.setStatus(4);
                    error.append("[" + vo.getTaskName() + "]任务运行异常！最后一次执行时间为" + vo2.getLogdate() + ",执行的错误信息为：" + vo2.getLog_field());
                }
            }
            vvo.setStatusName(error.toString());
            list.add(vvo);
        }
        return list;
    } */
}
