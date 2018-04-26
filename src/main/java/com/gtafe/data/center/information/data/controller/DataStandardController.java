package com.gtafe.data.center.information.data.controller;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.information.data.service.DataStandardService;
import com.gtafe.data.center.information.data.vo.DataStandardParam;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.framework.base.exception.OrdinaryException;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(path = "/datastandard")
@Api(value = "DataStandardController", protocols = "http")
@CrossOrigin
public class DataStandardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataStandardController.class);
    public static final int Type_Subset = 1;
    public static final int Type_Class = 2;
    public static final int Type_Subclass = 3;
    @Resource
    private DataStandardService dataStandardServiceImpl;

    /**
     * 查询子集列表
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    @RequestMapping(path = "/querySubsetList", method = RequestMethod.POST)
    public PageInfo<DataStandardVo> querySubsetList(@RequestParam(value = "sourceId", required = false) int sourceId,
                                                    @RequestBody DataStandardParam param) {
        param.setSourceId(sourceId);
        List<DataStandardVo> result = dataStandardServiceImpl
                .queryDataOrgList(param.getKeyWord(), sourceId, null, null, Type_Subset, param.getPageNum(), param.getPageSize());
        return new PageInfo<DataStandardVo>(result);
    }



    /**
     * 查询类列表
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    @RequestMapping(path = "/queryClassList", method = RequestMethod.POST)
    public PageInfo<DataStandardVo> queryClassList(@RequestParam(value = "sourceId", required = false) int sourceId,
                                                   @RequestBody DataStandardParam param) {
        param.setSourceId(sourceId);
        List<DataStandardVo> result = dataStandardServiceImpl
                .queryDataOrgList(param.getKeyWord(), sourceId, param.getSubsetCode(), null, Type_Class, param.getPageNum(), param.getPageSize());
        return new PageInfo<DataStandardVo>(result);
    }

    /**
     * 查询子类列表
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    @RequestMapping(path = "/querySubclassList", method = RequestMethod.POST)
    public PageInfo<DataStandardVo> querySubclassList(@RequestParam(value = "sourceId", required = false) int sourceId,
                                                      @RequestBody DataStandardParam param) {
        param.setSourceId(sourceId);
        List<DataStandardVo> result = dataStandardServiceImpl
                .queryDataOrgList(param.getKeyWord(), sourceId, param.getSubsetCode(), param.getClassCode(), Type_Subclass, param.getPageNum(), param.getPageSize());
        return new PageInfo<DataStandardVo>(result);
    }

    /**
     * 查询子集类子类下拉列表
     *
     * @author 汪逢建
     * @date 2017年11月6日
     * @nodeType 类型，需要查询的数据的节点类型，非父节点类型
     */
    @RequestMapping(path = "/queryDataOrgListAll", method = RequestMethod.GET)
    public List<DataStandardVo> queryDataOrgListAll(
            @RequestParam(value = "subsetCode", required = false) String subsetCode,
            @RequestParam(value = "classCode", required = false) String classCode,
            @RequestParam(value = "sourceId", required = false) int sourceId,
            @RequestParam(value = "nodeType", required = false) int nodeType) {
        if (nodeType > 3 || nodeType < 1) {
            throw new OrdinaryException("参数错误");
        }

        return dataStandardServiceImpl.queryDataOrgListAll(sourceId, subsetCode, classCode, nodeType);
    }


    /**
     * 查询子集类子类树
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    @RequestMapping(path = "/queryDataOrgTree", method = RequestMethod.GET)
    public DataStandardVo queryDataOrgTree(@RequestParam(value = "sourceId", required = false) int sourceId) {
        return dataStandardServiceImpl.queryDataOrgTree(sourceId);
    }


    @RequestMapping(path = "/addSubsetVos", method = RequestMethod.POST)
    public boolean addSubsetVos(@RequestParam(value = "sourceId", required = false) int sourceId,
                                @RequestParam(value = "parentCode", required = false) String parentCode,
                                @RequestBody List<DataStandardVo> voList) {

        return dataStandardServiceImpl.addDataStandardVos(sourceId, parentCode, voList, Type_Subset);
    }

    @RequestMapping(path = "/addClassVos", method = RequestMethod.POST)
    public boolean addClassVos(@RequestParam(value = "sourceId", required = false) int sourceId,
                               @RequestParam(value = "parentCode", required = false) String parentCode,
                               @RequestBody List<DataStandardVo> voList) {

        return dataStandardServiceImpl.addDataStandardVos(sourceId, parentCode, voList, Type_Class);
    }

    @RequestMapping(path = "/addSubClassVos", method = RequestMethod.POST)
    public boolean addSubClassVos(@RequestParam(value = "sourceId", required = false) int sourceId,
                                  @RequestParam(value = "parentCode", required = false) String parentCode,
                                  @RequestBody List<DataStandardVo> voList) {

        return dataStandardServiceImpl.addDataStandardVos(sourceId, parentCode, voList, Type_Subclass);
    }


    @RequestMapping(path = "/updateSubsetVo", method = RequestMethod.POST)
    public boolean updateSubsetVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                  @RequestBody DataStandardVo dataVo) {
        return dataStandardServiceImpl.updateDataStandardVo(sourceId, dataVo, Type_Subset);
    }

    @RequestMapping(path = "/updateClassVo", method = RequestMethod.POST)
    public boolean updateClassVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                 @RequestBody DataStandardVo dataVo) {

        return dataStandardServiceImpl.updateDataStandardVo(sourceId, dataVo, Type_Class);
    }


    @RequestMapping(path = "/updateSubclassVo", method = RequestMethod.POST)
    public boolean updateSubclassVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                    @RequestBody DataStandardVo dataVo) {
        return dataStandardServiceImpl.updateDataStandardVo(sourceId, dataVo, Type_Subclass);
    }

    @RequestMapping(path = "/deleteSubsetVo", method = RequestMethod.GET)
    public boolean deleteSubsetVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                  @RequestParam(value = "code", required = false) String code) {

        return dataStandardServiceImpl.deleteDataStandardVo(sourceId, code, Type_Subset);
    }

    @RequestMapping(path = "/deleteClassVo", method = RequestMethod.GET)
    public boolean deleteClassVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                 @RequestParam(value = "code", required = false) String code) {
        return dataStandardServiceImpl.deleteDataStandardVo(sourceId, code, Type_Class);
    }


    @RequestMapping(path = "/deleteSubclassVo", method = RequestMethod.GET)
    public boolean deleteSubclassVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                    @RequestParam(value = "code", required = false) String code) {
        return dataStandardServiceImpl.deleteDataStandardVo(sourceId, code, Type_Subclass);
    }

}
