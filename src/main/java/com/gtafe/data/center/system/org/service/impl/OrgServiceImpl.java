package com.gtafe.data.center.system.org.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.data.center.system.log.vo.LogVo;
import com.gtafe.data.center.system.org.mapper.OrgMapper;
import com.gtafe.data.center.system.org.service.IOrgService;
import com.gtafe.data.center.system.org.vo.OrgVo;
import com.gtafe.data.center.system.user.mapper.SysUserMapper;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.LogUtil;

@Service
public class OrgServiceImpl extends BaseController implements IOrgService {
    private static int ORG = 14;

    @Autowired
    private OrgMapper orgMapper;


    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private LogService logServiceImpl;

    @Override
    public boolean checkOrgNo(String orgNo) {
        int count = this.orgMapper.queryByOrgNo(orgNo);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkOrgName(String orgName, int pId, int nodeType) {
        int count = this.orgMapper.queryByOrgName(orgName, pId, nodeType);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<OrgVo> queryList(String orgName, String state, String parentId, String orgType, int pageNum, int pageSize) {
        return orgMapper.queryList(orgName, state, parentId, orgType, pageNum, pageSize);
    }

    @Override
    public OrgVo getEntityById(int orgId) {
        return orgMapper.getEntityById(orgId);
    }

    @Override
    public OrgVo orgTree() {
        int userId = this.isAdmin() ? -1 : this.getUserId();
        List<OrgVo> orgVos = orgMapper.getOrgVos(userId);
        OrgVo rootOrg = new OrgVo();
        for (OrgVo orgVo1 : orgVos) {
            for (OrgVo orgVo2 : orgVos) {
                if (orgVo1.getId() == orgVo2.getParentId()) {
                    orgVo1.getChildren().add(orgVo2);
                }
            }
            if (orgVo1.getParentId() == 0) {
                rootOrg = orgVo1;
            }
        }
        return rootOrg;
    }

    @Override
    public OrgVo orgShowTree() {
        OrgVo root = this.orgTree();
        if (root != null) {
            root.removeEmptys();
        }
        return root;
    }

    @Override
    public List<Integer> getUserAuthOrgIds(int userId) {
        SysUserVo user = userMapper.getUserVoByuserId(userId);
        if (user == null) {
            return new ArrayList<Integer>();
        }
        if (this.isAdmin(user)) {
            userId = -1;
        }
        List<OrgVo> orgVos = orgMapper.getOrgVos(userId);
        List<Integer> result = new ArrayList<Integer>();
        for (OrgVo vo : orgVos) {
            if (vo.getNodeType() == 3) {
                result.add(vo.getId());
            }
        }
        return result;
    }

    @Override
    public boolean insertOrg(OrgVo org) {
        orgMapper.insertOrg(org);
        org.setSort(org.getId());
        boolean result = orgMapper.updateSort(org);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Org);
        logInfo.setOperType("新增");
        logInfo.setOperContent("新增机构信息:" + org.getOrgNo() + "/" + org.getOrgName());
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }

    @Override
    public boolean deleteById(int id) {
        //需要验证该机构下是否存在有用的用户
        List<SysUserVo> userVos = this.userMapper.queryListByOrgId(id);
        if (userVos.size() > 0) {
            throw new OrdinaryException("当前机构下存在用户，不可删除");
        }
        boolean result = orgMapper.deleteById(id);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Org);
        logInfo.setOperType("删除");
        logInfo.setOperContent("删除机构信息:" + id);
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }

    @Override
    public OrgVo getOrgVoById(int id) {
        OrgVo orgVo = orgMapper.getOrgVoById(id);
        if (orgVo.getParentId() != 0) {
            orgVo.setParentName(orgMapper.getOrgVoById(orgVo.getParentId()).getOrgName());
        }
        return orgVo;
    }

    @Override
    public boolean updateOrg(OrgVo org) {
        OrgVo dbVo = orgMapper.getOrgVoById(org.getId());
        if (dbVo == null) {
            throw new OrdinaryException("机构不存在，或已被删除！");
        }
        boolean result = orgMapper.updateOrg(org);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Org);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改机构信息:" + org.getOrgNo() + "/" + (dbVo.getOrgName().equals(org.getOrgName()) ? "" : dbVo.getOrgName() + "=>") + org.getOrgName());
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }

    @Override
    public String getOrgNo(int parentId) {
        OrgVo orgVo = orgMapper.getOrgVoById(parentId);
        List<OrgVo> subOrgs = orgMapper.getSubOrg(parentId);
        int count = subOrgs.size() + 1;
        String newOrgNo;

        loop:
        while (true) {
            if (count < 10) {
                newOrgNo = orgVo.getOrgNo() + "0" + count;
            } else {
                newOrgNo = orgVo.getOrgNo() + count;
            }
            for (OrgVo org : subOrgs) {
                if (org.getOrgNo().equals(newOrgNo)) {
                    count++;
                    continue loop;
                }
            }
            break;
        }
        return newOrgNo;

    }

    @Override
    public void updateSort(int id, boolean up) {
        List<OrgVo> orgVos = orgMapper.getSameOrg(id);
        if (up) {
            for (int i = 0; i < orgVos.size(); i++) {
                if (orgVos.get(i).getId() == id) {
                    if (i < 1) {
                        return;
                    }
                    int tempsort = orgVos.get(i).getSort();
                    orgVos.get(i).setSort(orgVos.get(i - 1).getSort());
                    orgVos.get(i - 1).setSort(tempsort);
                    orgMapper.updateSort(orgVos.get(i));
                    orgMapper.updateSort(orgVos.get(i - 1));
                    break;
                }
            }
        } else {
            for (int i = 0; i < orgVos.size(); i++) {
                if (orgVos.get(i).getId() == id) {
                    if (i == orgVos.size() - 1) {
                        return;
                    }
                    int tempsort = orgVos.get(i).getSort();
                    orgVos.get(i).setSort(orgVos.get(i + 1).getSort());
                    orgVos.get(i + 1).setSort(tempsort);
                    orgMapper.updateSort(orgVos.get(i));
                    orgMapper.updateSort(orgVos.get(i + 1));
                    break;
                }
            }
        }
    }
}
