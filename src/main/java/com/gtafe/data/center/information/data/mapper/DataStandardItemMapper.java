package com.gtafe.data.center.information.data.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.data.center.metadata.item.vo.ItemVO;

public interface DataStandardItemMapper {

    public List<DataStandardItemVo> querySubclassItemList(
            @Param("sourceId") int sourceId, @Param("subclassCode") String subclassCode,
            @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    List<DataStandardItemVo> queryItemList(@Param("sourceId") int sourceId, @Param("code") String code, @Param("nodeType") int nodeType,
                                           @Param("keyWord") String keyWord,
                                           @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    /**
     * 查询单个item
     *
     * @author 汪逢建
     * @date 2017年11月7日
     */
    DataStandardItemVo getItemById(@Param("id") int id);


    DataStandardItemVo checkCodeNameRepeat(DataStandardItemVo itemVo);


    boolean insertDataStandardItemVo(@Param("item") DataStandardItemVo item, @Param("sourceId") int sourceId, @Param("userId") int userId);

    boolean updateDataStandardItemVo(@Param("item") DataStandardItemVo item, @Param("sourceId") int sourceId, @Param("userId") int userId);

    boolean deleteDataStandardItemVo(@Param("id") int id, @Param("sourceId") int sourceId);

    @Select("\t     select b.id id,\n" +
            "        b.subclass_code subclassCode,\n" +
            "        b.item_code itemCode,\n" +
            "        b.item_name itemName,\n" +
            "        b.item_comment itemComment,\n" +
            "        b.data_type dataType,\n" +
            "        b.data_length dataLength,\n" +
            "        b.data_primarykey dataPrimarykey,\n" +
            "        b.data_nullable dataNullable,\n" +
            "        b.data_value_source dataValueSource,\n" +
            "        b.data_explain dataExplain,\n" +
            "        b.data_referenced dataReferenced,\n" +
            "        b.selectable selectable　" +
            "        from info_datastandard_item b" +
            "       where b.subclass_code=#{subclassCode} " +
            "            and b.sourceid=#{sourceId}")
    List<ItemVO> queryItemListByParams(@Param("subclassCode") String subclassCode, @Param("sourceId") int sourceId);

    List<Integer> query4Index();


    @Select("\t     select b.id id,\n" +
            "        b.subclass_code subclassCode,\n" +
            "        b.item_code itemCode,\n" +
            "        b.item_name itemName,\n" +
            "        b.item_comment itemComment,\n" +
            "        b.data_type dataType,\n" +
            "        b.data_length dataLength,\n" +
            "        b.data_primarykey dataPrimarykey,\n" +
            "        b.data_nullable dataNullable,\n" +
            "        b.data_value_source dataValueSource,\n" +
            "        b.data_explain dataExplain,\n" +
            "        b.data_referenced dataReferenced,\n" +
            "        b.selectable selectable　" +
            "        from info_datastandard_item b" +
            "       where b.subclass_code=#{subclassCode} " +
            "            and b.sourceid=#{sourceId} ")
    List<DataStandardItemVo> queryItemListBy(@Param("subclassCode") String subclassCode, @Param("sourceId") int sourceId);

    @Select("SELECT\n" +
            "\titem_code\n" +
            "FROM\n" +
            "\tinfo_datastandard_item\n" +
            "WHERE\n" +
            "\tsubclass_code = #{subclassCode} \n" +
            "AND id = (\n" +
            "\tSELECT\n" +
            "\t\tmax(id)\n" +
            "\tFROM\n" +
            "\t\tinfo_datastandard_item where subclass_code =#{subclassCode} \n" +
            ") ")
    String getMaxItemCode(@Param("subclassCode")String subclassCode);
}
