package com.gtafe.data.center.system.role.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.datatask.vo.TaskStepVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.data.center.system.role.mapper.RoleMapper;
import com.gtafe.data.center.system.role.service.IRoleService;
import com.gtafe.data.center.system.role.vo.RoleInfoVo;
import com.gtafe.data.center.system.role.vo.RoleVo;
import com.gtafe.data.center.system.role.vo.TempUserRoleVo;
import com.gtafe.data.center.system.user.mapper.SysUserMapper;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;

@Service
public class RoleServiceImpl extends BaseController implements IRoleService {
    private static int ROLE = 15;

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private LogService logServiceImpl;
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public boolean saveRole(RoleInfoVo roleVo) {
        if (EmptyUtil.isEmpty(roleVo.getAuthList()) || EmptyUtil.isEmpty(roleVo.getOrgList())) {
            throw new OrdinaryException("授权信息不完整，请重新选择！");
        }
        if (roleMapper.checkRoleNameRepeat(roleVo.getRoleName(), null) > 0) {
            throw new OrdinaryException("角色名称已存在，请重新输入！");
        }
        RoleVo vo = new RoleVo();
        vo.setUpdater(this.getUserId());
        vo.setUpdateTime(new Date());
        vo.setCreateTime(vo.getUpdateTime());
        vo.setCreator(this.getUserId());
        vo.setRoleName(roleVo.getRoleName());
        vo.setRemark(roleVo.getRemark() == null ? "" : roleVo.getRemark());
        vo.setNcount(0);
        vo.setState(true);
        roleMapper.saveEntity(vo);
        roleMapper.insertRoleAuthcode(vo.getRoleId(), roleVo.getAuthList());
        roleMapper.insertRoleOrg(vo.getRoleId(), roleVo.getOrgList());
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Role);
        logInfo.setOperType("新增");
        logInfo.setOperContent("新增角色信息:" + roleVo.getRoleName());
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }




    @Override
    public boolean updateRole(RoleInfoVo roleVo) {
        if (EmptyUtil.isEmpty(roleVo.getAuthList()) || EmptyUtil.isEmpty(roleVo.getOrgList())) {
            throw new OrdinaryException("授权信息不完整，请重新选择！");
        }
        if (roleMapper.checkRoleNameRepeat(roleVo.getRoleName(), roleVo.getRoleId()) > 0) {
            throw new OrdinaryException("角色名称已存在，请重新输入！");
        }
        RoleVo dbVo = roleMapper.getEntityById(roleVo.getRoleId());
        if (dbVo == null) {
            throw new OrdinaryException("角色不存在，或已被删除！");
        }
        RoleVo vo = new RoleVo();
        vo.setRoleId(roleVo.getRoleId());
        vo.setUpdater(this.getUserId());
        vo.setUpdateTime(new Date());
        vo.setCreateTime(vo.getUpdateTime());
        vo.setCreator(this.getUserId());
        vo.setRoleName(roleVo.getRoleName());
        vo.setRemark(roleVo.getRemark() == null ? "" : roleVo.getRemark());
        vo.setState(roleVo.isState());
        roleMapper.updateEntity(vo);
        //更新授权
        roleMapper.deleteRoleAuthcode(vo.getRoleId());
        roleMapper.deleteRoleOrg(vo.getRoleId());
        roleMapper.insertRoleAuthcode(vo.getRoleId(), roleVo.getAuthList());
        roleMapper.insertRoleOrg(vo.getRoleId(), roleVo.getOrgList());
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Role);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改角色信息:" + (dbVo.getRoleName().equals(vo.getRoleName()) ? "" : dbVo.getRoleName() + "=>") + roleVo.getRoleName());
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean updateState(int roleId, boolean checked) {
        RoleVo vo = roleMapper.getEntityById(roleId);
        if (vo == null) {
            return false;
        }
        String roleName = vo.getRoleName();
        vo.setState(checked);
        vo.setUpdateTime(new Date());
        vo.setUpdater(this.getUserId());
        roleMapper.updateEntity(vo);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Role);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改角色状态:" + roleName + " " + (checked ? "启用" : "停用"));
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean deleteEntity(Integer roleId) {
        RoleVo vo = roleMapper.getEntityById(roleId);
        if (vo == null) return true;

        //先删除角色和人员关系 数据
        int a1 = roleMapper.deleteEntityforRelation(roleId);
        int a3 = roleMapper.deleteRoleAuthcode(roleId);
        int a2 = roleMapper.deleteEntity(roleId);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Role);
        logInfo.setOperType("删除");
        logInfo.setOperContent("删除角色信息:" + vo.getRoleName());
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public List<RoleVo> queryList(String roleName, int state, int pageNum, int pageSize) {
        return roleMapper.queryList(roleName, state, pageNum, pageSize);
    }

    @Override
    public TempUserRoleVo queryAllByUserId(String userId) {
        TempUserRoleVo tempUserRoleVo = new TempUserRoleVo();
        List<Integer> sysUserRoleVoIds = this.sysUserMapper.queryRoleListByUserId(userId);
        List<RoleVo> roleVos = this.roleMapper.queryAll();
        tempUserRoleVo.setList(roleVos);
        tempUserRoleVo.setHasRoleIds(sysUserRoleVoIds);
        return tempUserRoleVo;
    }

    @Override
    public RoleInfoVo getRoleInfoVo(int roleId) {
        RoleVo vo = this.roleMapper.getEntityById(roleId);
        if (vo == null) {
            throw new OrdinaryException("角色不存在，已被删除！");
        }
        RoleInfoVo result = new RoleInfoVo();
        result.setRoleId(roleId);
        result.setRoleName(vo.getRoleName());
        result.setRemark(vo.getRemark());
        result.setAuthList(this.roleMapper.getRoleAuthcode(roleId));
        result.setOrgList(this.roleMapper.getRoleOrg(roleId));
        return result;
    }

    @Override
    public void clearUserRole(String userId) {
        this.roleMapper.clearUserRoles(userId);
    }


}
