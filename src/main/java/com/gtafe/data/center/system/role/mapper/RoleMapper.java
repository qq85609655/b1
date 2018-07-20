package com.gtafe.data.center.system.role.mapper;

import com.gtafe.data.center.system.role.vo.RoleVo;
import com.gtafe.framework.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface RoleMapper extends BaseMapper {

    /**
     * @param roleName
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<RoleVo> queryList(@Param("roleName") String roleName, @Param("state") int state, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    int saveEntity(RoleVo vo);

    int deleteEntity(@Param("roleId") Integer roleId);

    int deleteEntityforRelation(@Param("roleId") Integer roleId);

    int updateEntity(RoleVo vo);

    @Select("SELECT role_id  roleId,role_name roleName,remark,if(state=1,true,false),ncount  from sys_role WHERE  role_id = #{roleId}")
    RoleVo getEntityById(@Param("roleId") int roleId);
    
    /**
     * 检查角色名称是否重复
     * @author 汪逢建
     * @date 2017年11月1日
     */
    int checkRoleNameRepeat(@Param("roleName") String roleName, @Param("roleId") Integer roleId);
       
    /**
     * 删除角色功能权限授权
     * @author 汪逢建
     * @date 2017年11月1日
     */
    int deleteRoleAuthcode(@Param("roleId") Integer roleId);
    
    /**
     * 添加角色功能权限授权
     * @author 汪逢建
     * @date 2017年11月1日
     */
    int insertRoleAuthcode(@Param("roleId") Integer roleId, @Param("authList")List<String> authList);
    
    /**
     * 删除角色组织权限授权
     * @author 汪逢建
     * @date 2017年11月1日
     */
    int deleteRoleOrg(@Param("roleId") Integer roleId);
    
    /**
     * 添加角色组织权限授权
     * @author 汪逢建
     * @date 2017年11月1日
     */
    int insertRoleOrg(@Param("roleId") Integer roleId, @Param("orgList")List<Integer> orgList);
    /**
     * 获取角色功能权限码
     * @author 汪逢建
     * @date 2017年11月1日
     */
    List<String> getRoleAuthcode(@Param("roleId") Integer roleId);
    /**
     * 获取角色组织权限
     * @author 汪逢建
     * @date 2017年11月1日
     */
    List<Integer> getRoleOrg(@Param("roleId") Integer roleId);
    
    @Select("SELECT role_id roleId, role_name roleName,remark,if(state=1,true,false),ncount  from sys_role  ")
    List<RoleVo> queryAll();

    @Delete("delete from sys_user_role where user_id=#{userId}")
    void clearUserRoles(@Param("userId") String userId);
}
