package com.gtafe.data.center.runadmin.alertpush.controller;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.runadmin.alertpush.service.AlertPushService;
import com.gtafe.data.center.runadmin.alertpush.vo.AlertPush;
import com.gtafe.data.center.runadmin.alertpush.vo.AlertPushParam;
import com.gtafe.data.center.runadmin.alertpush.vo.TempAlertPushVo;
import com.gtafe.framework.base.controller.BaseController;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description:
 */
@Controller
@RequestMapping(path = "/alertPush")
@Api(value = "SysUserController")
@CrossOrigin
public class AlertPushController extends BaseController {
    @Resource
    private AlertPushService alertPushServiceImpl;



    private static final Logger LOGGER = LoggerFactory.getLogger(
            AlertPushController.class);

    /**
     * list:分页显示映射关系数据. <br/>
     *
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    @ApiOperation(value = "分页显示", notes = "分页显示")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询:参数")})
    public @ResponseBody
    PageInfo<AlertPush> list(@RequestBody AlertPushParam param) {
        List<AlertPush> result = alertPushServiceImpl.list(param.getPageNum(), param.getPageSize(), param.getIsPush(), param.getOrgIds());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<AlertPush>(result);
    }

    /**
     * 查询已经发布的任务资源 和人 机构之间的关系 数据
     * @param userId
     * @return
     */
    @RequestMapping(path = "/queryAllByParams", method = RequestMethod.GET)
    public  @ResponseBody
     TempAlertPushVo  queryAllByParams(@RequestParam("userId") String userId, @RequestParam("businessType") int businessType){
        TempAlertPushVo vo=new TempAlertPushVo();
        //查询当前机构下 所发布的数据资源
        List<DataTaskVo> dataTaskVoList=this.alertPushServiceImpl.queryMappingVosByOrg(businessType);
        vo.setList(dataTaskVoList);
        //查询当前人已经拥有过的配置的资源数据
        List<Integer> mapTaskIds=this.alertPushServiceImpl.queryUserMapTaskIds(userId,businessType);
        vo.setHasTaskIds(mapTaskIds);
        vo.setUserId(userId);
        return vo;
    }


}
