package com.gtafe.data.center.dataetl.datajob.service;

import com.gtafe.data.center.dataetl.datajob.vo.JobEntity;

import java.util.List;

public interface DataJobService {

    List<JobEntity> queryList(Integer busType, String orgIds, Integer status, String jobName, int pageNum, int pageSize);

    JobEntity getEntityDetail(int jobId, boolean b);

    void insertDataJob(int businessType, JobEntity taskVO);

    void updateDataJob(int businessType, JobEntity jobEntity);

    boolean deleteJobs(List<Integer> integers);

    boolean batchUpdateState(String jobIds, int state);
}
