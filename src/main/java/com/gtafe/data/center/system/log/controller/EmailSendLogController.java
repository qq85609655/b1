package com.gtafe.data.center.system.log.controller;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.EmailSendLog;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.utils.StringUtil;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/12/6
 * @Description:
 */

@Controller
@RequestMapping(path = "/emailSendLog")
@Api(value = "EmailSendLogController", protocols = "http")
@CrossOrigin
public class EmailSendLogController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

    @Resource
    private LogService logServiceImpl;

    /**
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param module       module
     * @param keyWord       关键字
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(path = "/queryList", method = RequestMethod.GET)
    @ApiOperation(value = "查询日志信息", notes = "查询日志信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    PageInfo<EmailSendLog> queryList(
            @ApiParam(name = "startTime", value = "开始时间", required = false) @RequestParam(value = "startTime") String startTime,
            @ApiParam(name = "endTime", value = "结束时间", required = false) @RequestParam(value = "endTime") String endTime,
            @ApiParam(name = "module", value = "模块id", required = false) @RequestParam(value = "module") String module,
            @ApiParam(name = "keyWord", value = "关键字", required = false) @RequestParam(value = "keyWord") String keyWord,
            @ApiParam(name = "pageNum", value = "页码", required = false, defaultValue = "1") @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ApiParam(name = "pageSize", value = "每一页数据数量", required = false, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        LOGGER.info(startTime);
        LOGGER.info(endTime);
        LOGGER.info(module);
        LOGGER.info(keyWord);
        if (!StringUtil.isNotBlank(startTime) || startTime.equals("null")) {
            startTime = null;
        }
        if (!StringUtil.isNotBlank(endTime)|| endTime.equals("null")) {
            endTime = null;
        }
        List<EmailSendLog> result = logServiceImpl.queryEmailSendLogList(pageNum, pageSize, startTime, endTime, module, keyWord);
        LOGGER.info("Result: ", result.size());
        return new PageInfo<EmailSendLog>(result);
    }


}
