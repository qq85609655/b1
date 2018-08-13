package com.gtafe.data.center.dataetl.plsql.mapper;

import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlsqlMapper {

    List<PlsqlVo> queryList(@Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize,
                            @Param("orgIdList") List<String> orgIdList, @Param("nameKey") String nameKey);
}
