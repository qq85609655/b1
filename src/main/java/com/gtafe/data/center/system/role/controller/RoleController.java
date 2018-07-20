package com.gtafe.data.center.system.role.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.org.service.IOrgService;
import com.gtafe.data.center.system.role.service.IRoleService;
import com.gtafe.data.center.system.role.vo.RoleInfoVo;
import com.gtafe.data.center.system.role.vo.RoleParam;
import com.gtafe.data.center.system.role.vo.RoleVo;
import com.gtafe.data.center.system.role.vo.TempUserRoleVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(path = "/role")
@Api(value = "RoleController")
@CrossOrigin
public class RoleController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Resource
    private IRoleService roleServiceImpl;
    @Resource
    private LogService logServiceImpl;

    @Resource
    private IOrgService orgServiceImpl;

    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    @ApiOperation(value = "查询用戶", notes = "查询用戶")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    PageInfo<RoleVo> queryList(@RequestBody RoleParam param) {
        List<RoleVo> result = roleServiceImpl.queryList(param.getRoleName(), param.getState(), param.getPageNum(), param.getPageSize());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<RoleVo>(result);
    }


    @RequestMapping(path = "/queryAllByUserId/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询角色", notes = "查询角色")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    TempUserRoleVo queryAllByUserId(
            @ApiParam(name = "userId", value = "用户id", required = true) @PathVariable("userId") String userId) {
        return roleServiceImpl.queryAllByUserId(userId);
    }


    /**
     * 保存角色
     *
     * @param roleVo
     * @return
     */
    @RequestMapping(path = "/saveEntity", method = RequestMethod.POST)
    public @ResponseBody
    boolean saveEntity(@RequestBody RoleInfoVo roleVo) {
        return this.roleServiceImpl.saveRole(roleVo);
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @RequestMapping(path = "/deleteEntity", method = RequestMethod.POST)
    public @ResponseBody
    boolean deleteEntity(@ApiParam(name = "roleId", value = "角色信息", required = true) @RequestParam(value = "roleId", required = false) Integer roleId) {
        LOGGER.info(roleId.toString());
        if (0 == roleId) {
            throw new OrdinaryException("超级管理员角色不能删除!");
        }
        return this.roleServiceImpl.deleteEntity(roleId);
    }


    /**
     * 修改角色
     *
     * @param roleVo
     * @return
     */
    @RequestMapping(path = "/updateEntity", method = RequestMethod.POST)
    public @ResponseBody
    boolean updateEntity(
            @ApiParam(name = "vo", value = "角色信息", required = true) @RequestBody RoleInfoVo roleVo) {
        return this.roleServiceImpl.updateRole(roleVo);
    }



    @RequestMapping(path = "/updateStatus", method = RequestMethod.GET)
    public @ResponseBody
    boolean updateStatus(
            @ApiParam(name = "roleId", value = "角色id", required = true) int roleId,
            @ApiParam(name = "checked", value = "状态码", required = true) boolean checked) {
        return this.roleServiceImpl.updateState(roleId, checked);
    }

    @RequestMapping(path = "/getRoleInfoVo", method = RequestMethod.GET)
    public @ResponseBody
    RoleInfoVo getRoleInfoVo(@ApiParam(name = "roleId", value = "角色id", required = true) int roleId) {
        return this.roleServiceImpl.getRoleInfoVo(roleId);
    }
}
