package com.gtafe.data.center.system.user.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.user.service.SysUserFileService;
import com.gtafe.data.center.system.user.service.SysUserService;
import com.gtafe.data.center.system.user.vo.MessageVo;
import com.gtafe.data.center.system.user.vo.SysUserParam;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.annotation.AuthAnnotation;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(path = "/sysuser")
@Api(value = "SysUserController")
@CrossOrigin
public class SysUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserController.class);

    @Resource
    private SysUserService sysUserServiceImpl;

    @Autowired
    private SysUserFileService sysUserFileServiceImpl;

    /**
     * 查詢列表
     *
     * @return /api/sysuser/queryList?
     * state=-1
     * &orgId=1
     * &orgIds=1,28,80,82,85,8,47,70,55,56,57,69,74,20,25,10,26,75,76,77,79
     * &orgName=中国科技大学
     * &keyWord=
     * &userId=
     * &userNo=
     * &pageNum=1
     * &pageSize=10&1512361358176
     */
    @AuthAnnotation("013001")
    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    @ApiOperation(value = "查询用戶", notes = "查询用戶")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    PageInfo<SysUserVo> queryList(@RequestBody SysUserParam param){
        List<SysUserVo> result = sysUserServiceImpl
            .queryList(param.getKeyWord(), param.getState(), param.getOrgIds(), param.getPageNum(), param.getPageSize());
        return new PageInfo<SysUserVo>(result);
    }


    @RequestMapping(path = "/queryList4Role", method = RequestMethod.GET)
    @ApiOperation(value = "查询用戶s", notes = "查询用戶s")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    List<SysUserVo> queryList4Role(
            @ApiParam(name = "roleId", value = "角色id", required = false, defaultValue = "") @RequestParam(value = "roleId", required = false) int roleId) {
        return sysUserServiceImpl.queryList4Role(roleId);
    }

    /**
     * 保存用户信息
     *
     * @param vo
     * @return
     */
    @RequestMapping(path = "/saveEntity", method = RequestMethod.POST)
    public @ResponseBody
    boolean saveEntity(
            @ApiParam(name = "vo", value = "用户信息", required = true) @RequestBody SysUserVo vo
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        vo.setState(true);
        if (vo.getSex() == 0) {
            vo.setSex(1);
        }

        //設置默認密碼
        vo.setLoginPwd("e10adc3949ba59abbe56e057f20f883e");
        boolean sfcz = this.sysUserServiceImpl.checkUserNo(vo.getUserNo());
        if (sfcz) {
            throw new OrdinaryException("用户编号已经存在，请重新输入!");
        }
        return this.sysUserServiceImpl.saveEntity(vo);
    }

    /**
     * 保存用户信息
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @RequestMapping(path = "/saveUserRole/{userId}/{roleIds}")
    public @ResponseBody
    boolean saveUserRole(
            @ApiParam(name = "userId", value = "用户id", required = true) @PathVariable("userId") String userId,
            @ApiParam(name = "roleIds", value = "角色ids", required = true) @PathVariable("roleIds") String roleIds
    ) {
        LOGGER.info(userId + "-----" + roleIds);
        return this.sysUserServiceImpl.saveUserRole(userId, roleIds);
    }


    @RequestMapping(path = "/saveUserTask")
    public @ResponseBody
    boolean saveUserTask(
            @ApiParam(name = "busType", value = "业务类型", required = true) @RequestParam("busType") int busType,
            @ApiParam(name = "userId", value = "用户id", required = true) @RequestParam("userId") String userId,
            @ApiParam(name = "taskIds", value = "数据资源ids", required = true) @RequestParam("taskIds") String taskIds
    ) {
        LOGGER.info(busType + "-----" + userId + "-----" + taskIds);
        return this.sysUserServiceImpl.saveUserTasks(busType, userId, taskIds);
    }


    /**
     * 删除用户信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(path = "/deleteEntity", method = RequestMethod.GET)
    public @ResponseBody
    boolean deleteEntity(
            @ApiParam(name = "userId", value = "userId", required = true) @RequestParam String userId
    ) {
        return this.sysUserServiceImpl.deleteEntity(userId);
    }

    /**
     * 修改用户信息
     *
     * @param vo
     * @return
     */
    @RequestMapping(path = "/updateEntity", method = RequestMethod.POST)
    public @ResponseBody
    @AuthAnnotation("013003")
    boolean updateEntity(
            @ApiParam(name = "vo", value = "用户信息", required = true) @RequestBody SysUserVo vo
    ) {

        return this.sysUserServiceImpl.updateEntity(vo, false);
    }

    /**
     * 修改用户信息
     *
     * @param vo
     * @return
     */
    @RequestMapping(path = "/updateMyEntity", method = RequestMethod.POST)
    public @ResponseBody
    boolean updateMyEntity(
            @ApiParam(name = "vo", value = "用户信息", required = true) @RequestBody SysUserVo vo) {
        vo.setUserId(this.getUserId());
        boolean result = this.sysUserServiceImpl.updateEntity(vo, true);
        UserLoginInfo userInfo = this.getUserInfo();
        userInfo.setRealName(vo.getRealName());
        userInfo.setEmail(vo.getEmail());
        userInfo.setSex(vo.getSex());
        return result;
    }

    /**
     * 密碼丟失 時 找回
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @RequestMapping(path = "/updatePwd", method = RequestMethod.GET)
    public @ResponseBody
    boolean updatePwd(@ApiParam(name = "oldPwd", value = "原密码", required = true) @RequestParam String oldPwd,
                      @ApiParam(name = "newPwd", value = "新密码", required = true) @RequestParam String newPwd) {
        return this.sysUserServiceImpl.updatePwd(oldPwd, newPwd);
    }


    /**
     * 重置密碼
     *
     * @param userId
     * @return
     */
    @RequestMapping(path = "/resetPwd", method = RequestMethod.GET)
    public @ResponseBody
    boolean resetPwd(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam String userId) {
        SysUserVo vo = this.sysUserServiceImpl.getUserVoByuserId(userId);
        vo.setLoginPwd(StringUtil.MD5("123456"));
        return this.sysUserServiceImpl.resetPwd(vo);
    }


    /**
     * 修改用户状态
     *
     * @param userId
     * @param checked
     * @return
     */
    @AuthAnnotation("013003")
    @RequestMapping(path = "/updateStatus", method = RequestMethod.GET)
    public @ResponseBody
    boolean updateStatus(
            @ApiParam(name = "userId", value = "userId", required = true) @RequestParam String userId,
            @ApiParam(name = "checked", value = "checked", required = true) @RequestParam boolean checked) {
        SysUserVo vo = this.sysUserServiceImpl.getUserVoByuserId(userId);
        LOGGER.info(checked + "");
        vo.setState(checked);
        return this.sysUserServiceImpl.updateStatus(vo);
    }


    @RequestMapping(path = "/updateSendStatus", method = RequestMethod.GET)
    public @ResponseBody
    boolean updateSendStatus(
            @ApiParam(name = "userId", value = "userId", required = true) String id,
            @ApiParam(name = "checked", value = "checked", required = true) boolean checked) {
        SysUserVo vo = this.sysUserServiceImpl.getUserVoByuserId(id);
        LOGGER.info(checked + "");
        vo.setSendErrorState(checked);
        return this.sysUserServiceImpl.updateSendStatus(vo);
    }

//    @RequestMapping("t")
//    public String testpage() {
//        return "test";
//    }

    @RequestMapping("userDL")
    public void userDL(HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + new String("user.xlsx".getBytes("gbk"), "iso-8859-1"));
        Workbook wb = sysUserFileServiceImpl.exportUserInfos();
        try {
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(path = "up", method = RequestMethod.POST)
    public @ResponseBody MessageVo up(@RequestParam("file") MultipartFile file) throws Exception {

        if (file == null || file.getSize() == 0) {
            throw new OrdinaryException("文件不存在或者文件内容为空！请选择文件!");
        } else {
            return sysUserFileServiceImpl.importUserInfos(file);
        }

    }

    @GetMapping("user.xlsx")
    public String template() {
        return "forward:/common/resources/user.xlsx";
    }
}
