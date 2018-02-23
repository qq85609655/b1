package com.gtafe.data.center.runadmin.nodewatch.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.runadmin.nodewatch.service.NodeWatchService;
import com.gtafe.data.center.runadmin.nodewatch.vo.NodeWatchParam;
import com.gtafe.data.center.runadmin.nodewatch.vo.NodeWatchVo;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description:
 */
@Controller
@RequestMapping(path = "/nodeStatus")
@Api(value = "NodeWatchController")
@CrossOrigin
public class NodeWatchController extends BaseController {


    @Resource
    private NodeWatchService nodeWatchServiceImpl;

    @Resource
    private DataTaskService dataTaskServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(
            NodeWatchController.class);

    @SuppressWarnings("resource")
    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    @ApiOperation(value = "分页显示", notes = "分页显示")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询:参数")})
    public @ResponseBody
    PageInfo<NodeWatchVo> list(@RequestBody NodeWatchParam param) {
        List<DataTaskVo> list=this.dataTaskServiceImpl.queryList(0, param.getOrgIds(),0, -1,"",param.getPageNum(),param.getPageSize(),param.getBusType());
        List<NodeWatchVo> result = nodeWatchServiceImpl.list(list);
        if(list instanceof Page) {
            Page<DataTaskVo> page = (Page<DataTaskVo>)list;
            Page<NodeWatchVo> newPage = new Page<NodeWatchVo>(page.getPageNum(), page.getPageSize());
            newPage.addAll(result);
            newPage.setTotal(page.getTotal());
            result = newPage;
        }
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<NodeWatchVo>(result);
    }
}
