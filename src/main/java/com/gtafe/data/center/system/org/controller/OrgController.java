package com.gtafe.data.center.system.org.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.gtafe.framework.base.exception.OrdinaryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gtafe.data.center.system.org.service.IOrgService;
import com.gtafe.data.center.system.org.vo.OrgVo;
import com.gtafe.framework.base.annotation.AuthAnnotation;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(path = "/org")
@Api(value = "OrgController")
@CrossOrigin
public class OrgController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrgController.class);

    @Autowired
    private IOrgService orgServiceImpl;


    /**
     * 机构树
     *
     * @return
     */
    @AuthAnnotation(value = "014001", msg = "您没有机构管理的查看权限！")
    @RequestMapping("/getOrgTree")
    public OrgVo orgTree() {
        return orgServiceImpl.orgTree();
    }

    /**
     * 机构树,过滤无叶子节点的空文件夹
     *
     * @return
     */
    @RequestMapping("/orgShowTree")
    public OrgVo orgShowTree() {
        return orgServiceImpl.orgShowTree();
    }

    /**
     * 根据id返回单个机构
     *
     * @param id
     * @return
     */
    @RequestMapping("getOrg")
    public OrgVo getOrg(String id) {
        System.out.println(id);
        return orgServiceImpl.getOrgVoById(id);
    }

    /**
     * 获取新机构编码
     *
     * @param id
     * @return
     */
    @RequestMapping("getNewOrgNo")
    public String getNewOrgNo(String id) {
        return orgServiceImpl.getOrgNo(id);
    }

    /**
     * 新增机构
     *
     * @param vo
     * @return
     */
    @RequestMapping(path = "/saveEntity", method = RequestMethod.POST)
    public boolean saveEntity(
            @ApiParam(name = "vo", value = "机构信息", required = true) @RequestBody OrgVo vo, HttpServletRequest request
    ) {


        if (this.orgServiceImpl.checkOrgNo(vo.getOrgNo())) {
            throw new OrdinaryException("机构编号不能重复！");
        }

        if (this.orgServiceImpl.checkOrgName(vo.getOrgName(), vo.getParentId(), vo.getNodeType())) {
            throw new OrdinaryException("机构名称不能重复！");
        }
        vo.setCreateTime(new Date());
        vo.setCreater(this.getUserInfo().getUserId());
        vo.setUpdateTime(new Date());
        vo.setUpdater(this.getUserInfo().getUserId());
        vo.setState(1);
        vo.setSort(0);
        return orgServiceImpl.insertOrg(vo);
    }

    /**
     * 删除机构
     *
     * @param vo
     * @return
     */
    @RequestMapping(path = "/deleteEntity", method = RequestMethod.POST)
    public boolean deleteEntity(
            @ApiParam(name = "vo", value = "机构", required = true) @RequestBody OrgVo vo
    ) {
        return this.orgServiceImpl.deleteById(vo.getId());
    }

    /**
     * 修改单个机构信息
     *
     * @param vo
     * @return
     */
    @RequestMapping(path = "/updateEntity", method = RequestMethod.POST)
    public boolean updateEntity(
            @ApiParam(name = "vo", value = "机构信息", required = true) @RequestBody OrgVo vo, HttpServletRequest request
    ) {
        vo.setUpdateTime(new Date());
        vo.setUpdater(this.getUserInfo().getUserId());
        return this.orgServiceImpl.updateOrg(vo);
    }

    /**
     * 组织机构排序
     *
     * @param id
     * @param up
     * @return
     */
    @RequestMapping(path = "/updateSort", method = RequestMethod.GET)
    public boolean updateSort(String id, boolean up) {
        orgServiceImpl.updateSort(id, up);
        return true;
    }
}
