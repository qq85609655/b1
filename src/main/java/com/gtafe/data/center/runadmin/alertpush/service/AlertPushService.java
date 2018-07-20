package com.gtafe.data.center.runadmin.alertpush.service;

import java.util.List;

import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.runadmin.alertpush.vo.AlertPush;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description:
 */
public interface AlertPushService {
    /**
     * 根据参数查询 列表数据
     *
     * @param pageNum
     * @param pageSize
     * @param isPush
     * @param orgIds
     * @return
     */
    List<AlertPush> list(int pageNum, int pageSize, int isPush, String orgIds);

    /**
     * 根据机构编号 和 业务类型查找当前机构 拥有的 发布数据资源 和  订阅数据资源 任务
     *
     * @return
     */
    List<DataTaskVo> queryMappingVosByOrg(int businessType);

    /**
     * 根据userid 查询用户任务列表
     *
     * @param userId
     * @return
     */
    List<Integer> queryUserMapTaskIds(String userId, int businessType);
}
