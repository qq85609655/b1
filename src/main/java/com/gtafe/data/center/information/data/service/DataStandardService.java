package com.gtafe.data.center.information.data.service;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.information.data.vo.DataStandardVo;


public interface DataStandardService {

    /**
     * 查询信息数据标准组织信息
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    List<DataStandardVo> queryDataOrgList(String keyWord, int sourceId,
                                          String subsetCode, String classCode, int nodeType,
                                          int pageNum, int pageSize);

    /**
     * 查询信息数据标准组织信息,不分页
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    List<DataStandardVo> queryDataOrgListAll(int sourceId, String subsetCode, String classCode, int nodeType);

    /**
     * 查询子集类子类树
     *
     * @author 汪逢建
     * @date 2017年11月7日
     */
    DataStandardVo queryDataOrgTree(int sourceId);

    /**
     * 根据code查询数据
     *
     * @author 汪逢建
     * @date 2017年11月10日
     */
    public DataStandardVo getDataStandardVo(String code);

    /**
     * 新增信息数据标准
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    public boolean addDataStandardVos(int sourceId, String parentCode, List<DataStandardVo> voList, int nodeType);

    /**
     * 修改数据标准
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    public boolean updateDataStandardVo(int sourceId, DataStandardVo dataVo, int nodeType);

    /**
     * 修改数据标准
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    public boolean deleteDataStandardVo(int sourceId, String code, int nodeType);

}
