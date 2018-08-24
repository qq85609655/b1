
package com.gtafe.data.center.dataetl.datatask.mapper;

import java.util.List;

import com.gtafe.data.center.dataetl.datatask.vo.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.gtafe.data.center.runadmin.nodewatch.vo.EtlTaskStatus;
import com.gtafe.framework.base.mapper.BaseMapper;

public interface DataTaskMapper extends BaseMapper {

    List<DataTaskVo> queryList(@Param("collectionId") int collectionId, @Param("orgIdList") List<String> orgIdList,
                               @Param("status") Integer status,
                               @Param("name") String name,
                               @Param("pageNumKey") int pageNum,
                               @Param("pageSizeKey") int pageSize,
                               @Param("businessType") Integer businessType);

    DataTaskVo getDataTaskVo(@Param("taskId") Integer taskId);

    List<DataTaskVo> getDataTaskVos(@Param("taskIds") List<Integer> taskIds);

    /**
     * 检查名称重复
     *
     * @author 汪逢建
     * @date 2017年11月16日
     */
    int checkTaskNameRepeat(@Param("taskId") Integer taskId, @Param("taskName") String taskName, @Param("orgId") String orgId, @Param("businessType") int businessType);

    int insertDataTask(@Param("taskVo") DataTaskVo taskVo, @Param("userId") String userId);

    boolean updateDataTask(@Param("taskVo") DataTaskVo taskVo, @Param("userId") String userId);

    /**
     * @param runStatus 1启动，0停止
     * @author 汪逢建
     * @date 2017年11月16日
     */
    boolean updateDataTaskStatus(@Param("taskIds") List<Integer> taskIds, @Param("runStatus") int runStatus, @Param("userId") String userId);

    boolean deleteTaskSteps(@Param("taskId") Integer taskId);

    boolean insertTaskSteps(@Param("taskId") Integer taskId, @Param("steps") List<String> steps, @Param("userId") String userId);

    @Select("select step_detail from data_etl_task_step where task_id=#{taskId} order by serial asc")
    List<String> getTaskSteps(@Param("taskId") Integer taskId);


    @Select("select step_id stepId, task_id taskId, serial , " +
            " step_detail stepDetail, updater ,updatetime updateTime " +
            "from data_etl_task_step where task_id=#{taskId} order by serial asc")
    List<TaskStepVo> getTaskStepsAll(@Param("taskId") Integer taskId);

    @Delete("delete from data_etl_task where task_id= #{taskId}")
    void deleteTaskById(@Param("taskId") Integer taskId);

    List<DataTaskVo> queryListByOrgs(@Param("orgIds") List<String> orgIds, @Param("businessType") int businessType);

    @Delete("delete from sys_user_task where user_id=#{userId}")
    void clearUserTaskRelation(@Param("userId") String userId);

    @Select("   SELECT\n" +
            "        m.task_id taskId,\n" +
            "        m.task_name taskName,\n" +
            "        m.business_type businessType,\n" +
            "        m.third_tablename thirdTablename,\n" +
            "        m.center_tablename centerTablename \n" +
            "        FROM data_etl_task m \n" +
            "       where m.subclass_code =#{subclassCode}  ")
    List<DataTaskVo> findTasksBySubclass(@Param("subclassCode") String subclassCode);

    @Select("   SELECT\n" +
            "        m.task_id taskId,\n" +
            "        m.task_name taskName,\n" +
            "        m.business_type businessType,\n" +
            "        m.third_tablename thirdTablename,\n" +
            "        m.center_tablename centerTablename \n" +
            "        FROM data_etl_task m \n" +
            "       where m.third_connection_id =#{collectionId}  ")
    List<DataTaskVo> findTasksByConnId(@Param("collectionId") int collectionId);


    void saveEtlTaskStatus(@Param("ee") EtlTaskStatus etlTaskStatus);

    List<EtlTaskStatus> queryTaskStatusList(@Param("orgIdList") List<String> orgIdList,
                                            @Param("pageNumKey") int pageNum,
                                            @Param("pageSizeKey") int pageSize,
                                            @Param("busType") int busType);

    List<TransFileVo> queryKfileList(@Param("fileName") String fileName,
                                     @Param("fileType") String fileType,
                                     @Param("pageNumKey") int pageNum,
                                     @Param("pageSizeKey") int pageSize);

    @Select("select file_path filePath,schedule_info scheduleInfo,file_name fileName," +
            "update_time updateTime from t_trans_file_info where file_name=#{fileName}")
    TransFileVo findEtlFileInfoById(@Param("fileName") String fileName);

    @Select("select file_path filePath,file_name fileName,schedule_info scheduleInfo," +
            "file_type fileType,update_time updateTime  from t_trans_file_info")
    List<TransFileVo> queryKfileListAll();

    @Select("select  " +
            "m.task_id taskId, " +
            "m.task_name taskName," +
            " if(m.run_status=1,'启动','停止') runStatusStr " +
            " from data_etl_task m " +
            "  where m.business_type=#{busType}" +
            " and m.org_id=#{orgId} ")
    List<DataTaskVo> queryTasks(@Param("busType") int busType, @Param("orgId") String orgId);

    @Select("select id from data_etl_dataconnection where org_id=#{orgId}")
    List<Integer> getTopThirdConnectionId(@Param("orgId") String orgId);

    void saveTaskFieldNoteInfo(@Param("noteVo") EtlTaskNoteVo taskFieldsVo);

    void saveTaskFieldNoteDetail(@Param("detailsVo")TaskFieldDetailsVo vo);

    @Delete("delete from data_etl_field_note where taskId=#{taskId}")
    void deleteTaskNoteAndDetail1(@Param("taskId")int taskId);

    @Delete("delete from data_etl_field_detail where taskId=#{taskId}")
    void deleteTaskNoteAndDetail2(@Param("taskId")int taskId);

    @Select("select * from data_etl_field_note")
    List<EtlTaskNoteVo> getEtltaskNotes();

    @Select("select * from data_etl_field_detail where taskId=#{taskId}")
    List<TaskFieldDetailsVo> queryFieldDetailsList(@Param("taskId")int taskId);

    @Select("select count(1) c from data_etl_task t where t.third_tablename =#{alianname} ")
    int getCountByAlianName(@Param("alianname") String alianname);
}
