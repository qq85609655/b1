package com.gtafe.data.center.runadmin.alertpush.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.runadmin.alertpush.mapper.AlertPushMapper;
import com.gtafe.data.center.runadmin.alertpush.service.AlertPushService;
import com.gtafe.data.center.runadmin.alertpush.vo.AlertPush;
import com.gtafe.data.center.system.org.service.IOrgService;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.utils.StringUtil;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description:
 */
@Service
public class AlertPushServiceImpl extends BaseController implements AlertPushService {
    @Resource
    private AlertPushMapper alertPushMapper;

    @Resource
    private DataTaskMapper dataTaskMapper;
    @Autowired
    private IOrgService orgServiceImpl;

    @Override
    public List<AlertPush> list(int pageNum, int pageSize, int isPush, String orgIds) {
        List<String> orgIdList = StringUtil.splitListString(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, AlertPush.class);
        }
        return this.alertPushMapper.list(pageNum, pageSize, isPush, orgIdList);
    }

    @Override
    public List<DataTaskVo> queryMappingVosByOrg(int businessType) {
        //查询当前登录用户有权限的资源任务
        List<String> orgIds = orgServiceImpl.getUserAuthOrgIds(this.getUserId());
        return dataTaskMapper.queryListByOrgs(orgIds, businessType);
    }

    @Override
    public List<Integer> queryUserMapTaskIds(String userId, int businessType) {
        return this.alertPushMapper.queryUserMapTaskIds(userId, businessType);
    }
}
