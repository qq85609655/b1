package com.gtafe.data.center.common.index.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.vo.IndexVo;
import com.gtafe.data.center.common.index.service.IndexService;
import com.gtafe.data.center.information.data.mapper.DataStandardItemMapper;
import com.gtafe.data.center.system.user.mapper.SysUserAuthMapper;
import com.gtafe.framework.base.service.BaseService;

/**
 * 首页的实现类
 */

@Service
public class IndexServiceImpl extends BaseService implements IndexService {

    @Resource
    private DataStandardItemMapper dataStandardItemMapper;


    @Override
    public IndexVo queryIndex() {
        List<Integer> list = this.dataStandardItemMapper.query4Index();
        IndexVo result = new IndexVo();
        result.setA1(list.get(0));
        result.setA2(list.get(1));
        result.setA3(list.get(2));
        result.setA4(list.get(3));
        result.setA5(list.get(4));
        result.setA6(list.get(5));
        return result;
    }
}
