package com.gtafe.data.center.metadata.item.mapper;

import com.gtafe.data.center.metadata.item.vo.ItemVO;
import com.gtafe.data.center.metadata.item.vo.SubsetTreeVO;
import com.gtafe.framework.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ItemMapper extends BaseMapper {

    List<ItemVO> queryItemList(@Param("sourceId") int sourceId, @Param("itemCodeOritemName") String itemCodeOritemName, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    boolean itemDelete(@Param("subclassCode") String subclassCode, @Param("itemCode") String itemCode);

    boolean itemAdd(ItemVO itemVO);

    boolean itemUpdate(ItemVO itemVO);

    ItemVO queryByCode(@Param("subclassCode") String subclassCode, @Param("itemCode") String itemCode);

    public List<SubsetTreeVO> queryMetaDataTreeInfo(@Param("sourceId") int sourceId);

    List<ItemVO> queryItemListBysubclass(@Param("subclassCode") String subclassCode, @Param("itemCodeOrItemName") String itemCodeOrItemName, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

    String queryTableName(@Param("subclassCode") String subclassCode);

    int existTable(@Param("subclassTableName") String subclassTableName);

    void createTable(@Param("subclassTableName") String subclassTableName);

    int tableifhavedata(@Param("subclassTableName") String subclassTableName);

    void alterTable(@Param("subclassTableName") String subclassTableName, @Param("itemVO") ItemVO itemVO);

    @Select("select * from info_metadata_item where subclass_code=#{subclassCode}")
    List<ItemVO> queryItemListBysubclassCode(@Param("subclassCode") String subclassCode);
}
