package com.gtafe.data.center.system.menu.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.system.menu.service.MenuService;
import com.gtafe.data.center.system.menu.vo.MenuAuthCodeInfo;
import com.gtafe.data.center.system.menu.vo.MenuInfo;
import com.gtafe.data.center.system.menu.vo.MenuVo;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path = "/menu")
@Api(value = "MenuController")
@CrossOrigin
public class MenuController extends BaseController {


    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Resource
    private MenuService menuServiceImpl;

    @RequestMapping(path = "/queryList", method = RequestMethod.GET)
    @ApiOperation(value = "查询菜單", notes = "查询菜單列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public PageInfo<MenuVo> queryList(
            @ApiParam(name = "menuName", value = "菜单名称", required = false, defaultValue = "") @RequestParam(value = "menuName", required = false, defaultValue = "") String menuName,
            @ApiParam(name = "pageNum", value = "页码", required = false, defaultValue = "1") @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
            @ApiParam(name = "pageSize", value = "每一页数据量", required = false, defaultValue = "10") @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        List<MenuVo> result = menuServiceImpl.queryList(menuName, pageNum, pageSize);
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<MenuVo>(result);
    }

    /**
     *
     * @param vo
     * @param id
     * @return
     */
    public boolean updateEntity(
            @ApiParam(name = "vo", value = "菜單信息", required = true) @RequestBody MenuVo vo,
            @ApiParam(name = "id", value = "主键", required = true) @RequestBody int id
    ) {
        return this.menuServiceImpl.updateEntity(vo, id);
    }


    /**
     * 菜单树
     *
     * @return
     */
    @RequestMapping(value = "/getMenuTree", method = RequestMethod.GET)
    public MenuInfo getMenuTree() {
        return menuServiceImpl.getMenuTree();
    }
    
    /**
     * 菜单列表，移除顶级节点
     *
     * @return
     */
    @RequestMapping(value = "/getMenuInfos", method = RequestMethod.GET)
    public List<MenuInfo> getMenuInfos() {
        return menuServiceImpl.getMenuInfos();
    }
    
    /**
     * 获取权限码列表
     * 主要功能:     <br>
     * 注意事项:无  <br>
     * @return
     */
    @RequestMapping(value = "/queryMenuAuthCodes", method = RequestMethod.GET)
    public MenuAuthCodeInfo queryMenuAuthCodes() {
        return menuServiceImpl.queryMenuAuthCodes();
    }
    
    
    
}
