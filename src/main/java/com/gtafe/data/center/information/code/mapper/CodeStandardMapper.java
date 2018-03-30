package com.gtafe.data.center.information.code.mapper;

import java.util.List;

import com.gtafe.data.center.information.code.vo.MysqlTableVo;
import com.gtafe.data.center.information.code.vo.TableEntity;
import org.apache.ibatis.annotations.*;

import com.gtafe.data.center.information.code.vo.CodeInfoVo;
import com.gtafe.data.center.information.code.vo.CodeNodeVo;

public interface CodeStandardMapper {

    public List<CodeInfoVo> queryCodeList(@Param("keyWord") String keyWord, @Param("nodeId") int nodeId, @Param("sourceId") int sourceId,
                                          @Param("nodeType") int nodeType,
                                          @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    public List<CodeNodeVo> queryCodeNodes(@Param("sourceId") int sourceId);

    @Insert("insert into info_codestandard_node(code,parentnode_id,name,description,node_type,sourceid,creater,updater) " +
            "values(#{code},#{parentnodeId},#{name},#{description},#{nodeType},#{sourceId},#{creater},#{updater}) ")
    void saveNodeVo(CodeNodeVo vo);

    @Select("select code,parentnode_id parentnodeId,name,description,node_type nodeType,sourceid sourceId from info_codestandard_node where node_id=#{nodeId}")
    CodeNodeVo queryNodeEntity(@Param("nodeId") int nodeId);

    @Update("update info_codestandard_node set name=#{name},description=#{description},updater=#{userId}  where node_id=#{nodeId}")
    boolean updateNode(@Param("nodeId") int nodeId, @Param("name") String name, @Param("description") String description, @Param("userId") int userId);

    //根据节点id 删除代码
    @Delete("delete from info_codestandard_code where node_id=#{nodeId} ")
    boolean deleteCodesBy(@Param("nodeId") int nodeId);

    //根据主键删除节点
    @Delete("delete from info_codestandard_node where node_id=#{nodeId} ")
    boolean deleteNodeBy(@Param("nodeId") int nodeId);

    boolean saveCodeVo(CodeInfoVo v);

    @Update("update info_codestandard_code set name=#{name},description=#{description},updater=#{updater},updatetime=now()  where code_id=#{codeId}")
    boolean updateCodeVo(CodeInfoVo v);

    @Delete("delete from info_codestandard_code where code_id=#{codeId} ")
    boolean deleteCodeByCodeId(@Param("codeId") int codeId);

    List<CodeNodeVo> queryCodeNodesByParentId(@Param("parentId") String parentId);

    List<CodeInfoVo> queryCodeList2(@Param("keyWord") String keyWord, @Param("parentId") String parentId, @Param("sourceId") int sourceId, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    CodeInfoVo queryCodeEntity(@Param("codeId") int codeId);


    @Select(" SELECT\n" +
            "        c.code_id codeId,\n" +
            "        c.node_id nodeId,\n" +
            "        c.code,\n" +
            "        c.name,\n" +
            "        c.description\n" +
            "        FROM info_codestandard_code c where c.node_id=#{nodeId} ")
    List<CodeInfoVo> queryCodeALL(@Param("nodeId") int nodeId);


    List<TableEntity> queryAllCenterTableList(@Param("tableName") String tableName,
                                              @Param("tableType") String tableType,
                                              @Param("pageNumKey") int pageNum,
                                              @Param("pageSizeKey") int pageSize);
}
