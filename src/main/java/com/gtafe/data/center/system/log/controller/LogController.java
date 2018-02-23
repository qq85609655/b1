package com.gtafe.data.center.system.log.controller;

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

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogParam;
import com.gtafe.data.center.system.log.vo.LogVo;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(path = "/log")
@Api(value = "LogController", protocols = "http")
@CrossOrigin
public class LogController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @Resource
    private LogService logServiceImpl;

    /**
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param operRes       日志类型
     * @param operModuleIds 模块id
     * @param orgIds        所属机构id
     * @param keyWord       关键字
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(path = "/queryListLogin", method = RequestMethod.POST)
    @ApiOperation(value = "查询日志信息", notes = "查询日志信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    PageInfo<LogVo> queryListLogin(@RequestBody LogParam param) {
        List<LogVo> result = logServiceImpl.queryListLogin(param.getPageNum(), param.getPageSize(),
            param.getStartTime(), param.getEndTime(), param.getOperRes(), param.getOrgIds(), param.getKeyWord(), param.getOperModuleIds());
        LOGGER.info("Result: ", result.size());
        return new PageInfo<LogVo>(result);
    }
    
    @RequestMapping(path = "/queryListOper", method = RequestMethod.POST)
    public @ResponseBody
    PageInfo<LogVo> queryListOper(@RequestBody LogParam param) {
        List<LogVo> result = logServiceImpl.queryListOper(param.getPageNum(), param.getPageSize(),
            param.getStartTime(), param.getEndTime(), param.getOperRes(), param.getOrgIds(), param.getKeyWord(), param.getOperModuleIds());
        LOGGER.info("Result: ", result.size());
        return new PageInfo<LogVo>(result);
    }

}
