package com.gtafe.data.center.information.data.controller;

import java.util.List;

import javax.annotation.Resource;

import com.gtafe.data.center.dataetl.plsql.vo.SearchResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.TableFieldVo;
import com.gtafe.data.center.information.data.service.DataStandardItemService;
import com.gtafe.data.center.information.data.vo.DataStandardItemParam;
import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(path = "/datastandarditem")
@Api(value = "DataStandardController", protocols = "http")
@CrossOrigin
public class DataStandardItemController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataStandardItemController.class);
    public static final int Type_Subset = 1;
    public static final int Type_Class = 2;
    public static final int Type_Subclass = 3;
    @Resource
    private DataStandardItemService dataStandardItemServiceImpl;

    /**
     * 查询子集列表
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    @RequestMapping(path = "/querySubclassItemList", method = RequestMethod.GET)
    public PageInfo<DataStandardItemVo> querySubclassItemList(
            @RequestParam(value = "sourceId", required = false) int sourceId,
            @RequestParam(value = "subclassCode", required = false) String subclassCode,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        List<DataStandardItemVo> result = dataStandardItemServiceImpl
                .querySubclassItemList(sourceId, subclassCode, pageNum, pageSize);
        return new PageInfo<DataStandardItemVo>(result);
    }

    /**
     * 根据树节点，查询item列表
     *
     * @author 汪逢建
     * @date 2017年11月7日
     */
    @RequestMapping(path = "/queryItemList", method = RequestMethod.POST)
    public PageInfo<DataStandardItemVo> queryItemList(
            @RequestParam(value = "sourceId", required = false) int sourceId,
            @RequestBody DataStandardItemParam param) {
        param.setSourceId(sourceId);
        List<DataStandardItemVo> result = dataStandardItemServiceImpl
                .queryItemList(sourceId, param.getCode(), param.getNodeType(), param.getKeyWord(), param.getPageNum(), param.getPageSize());
        return new PageInfo<DataStandardItemVo>(result);
    }


    @RequestMapping(path = "/queryItemListAll", method = RequestMethod.GET)
    List<TableFieldVo> queryItemListAll(@RequestParam(value = "sourceId", required = false) int sourceId,
                                        @RequestParam(value = "subclassCode", required = false) String subclassCode) {
        return this.dataStandardItemServiceImpl.queryItemListAll(sourceId, subclassCode);
    }


    @RequestMapping(path = "/getItemVo", method = RequestMethod.GET)
    public DataStandardItemVo getItemVo(@RequestParam(value = "id", required = false) int id) {
        return dataStandardItemServiceImpl.getItemById(id);
    }

    @RequestMapping(path = "/addItemVo", method = RequestMethod.POST)
    public boolean addItemVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                             @RequestBody DataStandardItemVo itemVo) {
        return dataStandardItemServiceImpl.addItemVo(sourceId, itemVo);
    }

    @RequestMapping(path = "/updateItemVo", method = RequestMethod.POST)
    public boolean updateItemVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                @RequestBody DataStandardItemVo itemVo) {
        return dataStandardItemServiceImpl.updateItemVo(sourceId, itemVo);
    }

    @RequestMapping(path = "/deleteItemVo", method = RequestMethod.GET)
    public boolean deleteItemVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                @RequestParam(value = "id", required = false) int id) {
        return dataStandardItemServiceImpl.deleteItemVo(sourceId, id);
    }


    @RequestMapping(path = "/queryDataShow/{code}", method = RequestMethod.POST)
    public @ResponseBody
    SearchResultVo queryDataShow(@PathVariable("code") String code) {
        return this.dataStandardItemServiceImpl.queryDataShow(code);
    }

}
