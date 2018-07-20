package com.gtafe.data.center.system.role.service;

import java.util.List;

import com.gtafe.data.center.system.role.vo.RoleInfoVo;
import com.gtafe.data.center.system.role.vo.RoleVo;
import com.gtafe.data.center.system.role.vo.TempUserRoleVo;

/*
角色接口定义
 */
public interface IRoleService {

    /**
     * 保存角色
     *
     * @param roleVo
     * @return
     */
    boolean saveRole(RoleInfoVo roleVo);

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    boolean deleteEntity(Integer roleId);

    /**
     * 修改角色
     *
     * @param roleVo
     * @return
     */
    boolean updateRole(RoleInfoVo roleVo);

    /**
     * 修改角色状态
     * @param roleId
     * @param checked
     * @return
     */
    boolean updateState(int roleId, boolean checked);
    /**
     * 查询列表
     * @param roleName
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<RoleVo> queryList(String roleName, int state, int pageNum, int pageSize);
    /**
     * 根据userId 获取用户已有角色ids 和 所有角色list
     *
     * @param userId
     * @return
     */
    TempUserRoleVo queryAllByUserId(String userId);
    
    RoleInfoVo getRoleInfoVo(int roleId);

    void clearUserRole(String userId);

}
