package com.gtafe.data.center.system.log.mapper;

import com.gtafe.data.center.system.log.vo.EmailSendLog;
import com.gtafe.data.center.system.log.vo.LogVo;
import com.gtafe.framework.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogMapper extends BaseMapper {


    /**
     * 保存 入库
     *
     * @param logVo
     */
    void saveEntity(LogVo logVo);

    List<LogVo> queryList4Login(@Param("pageNumKey") int pageNum,
                          @Param("pageSizeKey") int pageSize,
                          @Param("startTime") String startTime,
                          @Param("endTime") String endTime,
                          @Param("operRes") int operRes,
                          @Param("orgIdList") List<String> orgIdList,
                          @Param("keyWord") String keyWord);

    List<LogVo> queryList4Oper(@Param("pageNumKey") int pageNum,
                          @Param("pageSizeKey") int pageSize,
                          @Param("startTime") String startTime,
                          @Param("endTime") String endTime,
                          @Param("operRes") int operRes,
                          @Param("operModuleIdList") List<Integer> operModuleIdList,
                          @Param("keyWord") String keyWord);



    List<EmailSendLog> queryList4EmailSend(@Param("pageNumKey") int pageNum,
                                           @Param("pageSizeKey") int pageSize,  @Param("startTime") String startTime,
                                           @Param("endTime") String endTime, @Param("module") String module,  @Param("keyWord")String keyWord);

    void saveEmailSendLog(EmailSendLog emailSendLog);
}
