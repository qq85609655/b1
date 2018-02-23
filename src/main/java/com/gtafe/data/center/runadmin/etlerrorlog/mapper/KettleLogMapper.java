package com.gtafe.data.center.runadmin.etlerrorlog.mapper;


import com.gtafe.data.center.runadmin.etlerrorlog.vo.ErrorLogVo;
import com.gtafe.data.center.runadmin.etlerrorlog.vo.KettleLogVO;
import com.gtafe.framework.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface KettleLogMapper extends BaseMapper {

    List<KettleLogVO> list(@Param("businessType") int businessType,
                           @Param("startTime") String startTime, @Param("endTime") String endTime,
                           @Param("pageNumKey") int pageNum,
                           @Param("pageSizeKey") int pageSize,
                           @Param("transName") String transName,
                           @Param("orgIdList") List<Integer> orgIdList);

    List<ErrorLogVo> queryErrorList(@Param("pageNumKey") int pageNum,
                                    @Param("pageSizeKey") int pageSize,
                                    @Param("channel_id") String channel_id);

    @Select("select * from Kettle_log where errors=1 and logdate >=#{s} and logdate <=#{e} group by TRANSNAME ")
    List<KettleLogVO> queryYesterdayErrorLogs(@Param("s") String s, @Param("e") String e);

    List<ErrorLogVo> queryErrorList(@Param("channel_id") String channel_id);


    List<KettleLogVO> queryLstErrorList(@Param("taskId")int taskId);

    @Select("   SELECT\n" +
            "        log.id_batch id,\n" +
            "        m.task_name transname,\n" +
            "        (case log.STATUS\n" +
            "        WHEN 'start' THEN '开始'\n" +
            "        WHEN 'end' THEN '结束'\n" +
            "        WHEN 'stop' THEN '停止'\n" +
            "        WHEN 'running' THEN '运行中'\n" +
            "        WHEN 'paused' THEN '暂停'\n" +
            "        END )status,\n" +
            "        log.LINES_INPUT line_input,\n" +
            "        (select count(1) from kettle_error_log ll where ll.channel_id=log.channel_id ) line_rejected,\n" +
            "        log.startdate startdate,\n" +
            "        log.enddate enddate,\n" +
            "        log.logdate logdate,\n" +
            "        log.log_field log_field,\n" +
            "        log.channel_id channel_id,\n" +
            "        o.org_name orgName,\n" +
            "        log.errors errors,\n" +
            "        o.id orgId\n" +
            "        FROM\n" +
            "        kettle_log log,\n" +
            "        data_etl_task m,\n" +
            "        sys_org o\n" +
            "        WHERE\n" +
            "        log.transname = m.task_id\n" +
            "        AND m.org_id = o.id and  channel_id =#{logChannelId}")
    KettleLogVO queryKettleVOByChannelId(@Param("logChannelId") String logChannelId);
}
