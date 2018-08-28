package com.gtafe.data.center.dataetl.plsql.service;

import com.gtafe.data.center.dataetl.plsql.vo.ColumnDetail;
import com.gtafe.data.center.dataetl.plsql.vo.ItemDetailVo;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;
import com.gtafe.data.center.dataetl.plsql.vo.SearchResultVo;

import java.util.List;

public interface PlsqlService {

    List<PlsqlVo> queryList(int pageNum, int pageSize, String orgIds, String nameKey);

    boolean checkOut(PlsqlVo vo);

    PlsqlVo getInfoById(String id);

    boolean insertData(PlsqlVo vo);

    boolean updateData(PlsqlVo vo);

    void deleteBatchs(List<Integer> idList);

    SearchResultVo runNow(int id);

    boolean upDateColumn(ColumnDetail vo);

    List<ItemDetailVo> queryColunDetailList(int pageNum, int pageSize, int sqlId);
}
