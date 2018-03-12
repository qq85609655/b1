package com.gtafe.data.center.dataetl.schedule.mapper;

import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;
import com.gtafe.data.center.runadmin.nodewatch.vo.EtlTaskStatus;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtlMapper {

    @Select("select * from data_etl_task where run_status=1")
    @Results({
            @Result(column = "task_id",property = "taskId",id = true),
            @Result(column = "task_name",property = "taskName"),
            @Result(column = "third_connection_id",property = "thirdConnectionId"),
            @Result(column = "third_tablename",property = "thirdTablename"),
            @Result(column = "business_type",property = "businessType"),
            @Result(column = "org_id",property = "orgId"),
            @Result(column = "center_tablename",property = "centerTablename"),
            @Result(column = "run_expression",property = "runEexpression"),
            @Result(column = "updatetime",property = "updateTime")
    })
    List<DataTaskVo> getAllTask();

    @Select("select * from data_etl_dataconnection where id=#{id}")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "dbtype",property = "dbType"),
            @Result(column = "db_name",property = "dbName"),
            @Result(column = "host",property = "host"),
            @Result(column = "port",property = "port"),
            @Result(column = "username",property = "username"),
            @Result(column = "password",property = "password")
    })
    DatasourceVO getDSById(int id);

    @Select("select * from data_etl_dataconnection where is_center=1")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "name"),
            @Result(column = "dbtype",property = "dbType"),
            @Result(column = "db_name",property = "dbName"),
            @Result(column = "host",property = "host"),
            @Result(column = "port",property = "port"),
            @Result(column = "username",property = "username"),
            @Result(column = "password",property = "password")
    })
    DatasourceVO getCenterDS();


    @Select("select * from data_etl_task where task_id=#{id}")
    @Results({
            @Result(column = "task_id",property = "taskId"),
            @Result(column = "task_name",property = "taskName"),
            @Result(column = "third_connection_id",property = "thirdConnectionId"),
            @Result(column = "third_tablename",property = "thirdTablename"),
            @Result(column = "center_tablename",property = "centerTablename"),
            @Result(column = "business_type",property = "businessType"),
            @Result(column = "run_expression",property = "runEexpression"),
            @Result(column = "run_status",property = "runStatus"),
            @Result(column = "task_id",property = "steps",many = @Many(select = "getStepById",fetchType = FetchType.EAGER))
    })
    DataTaskVo getDataTaskById(int id);

    @Select("select step_detail from data_etl_task_step where task_id=#{taskId} order by serial")
    List<String> getStepById(int taskId);

    @Update("update  data_etl_task set run_status=0 where task_id=#{taskId}")
    boolean stopErrorTask(int taskId);

    @Select("select ERRORS errors from kettle_log where CHANNEL_ID=#{channelId}")
    KettleLogVO taskStatus(String channelId);

    @Delete("delete from data_etl_task_status where task_id is not null ")
    void cleanAllStatus();
 
    
    
}
