package com.gtafe.data.center.dataetl.plsql.service;

import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;

import java.util.List;

public interface PlsqlService {

    List<PlsqlVo> queryList(int pageNum, int pageSize, String orgIds, String nameKey);

    boolean checkOut(PlsqlVo vo);

    PlsqlVo getInfoById(int id);

    boolean insertData(PlsqlVo vo);

    boolean updateData(PlsqlVo vo);

    void deleteBatchs(List<Integer> idList);
}
