package com.gtafe.data.center.common.index.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gtafe.data.center.common.common.vo.IndexVo;
import com.gtafe.data.center.common.index.service.IndexService;
import com.gtafe.data.center.runadmin.nodewatch.service.NodeWatchService;
import com.gtafe.data.center.runadmin.nodewatch.vo.NodeWatchVo;
import com.gtafe.framework.base.annotation.AuthAnnotation;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;

/**
 * 首页controller
 */
@RestController
@RequestMapping(path = "/index")
@Api(value = "IndexController")
@CrossOrigin
public class IndexController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private IndexService indexServiceImpl;
    @Resource
    private NodeWatchService nodeWatchServiceImpl;
    
    @RequestMapping("/getStandardInfos")
    public IndexVo getStandardInfos() throws Exception {
        return this.indexServiceImpl.queryIndex();
    }
    
    @RequestMapping("/getTaskStatus")
    public @ResponseBody
    IndexVo getTaskStatus() throws Exception {
        return nodeWatchServiceImpl.queryTaskRunStatus();
    }


}
