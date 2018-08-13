package com.gtafe.data.center.dataetl.plsql.controller;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.plsql.service.PlsqlService;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlParamVo;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping(path = "/plsql")
@CrossOrigin
public class PlsqlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlsqlController.class);

    @Resource
    private PlsqlService plsqlServiceImpl;


    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    public @ResponseBody
    PageInfo<PlsqlVo> queryList(@RequestBody PlsqlParamVo param) {
        LOGGER.debug("in PlsqlController.queryList method");
        List<PlsqlVo> result = plsqlServiceImpl.queryList(param.getPageNum(), param.getPageSize(), param.getOrgIds(), param.getNameKey());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<PlsqlVo>(result);
    }

}
