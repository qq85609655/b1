
package com.gtafe.data.center.dataetl.datatask.mapper;

import java.util.List;

import com.gtafe.data.center.dataetl.datatask.vo.TransFileVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.runadmin.nodewatch.vo.EtlTaskStatus;
import com.gtafe.framework.base.mapper.BaseMapper;

public interface DataTaskMapper extends BaseMapper {

    List<DataTaskVo> queryList(@Param("collectionId") int collectionId, @Param("orgIdList") List<Integer> orgIdList,
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
    int checkTaskNameRepeat(@Param("taskId") Integer taskId, @Param("taskName") String taskName, @Param("orgId") int orgId, @Param("businessType") int businessType);

    int insertDataTask(@Param("taskVo") DataTaskVo taskVo, @Param("userId") Integer userId);

    boolean updateDataTask(@Param("taskVo") DataTaskVo taskVo, @Param("userId") Integer userId);

    /**
     * @param runStatus 1启动，0停止
     * @author 汪逢建
     * @date 2017年11月16日
     */
    boolean updateDataTaskStatus(@Param("taskIds") List<Integer> taskIds, @Param("runStatus") int runStatus, @Param("userId") Integer userId);

    boolean deleteTaskSteps(@Param("taskId") Integer taskId);

    boolean insertTaskSteps(@Param("taskId") Integer taskId, @Param("steps") List<String> steps, @Param("userId") Integer userId);

    @Select("select step_detail from data_etl_task_step where task_id=#{taskId} order by serial asc")
    List<String> getTaskSteps(@Param("taskId") Integer taskId);

    @Delete("delete from data_etl_task where task_id= #{taskId}")
    void deleteTaskById(@Param("taskId") Integer taskId);

    List<DataTaskVo> queryListByOrgs(@Param("orgIds") List<Integer> orgIds, @Param("businessType") int businessType);

    @Delete("delete from sys_user_task where user_id=#{userId}")
    void clearUserTaskRelation(@Param("userId") Integer userId);

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

    List<EtlTaskStatus> queryTaskStatusList(@Param("orgIdList") List<Integer> orgIdList,
                                            @Param("pageNumKey") int pageNum,
                                            @Param("pageSizeKey") int pageSize,
                                            @Param("busType") int busType);

    List<TransFileVo> queryKfileList(@Param("fileName") String fileName, @Param("fileType") String fileType,  @Param("pageNumKey")int pageNum, @Param("pageSizeKey")int pageSize);

    @Select("select file_path,file_id, from t_trans_file_info where file_id=#{fileId}")
    TransFileVo findEtlFileInfoById(@Param("fileId") int fileId);
}
