package com.gtafe.data.center.dataetl.plsql.mapper;

import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.plsql.vo.ColumnDetail;
import com.gtafe.data.center.dataetl.plsql.vo.ItemDetailVo;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PlsqlMapper {

    List<PlsqlVo> queryList(@Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize,
                            @Param("orgIdList") List<String> orgIdList, @Param("nameKey") String nameKey);

    @Select("select * from data_etl_plsql where id=#{id}")
    PlsqlVo getInfoById(@Param("id") Integer id);

    boolean updateData(@Param("vo") PlsqlVo vo);

    int insertData(@Param("vo") PlsqlVo vo);

    @Delete("delete from data_etl_plsql where id=#{id}")
    void deleteById(@Param("id") Integer idd);

    @Insert("insert into data_etl_plsql_detail(" +
            "columnLabel_,displaySize_,typeName_,precision_," +
            "scale_,isAutoIncrement_,isCurrency_,isNullable_," +
            "isReadOnly_,sqlId,columnLabelName)" +
            " values(#{vo.columnLabel},#{vo.displaySize},#{vo.typeName},#{vo.preci},#{vo.scal}," +
            "#{vo.isAutoIncrement},#{vo.isCurrency},#{vo.isNullable},#{vo.isReadOnly},#{vo.sqlId},#{vo.columnLabel})")
    void insertItemDetail(@Param("vo") ItemDetailVo vo);

    @Delete("delete from data_etl_plsql_detail where sqlId=#{idd}")
    void deleteItemsById(@Param("idd") Integer idd);

    @Select("select columnLabel_ columnLabel,typeName_ typeName  from data_etl_plsql_detail where  sqlId=#{idd}")
    List<ItemDetailVo> getItemDetailVos(@Param("idd") int id);

    @Select("select  * from data_etl_plsql where orgId=#{orgId} ")
    List<PlsqlVo> getItemsByOrgId(@Param("orgId") String orgId);

    List<ItemDetailVo> getItemDetailVosByAlianName(@Param("aliansName") String table, @Param("orgId") String orgId);

    int checkAliansNameRepeat(@Param("aliansName") String aliansName, @Param("orgId") String orgId, @Param("id") int id);

    @Select("select content from data_etl_plsql where aliansName=#{aliansName} and orgId=#{orgId}")
    PlsqlVo getInfoByAliansName(@Param("aliansName") String AliansName, @Param("orgId") String orgId);

    @Update("update data_etl_plsql_detail set columnLabelName=#{vo.columnLabelName} where id=#{vo.id}  ")
    void upDateColumn(@Param("vo") ColumnDetail vo);

    List<ItemDetailVo> queryColunDetailList(@Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize, @Param("sqlId") int sqlId);
}
