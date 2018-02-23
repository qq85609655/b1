package com.gtafe.data.center.runadmin.etlerrorlog.controller;


import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.runadmin.etlerrorlog.service.IKettleLogService;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.ErrorLogVo;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogParam;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Controller
@RequestMapping(path = "/kettleLog")
@Api(value = "KettleLogController")
@CrossOrigin
public class KettleLogController extends BaseController {
    @Resource
    private IKettleLogService kettleLogServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(
            KettleLogController.class);

    /**
     * list:分页显示映射关系数据. <br/>
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    @ApiOperation(value = "分页显示kettle Log", notes = "分页显示kettle Log")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询kettle Log 信息:参数")})
    public @ResponseBody
    PageInfo<KettleLogVO> list(@RequestBody KettleLogParam param){
        List<KettleLogVO> result = kettleLogServiceImpl.list(param.getBusType(),
            param.getStartTime(),param.getEndTime(),param.getPageNum(), param.getPageSize(), param.getTransName(), param.getOrgIds());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<KettleLogVO>(result);
    }

    /**
     * list:分页显示映射关系数据. <br/>
     *
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    @RequestMapping(path = "/queryErrorList", method = RequestMethod.GET)
    @ApiOperation(value = "分页显示kettle error Log", notes = "分页显示kettle error Log")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询kettle Log 信息:参数")})
    public @ResponseBody
    PageInfo<ErrorLogVo> queryErrorList(@ApiParam(name = "pageNum", value = "页码", required = false, defaultValue = "1") @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @ApiParam(name = "channel_id", value = "channel_id", required = false, defaultValue = "") @RequestParam(value = "channel_id", required = false) String channel_id,
                                        @ApiParam(name = "pageSize", value = "每一页数据数量", required = false, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        LOGGER.info(channel_id);
        List<ErrorLogVo> result = kettleLogServiceImpl.queryErrorList(pageNum, pageSize, channel_id);
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<ErrorLogVo>(result);
    }



}
