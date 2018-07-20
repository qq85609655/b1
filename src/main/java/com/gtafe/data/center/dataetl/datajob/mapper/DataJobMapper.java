package com.gtafe.data.center.dataetl.datajob.mapper;

import com.gtafe.data.center.dataetl.datajob.vo.JobEntity;
import com.gtafe.framework.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataJobMapper extends BaseMapper {


    List<JobEntity> queryList(@Param("busType")Integer busType,
                              @Param("orgIdList") List<Integer> orgIdList,
                              @Param("status") Integer status,
                              @Param("jobName") String jobName,
                              @Param("pageNumKey") int pageNum,
                              @Param("pageSizeKey") int pageSize
    );

    List<JobEntity> getJobVos(List<Integer> jobList);

    void updateStatus(List<Integer> jobList, int state, String userId);
}
