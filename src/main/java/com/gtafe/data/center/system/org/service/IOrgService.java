package com.gtafe.data.center.system.org.service;

import java.util.List;

import com.gtafe.data.center.system.org.vo.OrgVo;

public interface IOrgService {

    /**
     * 查询列表
     *
     * @param orgName
     * @param state
     * @param parentId
     * @param orgType
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<OrgVo> queryList(String orgName, String state, String parentId, String orgType, int pageNum, int pageSize);


    /**
     * 根据 id 获取详情
     *
     * @param orgId
     * @return
     */
    OrgVo getEntityById(String orgId);


    OrgVo orgTree();

    /**
     * 过滤非叶子节点的空文件夹
     *
     * @author 汪逢建
     * @date 2017年11月1日
     */
    OrgVo orgShowTree();

    /**
     * 返回用户有权限的所有机构，不包含第一第二级机构
     *
     * @author 汪逢建
     * @date 2017年12月1日
     */
    List<String> getUserAuthOrgIds(String userId);

    boolean insertOrg(OrgVo org);

    boolean deleteById(String id);

    OrgVo getOrgVoById(String id);

    boolean updateOrg(OrgVo org);

    String getOrgNo(String parentId);

    void updateSort(String id, boolean up);

    boolean checkOrgName(String orgName, String pId, int nodeType);

    boolean checkOrgNo(String orgNo);


}
