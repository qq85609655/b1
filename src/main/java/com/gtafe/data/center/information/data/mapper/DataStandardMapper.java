package com.gtafe.data.center.information.data.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.information.data.vo.DataStandardVo;
import org.apache.ibatis.annotations.Select;

public interface DataStandardMapper {

    public List<DataStandardVo> queryDataOrgList(
            @Param("keyWord") String keyWord, @Param("sourceId") int sourceId,
            @Param("subsetCode") String subsetCode, @Param("classCode") String classCode, @Param("nodeType") int nodeType,
            @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    public List<DataStandardVo> queryDataOrgListAll(
            @Param("sourceId") int sourceId,
            @Param("subsetCode") String subsetCode, @Param("classCode") String classCode, @Param("nodeType") int nodeType);

    public List<DataStandardVo> queryDataOrgTreeVos(@Param("sourceId") int sourceId);

    /**
     * 根据code查询数据
     *
     * @author 汪逢建
     * @date 2017年11月10日
     */
    public DataStandardVo getDataStandardVo(@Param("code") String code);

    /**
     * 检查code重复
     *
     * @author 汪逢建
     * @date 2017年11月10日
     */
    public List<String> checkDataCodeRepeat(@Param("codeList") List<String> codeList);

    /**
     * 检查名称重复
     *
     * @author 汪逢建
     * @date 2017年11月10日
     */
    public List<String> checkDataNameRepeat(@Param("nameList") List<String> nameList, @Param("parentCode") String parentCode, @Param("code") String code);

    /**
     * 检查表名重复
     *
     * @author 汪逢建
     * @date 2017年11月10日
     */
    public List<String> checkDataTablenameRepeat(@Param("tablenameList") List<String> tablenameList);

    /**
     * 插入数据
     *
     * @author 汪逢建
     * @date 2017年11月10日
     */
    public boolean insertDataStandardVos(@Param("voList") List<DataStandardVo> voList, @Param("sourceId") int sourceId, @Param("userId") int userId);

    /**
     * 更新数据
     *
     * @author 汪逢建
     * @date 2017年11月10日
     */
    public boolean updateDataStandardVo(@Param("dataVo") DataStandardVo dataVo, @Param("sourceId") int sourceId, @Param("userId") int userId);

    /**
     * 查询下级节点情况
     *
     * @author 汪逢建
     * @date 2017年11月10日
     */
    public List<DataStandardVo> getChildDataStandardVos(@Param("parentCode") String parentCode, @Param("sourceId") int sourceId);

    /**
     * 删除节点
     *
     * @author 汪逢建
     * @date 2017年11月10日
     */
    public boolean deleteDataStandardVo(@Param("code") String code, @Param("sourceId") int sourceId);

    @Select("      SELECT\n" +
            "            code,\n" +
            "            parent_code parentCode,\n" +
            "            code_name name,\n" +
            "            description description,\n" +
            "            tablename tableName,\n" +
            "            sourceid sourceId,\n" +
            "            node_type nodeType\n" +
            "\t\tFROM\n" +
            "\t\t    info_datastandard_org where sourceid=#{sourceId} and node_type=3 ")
    List<DataStandardVo> queryAll(@Param("sourceId") int sourceId);
}
