package com.gtafe.data.center.information.data.test;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(path = "/common/standardtest")
@Api(value = "DataStandardTestController", protocols = "http")
@CrossOrigin
public class DataStandardTestController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataStandardTestController.class);
    @Resource
    private DataStandardTestServiceImpl dataStandardTestServiceImpl;
    /**
     * 查询子集列表
     * @author 汪逢建
     * @date 2017年11月6日
     */
    @RequestMapping(path = "/querySubclass", method = RequestMethod.POST)
    public List<DataStandardVo> querySubclass(@RequestBody DataTestParam param) {
            
        return dataStandardTestServiceImpl.querySubclass(param.getType(), param.getKeyword());
    }
    
    /**
     * 根据树节点，查询item列表
     * @author 汪逢建
     * @date 2017年11月7日
     */
    @RequestMapping(path = "/queryItemList", method = RequestMethod.GET)
    public @ResponseBody List<DataStandardItemVo> queryItemList(
      @RequestParam(value = "subclassCode", required = false) String subclassCode) {
        
        return dataStandardTestServiceImpl.queryItemList(subclassCode);
    }
    
    
    @RequestMapping(path = "/updateItemList", method = RequestMethod.POST)
    public boolean updateItemList(@RequestParam(value = "subclassCode", required = false)String subclassCode, 
                                      @RequestBody List<Integer> ids){
        return dataStandardTestServiceImpl.updateItemList(subclassCode, ids);
    }
    
    @RequestMapping(path = "/rebuildAllTables", method = RequestMethod.GET)
    public boolean rebuildAllTables(){
        return dataStandardTestServiceImpl.updateAllTable();
    }
}
