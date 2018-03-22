/**
 * Project Name:gtacore
 * File Name:	I<#%modlue%#>Service.java
 * Description:
 * Date: 2017-08-14 17:52:20
 * Author: Xiang Zhiling
 * History:
 * Copyright (c) 2017, GTA All Rights Reserved.
 */
package com.gtafe.data.center.dataetl.datatask.service;

import java.util.List;

import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;

public interface DataTaskService {

    /**
     * 查询列表
     * @author 汪逢建
     * @date 2017年11月15日
     * @param collectionId 0查询全部 大于0查询相关数据库链接的
     */
    List<DataTaskVo> queryList(int collectionId, String orgIds, Integer dataTypeValue, Integer status,
                               String name, int pageNum, int pageSize, Integer businessType);

    /**
     * 获取单个任务基本信息
     * @author 汪逢建
     * @date 2017年11月16日
     */
    DataTaskVo getDataTaskVo(Integer taskId, boolean stepsFlag);
    /**
     * 添加任务
     * @author 汪逢建
     * @date 2017年11月16日
     */
    int insertDataTaskVo(int businessType, DataTaskVo taskVO);
    
    /**
     * 更新任务
     * @author 汪逢建
     * @date 2017年11月16日
     */
    boolean updateDataTaskVo(int businessType, DataTaskVo taskVO);

    boolean batchUpdateState(String taskIds, int state);

    boolean deleteTasks(List<Integer> taskIds);
    
    boolean startNow(int taskId);

    boolean startLocalKettleNow();

    List<TransFileVo> queryKfileList(String fileType, String fileName, int pageNum, int pageSize);

    boolean runItem(int fileId);
}
