package com.gtafe.data.center.runadmin.nodewatch.service;

import java.util.List;

import com.gtafe.data.center.common.common.vo.IndexVo;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.runadmin.nodewatch.vo.NodeWatchVo;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description:
 */
public interface NodeWatchService {
    List<NodeWatchVo> list(List<DataTaskVo> mappingVOList);

    IndexVo queryTaskRunStatus();
}
