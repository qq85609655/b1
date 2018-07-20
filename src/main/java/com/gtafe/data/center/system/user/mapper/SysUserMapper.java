package com.gtafe.data.center.system.user.mapper;

import com.gtafe.data.center.system.role.vo.RoleVo;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface SysUserMapper extends BaseMapper {
    /**
     * @param state
     * @param orgIdList
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<SysUserVo> queryList(@Param("isAdmin") boolean isAdmin, @Param("keyWord") String keyWord, @Param("state") int state, @Param("orgIdList") List<String> orgIdList,
                              @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    /**
     * @param userNo
     * @return
     */
    SysUserVo getUserVoByUserNo(@Param("userNo") String userNo);

    SysUserVo getUserVoByuserId(String userId);

    /**
     * @param vo
     * @return
     */
    boolean saveEntity(SysUserVo vo);

    /**
     * @param userId
     * @return
     */
    boolean deleteEntity(@Param("userId") String userId);

    /**
     * @param userVo
     * @param type   1更新信息，2更新状态，3更新密码 4 更改 是否发邮件
     * @return
     */
    boolean updateEntity(@Param("userVo") SysUserVo userVo, @Param("type") int type);


    /**
     * 保存用户 和角色关系
     *
     * @param userId
     * @param roleIds
     * @return
     */
    boolean saveUserRole(@Param("userId") String userId, @Param("roleIds") List<Integer> roleIds);

    /**
     * 删除用户和角色关系
     *
     * @param userId
     * @return
     */
    @Delete("delete from sys_user_role where user_id=#{userId}")
    boolean deleteUserRole(@Param("userId") String userId);

    /**
     * 根据userId 查询所有角色id
     *
     * @param userId
     * @return
     */
    @Select("select role_id from sys_user_role where user_id=#{userId}")
    List<Integer> queryRoleListByUserId(@Param("userId") String userId);

    @Select("SELECT\n" +
            "\t CONCAT(u.realname, '(',u.user_no, ')' ) realName,\n" +
            "\to.org_name orgName\n" +
            "FROM\n" +
            "\tsys_user u,\n" +
            "\tsys_user_role r,\n" +
            "\tsys_org o\n" +
            "WHERE\n" +
            "\tu.user_id = r.user_id\n" +
            "AND u.org_id = o.id\n" +
            "AND r.role_id = #{roleId} ")
    List<SysUserVo> queryList4Role(@Param("roleId") int roleId);

    @Select("SELECT\n" +
            "\tu.*\n" +
            "FROM\n" +
            "\tsys_user u,\n" +
            "\tsys_org o\n" +
            "WHERE\n" +
            "\tu.org_id = o.id\n" +
            "AND u.isdelete = 0\n" +
            "AND u.user_no <>'admin' \n" +
            "ORDER BY\n" +
            "\tu.updatetime DESC ")
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "org_id", property = "orgId"),
            @Result(column = "user_no", property = "userNo"),
            @Result(column = "realname", property = "realName"),
            @Result(column = "email", property = "email"),
            @Result(column = "sex", property = "sex"),
            @Result(column = "user_type", property = "userType"),
            @Result(column = "user_id", property = "roles", many = @Many(select = "com.gtafe.data.center.system.user.mapper.SysUserMapper.getRoleByUserId", fetchType = FetchType.EAGER))
    })
    List<SysUserVo> getAllUser();

    @Select("select  role_id  roleId,role_name roleName  from sys_role WHERE  role_id  in (select  role_id from  sys_user_role where  user_id=#{userId})")
    List<RoleVo> getRoleByUserId(String userId);

    @Delete("delete from sys_user_task where user_id=#{userId} and bus_type=#{busType}")
    void deleteUserTasks(@Param("userId") String userId, @Param("busType") int busType);

    void saveUserTasks(@Param("busType") int busType, @Param("userId") String userId, @Param("taskList") List<Integer> taskList);

    @Select("select t.* from sys_user t where t.org_id=#{orgId} and t.isdelete=0 ")
    List<SysUserVo> queryListByOrgId(@Param("orgId") String id);

    @Select("select count(1) from sys_user where user_no=#{userNo} and isdelete=0")
    int queryCountByUserNo(@Param("userNo") String userNo);
}
