package com.gtafe.data.center.runadmin.alertpush.mapper;

import com.gtafe.data.center.runadmin.alertpush.vo.AlertPush;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/11/6
 * @Description:
 */
public interface AlertPushMapper {

    List<AlertPush> list(
            @Param("pageNumKey") int pageNum,
            @Param("pageSizeKey") int pageSize,
            @Param("isPush") int isPush,
            @Param("orgIdList") List<Integer> orgIdList);

    @Select("select ut.task_id taskId from sys_user_task ut where user_id=#{userId} and bus_type=#{businessType}")
    List<Integer> queryUserMapTaskIds(@Param("userId") int userId, @Param("businessType")int businessType);

    @Select("select ut.user_id userId from sys_user_task ut  where task_id=#{taskId}")
    List<Integer> queryUserIdsByTaskId(@Param("taskId")int transname);

    @Select( "select ut.user_id userId from sys_user_task ut, data_etl_task t  where ut.task_id=t.task_id and t.task_name=#{transname} ")
    List<Integer> queryUserIdsByTransName(@Param("transname")String transname);
}
