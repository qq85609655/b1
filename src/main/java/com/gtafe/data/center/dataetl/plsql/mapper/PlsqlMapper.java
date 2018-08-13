package com.gtafe.data.center.dataetl.plsql.mapper;

import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PlsqlMapper {

    List<PlsqlVo> queryList(@Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize,
                            @Param("orgIdList") List<String> orgIdList, @Param("nameKey") String nameKey);

    @Select("select * from data_etl_plsql where id=#{id}")
    PlsqlVo getInfoById(@Param("id") int id);

    boolean updateData(@Param("vo") PlsqlVo vo);

    boolean insertData(@Param("vo") PlsqlVo vo);

    @Delete("delete from data_etl_plsql where id=#{id}")
    void deleteById(@Param("id") Integer idd);
}
