package com.gtafe.data.center.dataetl.datatask.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.gtafe.data.center.dataetl.datatask.vo.TaskOrgsInfoVo;
import com.gtafe.data.center.system.role.vo.RoleInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskParam;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.framework.base.annotation.AuthAnnotation;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(path = "/datatask")
@Api(value = "DataTaskController")
@CrossOrigin
public class DataTaskController extends BaseController {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DataTaskController.class);
    @Resource
    private DataTaskService dataTaskServiceImpl;

    @AuthAnnotation(value = {"011001", "012001"}, conditions = {"businessType=1", "businessType=2"})
    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    public @ResponseBody
    PageInfo<DataTaskVo> queryList(@RequestBody DataTaskParam param,
                                   @RequestParam(value = "businessType", required = true) Integer businessType) {
        param.setBusinessType(businessType);
        List<DataTaskVo> result = dataTaskServiceImpl.queryList(
                param.getCollectionId(), param.getOrgIds(), 1, param.getStatus(), param.getName(), param.getPageNum(), param.getPageSize(), businessType);
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<DataTaskVo>(result);
    }

    @RequestMapping(path = "/startNow/{taskId}", method = RequestMethod.GET)
    public @ResponseBody
    boolean startNow(@PathVariable(name = "taskId") String taskId) {
        return this.dataTaskServiceImpl.startNow(Integer.parseInt(taskId));
    }


    @RequestMapping(path = "/startLocalKettle", method = RequestMethod.GET)
    public @ResponseBody
    boolean startLocalKettle() {
        return this.dataTaskServiceImpl.startLocalKettleNow();
    }


    /**
     * 根据任务id获取明细信息
     *
     * @author 汪逢建
     * @date 2017年11月15日
     */
    @RequestMapping(path = "/getDataTaskVo", method = RequestMethod.GET)
    public DataTaskVo getDataTaskVo(@RequestParam(value = "taskId", required = true) int taskId) {
        return dataTaskServiceImpl.getDataTaskVo(taskId, true);
    }


    @RequestMapping(path = "/cloneTasksTo", method = RequestMethod.POST)
    public @ResponseBody
    boolean cloneTasksTo(@RequestBody TaskOrgsInfoVo vo) {
        return this.dataTaskServiceImpl.cloneTasksTo(vo);
    }


    /**
     * 新增资源任务
     *
     * @author 汪逢建
     * @date 2017年11月16日
     */
    @AuthAnnotation(value = {"011002", "012002"}, conditions = {"businessType=1", "businessType=2"})
    @RequestMapping(path = "/insertDataTask", method = RequestMethod.POST)
    public @ResponseBody
    boolean insertDataTask(
            @RequestParam(value = "businessType") int businessType,
            @RequestBody DataTaskVo taskVO) {
        this.dataTaskServiceImpl.insertDataTaskVo(businessType, taskVO);
        return true;
    }

    /**
     * 保存资源任务
     *
     * @author 汪逢建
     * @date 2017年11月16日
     */
    @AuthAnnotation(value = {"011003", "012003"}, conditions = {"businessType=1", "businessType=2"})
    @RequestMapping(path = "/updateDataTask", method = RequestMethod.POST)
    public @ResponseBody
    boolean updateDataTask(
            @RequestParam(value = "businessType") int businessType,
            @RequestBody DataTaskVo taskVO) {
        this.dataTaskServiceImpl.updateDataTaskVo(businessType, taskVO);
        return true;
    }

    //@AuthAnnotation(value = {"011004","012004"}, conditions= {"businessType=1","businessType=2"})
    @RequestMapping(path = "/{taskId}", method = RequestMethod.DELETE)
    public @ResponseBody
    boolean deleteMapping(
            @PathVariable(name = "taskId") int taskId) {
        LOGGER.debug("delete param:taskId={} ", taskId);
        return dataTaskServiceImpl.deleteTasks(Arrays.asList(taskId));
    }

    /**
     * deleteSource:批量删除发布资源. <br/>
     */
    @RequestMapping(path = "/deleteTaskBatch/{taskIds}", method = RequestMethod.DELETE)
    public @ResponseBody
    boolean deleteTaskBatch(
            @PathVariable(name = "taskIds") String taskIds) {
        LOGGER.debug("delete param:taskIds={} ", taskIds);
        // mapping不存在时，返回false
        String[] taskIds_ = taskIds.split(",");
        List<Integer> taskIdList = new ArrayList<Integer>();
        for (String mdddp : taskIds_) {
            taskIdList.add(Integer.parseInt(mdddp));
        }
        dataTaskServiceImpl.deleteTasks(taskIdList);
        return true;
    }

    /**
     * deleteSource:批量修改资源状态
     */
    @RequestMapping(path = "/startStopServiceBatch/{taskIds}/{state}", method = RequestMethod.PUT)
    public @ResponseBody
    boolean batchUpdateState(
            @PathVariable(name = "taskIds") String taskIds,
            @PathVariable(name = "state") int state) {
        LOGGER.debug("update param:taskIds state={}  ", taskIds, state);
        return this.dataTaskServiceImpl.batchUpdateState(taskIds, state);
    }

    @RequestMapping(path = "/updateStatus", method = RequestMethod.GET)
    public @ResponseBody
    boolean updateStatus(@RequestParam int taskId,
                         @RequestParam boolean checked) {
        return dataTaskServiceImpl.batchUpdateState(taskId + "", checked ? 1 : 0);
    }
}
