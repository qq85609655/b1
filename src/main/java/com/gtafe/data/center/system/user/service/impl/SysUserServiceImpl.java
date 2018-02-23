package com.gtafe.data.center.system.user.service.impl;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.common.login.controller.LoginController;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.EmailSendLog;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.data.center.system.log.vo.LogVo;
import com.gtafe.data.center.system.role.service.IRoleService;
import com.gtafe.data.center.system.role.vo.RoleInfoVo;
import com.gtafe.data.center.system.user.mapper.SysUserMapper;
import com.gtafe.data.center.system.user.service.SysUserService;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.DateUtil;
import com.gtafe.framework.base.utils.LogUtil;
import com.gtafe.framework.base.utils.MailSender;
import com.gtafe.framework.base.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SysUserServiceImpl extends BaseController implements SysUserService {

    private static int USER = 13;

    @Autowired
    MailSender mailSender;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private LogService logServiceImpl;
    @Resource
    private IRoleService roleServiceImpl;
    @Resource
    private DataTaskMapper dataTaskMapper;

    @Override
    public List<SysUserVo> queryList(String keyWord, int state, String orgIds, int pageNum, int pageSize) {
        List<Integer> orgIdList = StringUtil.splitListInt(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, SysUserVo.class);
        }
        return this.sysUserMapper.queryList(this.isAdmin(), keyWord, state, orgIdList, pageNum, pageSize);
    }


    @Override
    public SysUserVo getUserVoByUserNo(String userNo) {
        return this.sysUserMapper.getUserVoByUserNo(userNo);
    }

    @Override
    public SysUserVo getUserVoByuserId(int userId) {
        return this.sysUserMapper.getUserVoByuserId(userId);
    }

    @Override
    public boolean updateStatus(SysUserVo userVo) {
        SysUserVo dbVo = this.checkSuperAdminUpdate(userVo.getUserId());
        if (dbVo == null) {
            return true;
        }
        if (isAdmin(dbVo)) {
            throw new OrdinaryException("超级管理员不能修改状态！");
        }
        userVo.setUpdater(getUserId());
        userVo.setUpdateTime(new Date());
        boolean result = this.sysUserMapper.updateEntity(userVo, 2);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改用户状态:" + dbVo.getRealName() + "(" + dbVo.getUserNo() + ") " + (userVo.isState() ? "启用" : "停用"));
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }


    @Override
    public boolean updateSendStatus(SysUserVo userVo) {
        SysUserVo dbVo = sysUserMapper.getUserVoByuserId(userVo.getUserId());
        if (dbVo == null) {
            return true;
        }
        userVo.setUpdater(getUserId());
        userVo.setUpdateTime(new Date());
        boolean result = this.sysUserMapper.updateEntity(userVo, 4);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改用户邮件状态:" + dbVo.getRealName() + "(" + dbVo.getUserNo() + ") " + (userVo.isState() ? "启用" : "停用"));
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }

    @Override
    public List<SysUserVo> queryList4Role(int roleId) {
        return this.sysUserMapper.queryList4Role(roleId);
    }

    @Resource
    private SysConfigService sysConfigServiceImpl;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);


    /**
     * 找回密码是，修改密码不验证超级管理员
     */
    @Override
    public boolean updatePwdForUserNo(String pwd, String confirmpwd, String userNo) {
        if (StringUtil.isBlank(pwd) || StringUtil.isBlank(confirmpwd) || StringUtil.isBlank(userNo)) {
            return false;
        }
        if (!pwd.toLowerCase().equals(confirmpwd.toLowerCase())) {
            return false;
        }
        SysUserVo vo = this.sysUserMapper.getUserVoByUserNo(userNo);
        if (vo == null) {
            return true;
        }
        String pwdmd = StringUtil.MD5(pwd);
        vo.setLoginPwd(pwdmd);
        vo.setUpdateTime(new Date());
        vo.setUpdater(vo.getUserId());
        this.sysUserMapper.updateEntity(vo, 3);
        SysConfigVo configVo = this.sysConfigServiceImpl.getBasicSysConfigVO();
        if (StringUtil.isNotBlank(vo.getEmail())) {
            try {
                boolean b3 = this.mailSender.sendEmail(vo.getEmail(), "密码修改提示", "[" + configVo.getSysName() + "]友情提示：尊敬的" + vo.getRealName() + "用户您好，您的密码已经被修改为" + pwd + ".请注意您的账号安全~~");
                LOGGER.info(b3 ? "邮件发送成功!" : "邮件发送失败!");
                if (!b3) {
                    EmailSendLog emailSendLog = new EmailSendLog();
                    emailSendLog.setModule(2);
                    emailSendLog.setSendTime(DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    emailSendLog.setUserNo(vo.getUserNo());
                    emailSendLog.setModuleName("忘记密码");
                    emailSendLog.setSendSuccess(0);
                    emailSendLog.setContent("用户【" + vo.getRealName() + "】-忘记密码邮件");
                    this.logServiceImpl.saveEmailSendLog(emailSendLog);
                    throw new OrdinaryException("密码修改异常，请联系管理员!");
                } else {
                    EmailSendLog emailSendLog = new EmailSendLog();
                    emailSendLog.setModule(1);
                    emailSendLog.setSendTime(DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    emailSendLog.setUserNo(vo.getUserNo());
                    emailSendLog.setModuleName("忘记密码");
                    emailSendLog.setSendSuccess(2);
                    emailSendLog.setContent("用户【" + vo.getRealName() + "】-忘记密码邮件");
                    this.logServiceImpl.saveEmailSendLog(emailSendLog);
                }
            } catch (Exception e) {
             //   throw new OrdinaryException("请检查邮件服务器配置...!");
            }
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("修改");
        logInfo.setOperContent("找回密码:" + vo.getRealName() + "(" + vo.getUserNo() + ") ");
        this.logServiceImpl.saveLogNoLogin(logInfo);
        return true;
    }

    @Override
    public boolean checkUserNo(String userNo) {
        int c = this.sysUserMapper.queryCountByUserNo(userNo);
        return c >= 1 ? true : false;
    }

    @Override
    public boolean resetPwd(SysUserVo userVo) {
        SysUserVo dbVo = this.checkSuperAdminUpdate(userVo.getUserId());
        if (dbVo == null) {
            throw new OrdinaryException("用户不存在，或已被删除！");
        }
        userVo.setUpdater(getUserId());
        userVo.setUpdateTime(new Date());
        this.sysUserMapper.updateEntity(userVo, 3);
        if (StringUtil.isNotBlank(userVo.getEmail())) {
            try {
                boolean res = this.mailSender.sendEmail(userVo.getEmail(), "密码重置", userVo.getRealName() + " 您好，您在【数据中心】的密码已经重置为系统默认密码123456，请立即登录系统修改密码！");
                LOGGER.info(res ? "发送邮件成功" : "发送邮件失败!");
                if (res) {
                    EmailSendLog emailSendLog = new EmailSendLog();
                    emailSendLog.setModule(1);
                    emailSendLog.setSendTime(DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    emailSendLog.setUserNo(this.getUserInfo().getUserNo());
                    emailSendLog.setModuleName("重置密码");
                    emailSendLog.setSendSuccess(1);
                    emailSendLog.setContent("用户【" + userVo.getRealName() + "】-密码重置邮件");
                    this.logServiceImpl.saveEmailSendLog(emailSendLog);
                } else {
                    EmailSendLog emailSendLog = new EmailSendLog();
                    emailSendLog.setModule(1);
                    emailSendLog.setSendTime(DateUtil.formatDateByFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
                    emailSendLog.setUserNo(this.getUserInfo().getUserNo());
                    emailSendLog.setModuleName("重置密码");
                    emailSendLog.setSendSuccess(0);
                    emailSendLog.setContent("用户【" + userVo.getRealName() + "】-密码重置邮件");
                    this.logServiceImpl.saveEmailSendLog(emailSendLog);
                }
            } catch (Exception e) {
                //throw new OrdinaryException("请检查邮件服务器配置...!");
            }
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("修改");
        logInfo.setOperContent("重置用户密码:" + dbVo.getRealName() + "(" + dbVo.getUserNo() + ") ");
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    /**
     * 检查非超级管理员不能修改超级管理员信息
     *
     * @author 汪逢建
     * @date 2017年11月30日
     */
    private SysUserVo checkSuperAdminUpdate(int updateUserId) {
        SysUserVo user = this.sysUserMapper.getUserVoByuserId(updateUserId);
        if (user == null) {
            return null;
        }
        if (user.getUserType() == 1) {
            if (this.getUserId() != user.getUserId()) {
                throw new OrdinaryException("非超级管理员不能修改超级管理员信息！");
            }
        }
        return user;
    }

    @Override
    public boolean saveEntity(SysUserVo userVo) {
        userVo.setUpdater(getUserId());
        userVo.setCreater(this.getUserId());
        userVo.setCreateTime(new Date());
        userVo.setCreateTimeStr(DateUtil.getNewDateStr());
        this.sysUserMapper.saveEntity(userVo);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("新增");
        logInfo.setOperContent("新增用户信息:" + userVo.getRealName() + "(" + userVo.getUserNo() + ") ");
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean deleteEntity(Integer userId) {
        SysUserVo dbVo = this.checkSuperAdminUpdate(userId);
        if (dbVo == null) {
            throw new OrdinaryException("用户不存在，或已被删除！");
        }
        if (isAdmin(dbVo)) {
            throw new OrdinaryException("超级管理员不能删除！");
        }
        // 需要删除 用户和角色的关联数据
        this.roleServiceImpl.clearUserRole(userId);
        //需要删除用户和任务的关系数据
        this.dataTaskMapper.clearUserTaskRelation(userId);
        this.sysUserMapper.deleteEntity(userId);

        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("删除");
        logInfo.setOperContent("删除用户信息:" + dbVo.getRealName() + "(" + dbVo.getUserNo() + ") ");
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean updateEntity(SysUserVo vo, boolean updateMy) {
        if (updateMy) {
            vo.setUserId(this.getUserId());
        }
        SysUserVo dbVo = this.checkSuperAdminUpdate(vo.getUserId());
        if (dbVo == null) {
            throw new OrdinaryException("用户不存在，或已被删除！");
        }
        vo.setUpdater(getUserId());
        vo.setUpdateTime(new Date());

        this.sysUserMapper.updateEntity(vo, 1);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改用户信息:" + (dbVo.getRealName().equals(vo.getRealName()) ? "" : dbVo.getRealName() + "=>") + dbVo.getRealName() + "(" + dbVo.getUserNo() + ") ");
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean updatePwd(String oldPwd, String newPwd) {
        SysUserVo vo = this.checkSuperAdminUpdate(this.getUserId());
        if (vo == null) {
            throw new OrdinaryException("用户不存在，或已被删除！");
        }
        if (!vo.getLoginPwd().equalsIgnoreCase(oldPwd)) {
            throw new OrdinaryException("旧密码错误，请重新输入！");
        }
        vo.setLoginPwd(newPwd);
        vo.setUpdater(getUserId());
        vo.setUpdateTime(new Date());
        this.sysUserMapper.updateEntity(vo, 3);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("修改");
        logInfo.setOperContent("用户密码修改:" + vo.getRealName() + "(" + vo.getUserNo() + ") ");
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public SysUserVo queryUserByEmailAndLoginName(String email, String userNo) {
        SysUserVo userVo = this.sysUserMapper.getUserVoByUserNo(userNo);
        if (userVo != null && !userVo.getEmail().equalsIgnoreCase(email)) {
            userVo = null;
        }
        return userVo;
    }


    @Override
    public boolean saveUserRole(int userId, String roleIds) {
        SysUserVo dbVo = this.checkSuperAdminUpdate(userId);
        if (dbVo == null) {
            return true;
        }
        if (isAdmin(dbVo)) {
            throw new OrdinaryException("超级管理员不能修改授权！");
        }
        List<Integer> roleList = new ArrayList<Integer>();
        StringBuffer roleNames = new StringBuffer("");
        if (StringUtil.isNotBlank(roleIds)) {
            String[] rs = roleIds.split(",");
            for (String r : rs) {
                Integer roleId = Integer.parseInt(r);
                RoleInfoVo voc = this.roleServiceImpl.getRoleInfoVo(roleId);
                if (voc != null) {
                    String a = voc.getRoleName();
                    roleNames.append(a).append(",");
                }
                roleList.add(roleId);
            }
        }
        this.sysUserMapper.deleteUserRole(userId);
        if (!roleList.isEmpty()) {
            this.sysUserMapper.saveUserRole(userId, roleList);
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("修改");
        logInfo.setOperContent("用户授权修改:" + dbVo.getRealName() + "(" + dbVo.getUserNo() + ")，共" + roleList.size() + "个角色！");
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean saveUserTasks(int busType, int userId, String taskIds) {
        SysUserVo dbVo = this.sysUserMapper.getUserVoByuserId(userId);
        if (dbVo == null) {
            return true;
        }
        List<Integer> taskList = new ArrayList<Integer>();
        this.sysUserMapper.deleteUserTasks(userId, busType);
        StringBuffer taskNames = new StringBuffer("");
        if (StringUtil.isNotBlank(taskIds)) {
            String[] rs = taskIds.split(",");
            for (String r : rs) {
                Integer tid = Integer.parseInt(r);
                DataTaskVo voc = this.dataTaskMapper.getDataTaskVo(tid);
                if (voc != null) {
                    String a = voc.getTaskName();
                    taskNames.append(a).append(",");
                }
                taskList.add(tid);
            }
        }
        if (!taskList.isEmpty()) {
            this.sysUserMapper.saveUserTasks(busType, userId, taskList);
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("修改");
        logInfo.setOperContent("用户授权" + (busType == 1 ? "发布" : "订阅") + "资源推送:" + dbVo.getRealName() + "(" + dbVo.getUserNo() + ")，共" + taskList.size() + "个数据资源！");
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }
}
