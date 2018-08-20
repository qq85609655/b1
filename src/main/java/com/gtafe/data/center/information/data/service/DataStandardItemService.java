package com.gtafe.data.center.information.data.service;

import java.util.List;

import com.gtafe.data.center.dataetl.datatask.vo.rule.TableFieldVo;
import com.gtafe.data.center.dataetl.plsql.vo.SearchResultVo;
import com.gtafe.data.center.information.data.vo.DataStandardItemVo;

public interface DataStandardItemService {

    /**
     * 查询单个子类下的字段
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    List<DataStandardItemVo> querySubclassItemList(int sourceId, String subclassCode,
                                                   int pageNum, int pageSize);

    List<TableFieldVo> queryItemListAll(int sourceId, String subclassCode);

    /**
     * 查询单个子类下的字段
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    List<DataStandardItemVo> queryItemList(int sourceId, String code, int nodeType,
                                           String keyWord, int pageNum, int pageSize);

    /**
     * 查询单个item
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    DataStandardItemVo getItemById(int id);

    /**
     * 添加item
     *
     * @author 汪逢建
     * @date 2017年11月9日
     */
    boolean addItemVo(int sourceId, DataStandardItemVo itemVo);

    boolean updateItemVo(int sourceId, DataStandardItemVo itemVo);

    boolean deleteItemVo(int sourceId, int id);

    /**
     * 重建表
     *
     * @author 汪逢建
     * @date 2017年12月19日
     */
    boolean rebuildSubclassTable(String subclassCode);

    SearchResultVo queryDataShow( String code);
}
