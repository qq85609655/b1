package com.gtafe.data.center.dataetl.plsql.service.impl;

import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.plsql.mapper.PlsqlMapper;
import com.gtafe.data.center.dataetl.plsql.service.PlsqlService;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;
import com.gtafe.framework.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlsqlServiceImpl implements PlsqlService {

    @Autowired
    private PlsqlMapper plsqlMapper;


    @Override
    public List<PlsqlVo> queryList(int pageNum, int pageSize, String orgIds, String nameKey) {
        List<String> orgIdList = StringUtil.splitListString(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, PlsqlVo.class);
        }
        return plsqlMapper.queryList(pageNum, pageSize, orgIdList, nameKey);
    }
}
