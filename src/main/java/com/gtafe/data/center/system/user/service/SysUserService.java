package com.gtafe.data.center.system.user.service;

import com.gtafe.data.center.system.user.vo.SysUserVo;

import java.util.List;

public interface SysUserService {

    /**
     *
     * @param keyWord
     * @param state
     * @param orgIds
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<SysUserVo> queryList(String keyWord, int state, String orgIds, int pageNum, int pageSize);

    /**
     * 获取用户信息
     * @param userNo
     * @return
     */
    public SysUserVo getUserVoByUserNo(String userNo);

    /**
     * 保存用户信息
     *
     * @param vo
     * @return
     */
    boolean saveEntity(SysUserVo vo);

    /**
     * 删除用户信息
     *
     * @param ids
     * @return
     */
    boolean deleteEntity(Integer ids);

    /**
     * 修改用户信息
     *
     * @param vo
     * @return
     */
    boolean updateEntity(SysUserVo vo, boolean updateMy);

    /**
     * 修改密碼
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    boolean updatePwd(String oldPwd, String newPwd);

    /**
     * 根据用户名和邮箱去检束是否存在此人
     *
     * @param email
     * @param userNo
     * @return
     */
    SysUserVo queryUserByEmailAndLoginName(String email, String userNo);

    /**
     * 保存用户和角色关系
     *
     *
     * @param userId
     * @param roleIds
     * @return
     */
    boolean saveUserRole(int userId, String roleIds);

    /**
     * 保存 用户  和 资源任务 关系
     *
     * @param busType
     * @param userId
     * @param taskIds
     * @return
     */
    boolean saveUserTasks(int busType, int userId, String taskIds);

    SysUserVo getUserVoByuserId(int userId);

    boolean updateStatus(SysUserVo vo);

    boolean resetPwd(SysUserVo vo);

    boolean updateSendStatus(SysUserVo vo);

    List<SysUserVo> queryList4Role(int roleId);

    boolean updatePwdForUserNo(String pwd, String confirmpwd, String userNo);

    boolean checkUserNo(String userNo);
}
