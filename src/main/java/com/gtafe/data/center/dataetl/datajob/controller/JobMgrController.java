package com.gtafe.data.center.dataetl.datajob.controller;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.datajob.service.DataJobService;
import com.gtafe.data.center.dataetl.datajob.vo.JobEntity;
import com.gtafe.data.center.dataetl.datatask.service.DataTaskService;
import com.gtafe.data.center.dataetl.datatask.vo.DataJobParam;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.framework.base.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/datajob")
@CrossOrigin
public class JobMgrController extends BaseController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(JobMgrController.class);
    @Resource
    private DataJobService dataJobServiceImpl;
    @Resource
    private DataTaskService dataTaskServiceImpl;

    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    public @ResponseBody
    PageInfo<JobEntity> queryList(@RequestBody DataJobParam param) {
        List<JobEntity> result = dataJobServiceImpl.queryList(param.getBusType(),
                param.getOrgIds(), param.getStatus(), param.getJobName(), param.getPageNum(), param.getPageSize());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<JobEntity>(result);
    }

    @RequestMapping(path = "/getEntityDetail", method = RequestMethod.GET)
    public JobEntity getDataTaskVo(@RequestParam(value = "jobId", required = true) int jobId) {
        return dataJobServiceImpl.getEntityDetail(jobId, true);
    }

    @RequestMapping(path = "/insertDataJob", method = RequestMethod.POST)
    public @ResponseBody
    boolean insertDataTask(
            @RequestParam(value = "businessType") int businessType,
            @RequestBody JobEntity jobEntity) {
        this.dataJobServiceImpl.insertDataJob(businessType, jobEntity);
        return true;
    }

    @RequestMapping(path = "/updateDataJob", method = RequestMethod.POST)
    public @ResponseBody
    boolean updateDataTask(
            @RequestParam(value = "businessType") int businessType,
            @RequestBody JobEntity jobEntity) {
        this.dataJobServiceImpl.updateDataJob(businessType, jobEntity);
        return true;
    }

    @RequestMapping(path = "/{jobId}", method = RequestMethod.DELETE)
    public @ResponseBody
    boolean deleteMapping(
            @PathVariable(name = "jobId") int jobId) {
        LOGGER.debug("delete param:jobId={} ", jobId);
        return dataJobServiceImpl.deleteJobs(Arrays.asList(jobId));
    }

    @RequestMapping(path = "/updateStatus", method = RequestMethod.GET)
    public @ResponseBody
    boolean updateStatus(@RequestParam int jobId,
                         @RequestParam boolean checked) {
        return dataJobServiceImpl.batchUpdateState(jobId + "", checked ? 1 : 0);
    }

    @RequestMapping(path = "/queryTasks", method = RequestMethod.GET)
    public List<DataTaskVo> queryTasks(@RequestParam int businessType, @RequestParam String orgId) {
        return this.dataTaskServiceImpl.queryTasks(businessType, orgId);
    }
}
