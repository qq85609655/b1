package com.gtafe.data.center.information.data.test;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.data.center.information.data.vo.DataStandardVo;

public interface DataStandardTestMapper {

    public List<DataStandardVo> querySubclass( @Param("type") int type, @Param("keyword") String keyword, @Param("keyword2") String keyword2);
    public List<DataStandardItemVo> queryItemList(@Param("subclassCode") String subclassCode);
    
    public boolean updatePrimaryKey(@Param("subclassCode") String subclassCode,
                                                     @Param("ids") List<Integer> ids,
                                                     @Param("primary") int primary);
}
