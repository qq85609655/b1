package com.gtafe.data.center.system.user.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.data.center.system.org.mapper.OrgMapper;
import com.gtafe.data.center.system.org.vo.OrgVo;
import com.gtafe.data.center.system.role.mapper.RoleMapper;
import com.gtafe.data.center.system.role.vo.RoleVo;
import com.gtafe.data.center.system.user.mapper.SysUserMapper;
import com.gtafe.data.center.system.user.service.SysUserFileService;
import com.gtafe.data.center.system.user.vo.ErrorVo;
import com.gtafe.data.center.system.user.vo.MessageVo;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.utils.DateUtil;
import com.gtafe.framework.base.utils.StringUtil;

/**
 * 用户导入导出功能实现
 */
@Service
public class SysUserFileServiceImpl extends BaseController implements SysUserFileService {

    @Autowired
    OrgMapper orgMapper;

    @Autowired
    SysUserMapper sysUserMapper;
    @Resource
    private LogService logServiceImpl;

    @Autowired
    RoleMapper roleMapper;
    @Resource
    private SysConfigService sysConfigServiceImpl;

    private String split = "，";


    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserFileServiceImpl.class);

    /**
     * 导出用户
     *
     * @return
     */
    public Workbook exportUserInfos() {
        List<OrgVo> orgVos = orgMapper.getOrgVos4Import(-1);
        List<SysUserVo> userVos = sysUserMapper.getAllUser();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("sheet1");
        Row rowHead = sheet1.createRow(0);
        rowHead.createCell(0).setCellValue("序号");
        rowHead.createCell(1).setCellValue("用户编号");
        rowHead.createCell(2).setCellValue("姓名");
        rowHead.createCell(3).setCellValue("所属机构");
        rowHead.createCell(4).setCellValue("邮箱");
        rowHead.createCell(5).setCellValue("性别");
        rowHead.createCell(6).setCellValue("用户权限");
        for (int i = 0; i < userVos.size(); i++) {
            Row row = sheet1.createRow(i + 1);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(userVos.get(i).getUserNo());
            row.createCell(2).setCellValue(userVos.get(i).getRealName());
            String orgName = null;
            if(this.isAdmin(userVos.get(i))) {
                orgName = sysConfigServiceImpl.getBasicSysConfigVO().getSchoolName();
            }else {
                orgName = this.getOrgName(userVos.get(i).getOrgId(), orgVos);
            }
            row.createCell(3).setCellValue(orgName);
            row.createCell(4).setCellValue(userVos.get(i).getEmail());
            row.createCell(5).setCellValue(getSex(userVos.get(i).getSex()));
            row.createCell(6).setCellValue(getAuth(userVos.get(i)));
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("查看");
        logInfo.setOperContent("导出用户，共" + userVos.size() + "个用户");
        this.logServiceImpl.saveLog(logInfo);
        return wb;
    }

    /**
     * 上传
     *
     * @param file
     * @throws IOException
     */
    public MessageVo importUserInfos(MultipartFile file) throws Exception {
        String creatorId = this.getUserId();
        List<OrgVo> orgVosAll = orgMapper.getOrgVos(this.isAdmin() ?"-1" : creatorId);
        List<OrgVo> orgVos = new ArrayList<OrgVo>();
        for(OrgVo org : orgVosAll) {
            if(org.getNodeType() == 3) {
                orgVos.add(org);
            }
        }
        List<RoleVo> roleVos = roleMapper.queryAll();
        List<SysUserVo> userVos = sysUserMapper.getAllUser();
        XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        int successCount = 0;
        int errorCount = 0;
        LOGGER.info("开始导入....");
        MessageVo messageVo = new MessageVo();
        List<SysUserVo> successVos = new ArrayList<SysUserVo>();
        List<ErrorVo> errorVos = new ArrayList<ErrorVo>();
        int lastRowNum = sheet.getLastRowNum();
        for (int i = sheet.getFirstRowNum() + 1; i <= lastRowNum; i++) {
            List<Integer> roleIds = new ArrayList<Integer>();
            //成功对象
            SysUserVo successVo = new SysUserVo();
            //失败信息
            ErrorVo errorVo = new ErrorVo();

            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            StringBuffer error = new StringBuffer("");
            String userNo = this.checkCellValue(row, error, userVos, 0);

            String realName = this.checkCellValue(row, error, userVos, 1);

            String orgNo = this.checkCellValue(row, error, userVos, 2);
            String orgId = "";
            if (orgNo != null) {
                orgId = getOrgId(orgNo, orgVos);
                if (orgId.equals("")) {
                    error.append("所属机构不存在、无权限或非第三级节点" + split);
                }
            }

            String email = this.checkCellValue(row, error, userVos, 3);

            String sex = this.checkCellValue(row, error, userVos, 4);

            String roles = this.checkCellValue(row, error, userVos, 5);
            if (roles != null) {
                String[] roless = roles.split(",");
                for (String role : roless) {
                    String roleId = getRoleId(role, roleVos);
                    if (StringUtil.isNotBlank(roleId)) {
                        roleIds.add(Integer.parseInt(roleId));
                    }
                }
            }

            if (error.length() > 0) {
                errorCount++;
                errorVo.setNumber(i + 1);
                errorVo.setUserNo(userNo == null ? "" : userNo);
                errorVo.setRealName(realName == null ? "" : realName);
                if (error.toString().endsWith(split)) {
                    errorVo.setMsg(error.substring(0, error.length() - 1));
                } else {
                    errorVo.setMsg(error.toString());
                }
                errorVos.add(errorVo);
            } else {
                successVo.setUserNo(userNo);
                successVo.setRealName(realName);
                successVo.setOrgId(orgId);
                successVo.setSex(sex != null && "女".equals(sex) ? 2 : 1);
                successVo.setEmail(email);
                successVo.setLoginPwd("e10adc3949ba59abbe56e057f20f883e");
                successVo.setCreater(creatorId);
                successVo.setCreateTime(new Date());
                successVo.setCreateTimeStr(DateUtil.getNewDateStr());
                successVo.setUpdater(creatorId);
                successVo.setUpdateTime(new Date());
                successVo.setState(true);
                sysUserMapper.saveEntity(successVo);
                if (roleIds.size() > 0) {
                    sysUserMapper.saveUserRole(successVo.getUserId(), roleIds);
                }
                successCount++;
                userVos.add(successVo);
            }
            successVos.add(successVo);
        }
        messageVo.setErrorVos(errorVos);
        messageVo.setErrorCount(errorCount);
        messageVo.setSuccessCount(successCount);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_User);
        logInfo.setOperType("修改");
        logInfo.setOperContent("导入用户，成功：" + successCount + ",失败:" + errorCount);
        this.logServiceImpl.saveLog(logInfo);
        wb.close();
        return messageVo;
    }

    public Object[][] checks = new Object[][]{
            {"用户编号", true, 20, "^[\\u4e00-\\u9fa5A-Za-z0-9\\-\\_]+$"},
            {"姓名", true, 20, "^[\\u4e00-\\u9fa5A-Za-z0-9\\-\\_]+$"},
            {"所属机构", true, 20, "^[\\u4e00-\\u9fa5A-Za-z0-9\\-\\_]+$"},
            {"邮箱", true, 100, "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"},
            {"性别", false, 0, ""},
            {"用户权限", false, 0, ""}
    };

    private String checkCellValue(Row row, StringBuffer error, List<SysUserVo> userVos, int type) {
        Cell cell = row.getCell(type + 1);
        String name = (String) checks[type][0];
        boolean notBlank = (Boolean) checks[type][1];
        int maxLength = (Integer) checks[type][2];
        String regex = (String) checks[type][3];
        String value = cell != null ? cell.toString().trim() : null;
        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            error.append(name + "单元格必须为文本格式" + split);
            return value;
        }
        if (notBlank && value == null) {
            error.append(name + "不能为空" + split);
            return null;
        }
        if (type == 0) {
            if (this.checkExist(value, userVos)) {
                error.append(name + "已存在" + split);
                return value;
            }
        }
        if (maxLength > 0 && value.length() > maxLength) {
            error.append(name + "最大支持" + maxLength + "个长度" + split);
            return value;
        }
        if (EmptyUtil.isNotBlank(regex) && !value.matches(regex)) {
            error.append(name + "格式不正确" + split);
            return value;
        }
        return value;
    }


    private String getOrgName(String orgId, List<OrgVo> orgVos) {
        String result = "";
        for (OrgVo org : orgVos) {
            if (orgId == org.getId()) {
                result = org.getOrgName();
                break;
            }
        }
        return result;
    }

    /**
     * 获取机构id
     *
     * @param orgNo
     * @param orgVos
     * @return
     */
    private String getOrgId(String orgNo, List<OrgVo> orgVos) {

        String result = "";
        for (OrgVo org : orgVos) {
            if (orgNo.equals(org.getOrgNo())) {
                result = String.valueOf(org.getId());
                break;
            }
        }
        return result;
    }

    /**
     * 性别
     *
     * @param sex
     * @return
     */
    private String getSex(int sex) {
        String s;
        switch (sex) {
            case 1:
                s = "男";
                break;
            case 2:
                s = "女";
                break;
            default:
                s = "男";
        }
        return s;
    }

    /**
     * 性别
     *
     * @param sex
     * @return
     */
    private int getSexInt(String sex) {
        int s = 1;
        switch (sex) {
            case "男":
                s = 1;
            case "女":
                s = 2;
            default:
                s = 1;
        }
        return s;
    }

    /**
     * 角色
     *
     * @param sysUserVo
     * @return
     */
    private String getAuth(SysUserVo sysUserVo) {
        String auth = "";
        for (RoleVo roleVo : sysUserVo.getRoles()) {
            if (!auth.equals("")) {
                auth += ",";
            }
            auth += roleVo.getRoleName();
        }
        return auth;
    }

    /**
     * 角色id
     *
     * @param roleName
     * @param roleVos
     * @return
     */
    private String getRoleId(String roleName, List<RoleVo> roleVos) {

        String roleId = "";

        for (RoleVo roleVo : roleVos) {

            if (roleName.equals(roleVo.getRoleName())) {
                roleId = String.valueOf(roleVo.getRoleId());
                break;
            }
        }

        return roleId;
    }

    private boolean checkExist(String userNo, List<SysUserVo> sysUserVos) {

        boolean b = false;

        for (SysUserVo sysUserVo : sysUserVos) {
            if (userNo.equals(sysUserVo.getUserNo())) {
                b = true;
                break;
            }
        }

        return b;
    }
}
