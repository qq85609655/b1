package com.gtafe.data.center.information.data.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gtafe.data.center.information.data.mapper.DataStandardMapper;
import com.gtafe.data.center.information.data.service.DataStandardItemService;
import com.gtafe.data.center.information.data.service.DataTableService;
import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.framework.base.controller.BaseController;

@Service
public class DataStandardTestServiceImpl extends BaseController {
    @Resource
    private DataStandardTestMapper dataStandardTestMapper;
    @Resource
    private DataStandardItemService dataStandardItemServiceImpl;
    @Resource
    private DataStandardMapper dataStandardMapper;
    @Resource
    private SysConfigService sysConfigServiceImpl;
    @Resource
    private DataTableService dataTableServiceImpl;

    public List<DataStandardVo> querySubclass(int type, String keyword) {
        
        return dataStandardTestMapper.querySubclass(type,keyword, keyword!=null ? keyword.toUpperCase() : null);
    }
    
    public List<DataStandardItemVo> queryItemList( String subclassCode) {
        
        return dataStandardTestMapper.queryItemList(subclassCode);
    }
    
    public boolean updateItemList(String subclassCode,  List<Integer> ids){
        DataStandardVo subclassVo = dataStandardMapper.getDataStandardVo(subclassCode);
        if(subclassVo == null) {
            return false;
        }
        dataStandardTestMapper.updatePrimaryKey(subclassCode, null, 0);
        if(ids!=null && !ids.isEmpty()) {
            dataStandardTestMapper.updatePrimaryKey(subclassCode, ids, 1);    
        }
        dataTableServiceImpl.createTable(subclassCode);
        return true;
    }
    
    public boolean updateAllTable(){
        return sysConfigServiceImpl.initCenterDataBase();
    }
}
