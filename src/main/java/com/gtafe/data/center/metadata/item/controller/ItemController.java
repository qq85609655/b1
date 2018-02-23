package com.gtafe.data.center.metadata.item.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.information.data.service.DataStandardItemService;
import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.data.center.metadata.item.service.IItemService;
import com.gtafe.data.center.metadata.item.vo.ItemVO;
import com.gtafe.data.center.metadata.item.vo.SubsetTreeVO;
import com.gtafe.data.center.metadata.subclass.vo.SubclassVO;
import com.gtafe.framework.base.controller.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(path = "/item")
@Api(value = "ItemController", protocols = "http")
@CrossOrigin
public class ItemController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Resource
    private IItemService itemServiceImpl;

    /**
     * queryItemList:分页查询元数据信息(跟初始化元数据查询用同一个方法). <br/>
     *
     * @param itemCodeOritemName 数据项名或中文名称
     * @param pageNum            页码
     * @param pageSize           每一页数据数量
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */
    @RequestMapping(path = "/query/{sourceId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询元数据或初始化查询", notes = "元数据查询或初始化查询")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    PageInfo<ItemVO> queryItemList(
            @ApiParam(name = "sourceId", value = "标准来源id", required = true) @PathVariable(name = "sourceId", required = true) int sourceId,
            @ApiParam(name = "itemCodeOritemName", value = "数据项名或中文名称", required = false) @RequestParam(value = "itemCodeOritemName", required = false) String itemCodeOritemName,
            @ApiParam(name = "pageNum", value = "页码", required = false, defaultValue = "1") @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ApiParam(name = "pageSize", value = "每一页数据数量", required = false, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        LOGGER.debug("call ItemController.queryItemList method");
        //对于入参值先做截取两端的可能存在的空格问题
        if (null != itemCodeOritemName) {
            itemCodeOritemName = itemCodeOritemName.trim();
        }
        List<ItemVO> result = itemServiceImpl.queryItemList(sourceId, itemCodeOritemName, pageNum, pageSize);
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<ItemVO>(result);
    }
    //TODO:迭代二再处理删除时判定是否已映射的问题

    /**
     * itemDelete:删除元数据信息. <br/>
     *
     * @param subclassCode
     * @param itemCode
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */
    @RequestMapping(path = "/{subclassCode}/{itemCode}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除元数据", notes = "删除元数据管理，根据subclassCode和itemCode删除元数据")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否删除成功")})
    public @ResponseBody
    boolean itemDelete(
            @ApiParam(name = "subclassCode", value = "子类编号", required = true) @PathVariable(name = "subclassCode") String subclassCode,
            @ApiParam(name = "itemCode", value = "元数据编号", required = true) @PathVariable(name = "itemCode") String itemCode) {
        LOGGER.debug("Delete item subclassCode = {}, itemCode = {}", subclassCode, itemCode);
        // 根据子类编号查出表名，再根据表名查看是否已经创建
        // 表不存在则可以删除
        if (!StringUtils.isEmpty(this.itemServiceImpl
                .queryTableName(subclassCode))) {
            if (0 == (this.itemServiceImpl.existTable(this.itemServiceImpl
                    .queryTableName(subclassCode)))) {
                return this.itemServiceImpl.itemDelete(subclassCode, itemCode);
            } else { //对应的表已存在，不能删除
                return false;
            }
        }

        return this.itemServiceImpl.itemDelete(subclassCode, itemCode);
    }

    /**
     * itemUpdate:修改元数据信息. <br/>
     *
     * @param subclassCode
     * @param itemCode
     * @param itemVO
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */

    @RequestMapping(path = "/{subclassCode}/{itemCode}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改元数据", notes = "修改元数据管理，只能修改中文名称和描述")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否修改成功")})
    public @ResponseBody
    boolean itemUpdate(
            @ApiParam(name = "subclassCode", value = "子类编号", required = true) @PathVariable(name = "subclassCode", required = true) String subclassCode,
            @ApiParam(name = "itemCode", value = "元数据编号", required = true) @PathVariable(name = "itemCode", required = true) String itemCode, @RequestBody ItemVO itemVO) {
        LOGGER.debug("Update item subclassCode = {}, itemCode = {}", subclassCode, itemCode);
        itemVO.setSubclassCode(subclassCode);
        itemVO.setItemCode(itemCode);
        return this.itemServiceImpl.itemUpdate(itemVO);
    }

    /**
     * itemAdd:新增元数据信息. <br/>
     *
     * @param itemVO
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */

    @RequestMapping(path = "", method = RequestMethod.POST)
    @ApiOperation(value = "新增元数据", notes = "新增元数据管理")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否新增成功")})
    public @ResponseBody
    boolean itemAdd(
            @ApiParam(name = "itemVO", value = "元数据信息", required = true) @RequestBody ItemVO itemVO) {
        LOGGER.debug("Query item: itemVO = {}", itemVO);
        return  this.itemServiceImpl.itemAdd(itemVO);
    }


    /**
     * queryMetaDataTreeInfo:初始显示树形菜单. <br/>
     *
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */
    @RequestMapping(path = "/metadatatree/{sourceId}", method = RequestMethod.GET)
    @ApiOperation(value = "根据sourceid决定初始显示哪个标准的元数据树形结构", notes = "根据sourceid决定初始显示哪个标准的元数据树形结构")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "显示树形结构数据")})
    public @ResponseBody
    List<SubsetTreeVO> queryMetaDataTreeInfo(
            @ApiParam(name = "sourceId", value = "标准来源id", required = true) @PathVariable(name = "sourceId", required = true) int sourceId) {
        return this.itemServiceImpl.queryMetaDataTreeInfo(sourceId);
    }

    /**
     * queryItemListBysubclass:根据子类编号查询元数据list信息. <br/>
     *
     * @param subclassCode 子类编号
     * @param pageNum      页码
     * @param pageSize     每一页数据数量
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */
    @RequestMapping(path = "/{subclassCode}", method = RequestMethod.GET)
    @ApiOperation(value = "根据子类编号查询元数据list信息", notes = "根据子类编号查询元数据list信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    PageInfo<ItemVO> queryItemListBysubclass(
            @ApiParam(name = "subclassCode", value = "子类编号", required = true) @PathVariable(value = "subclassCode", required = true) String subclassCode,
            @ApiParam(name = "itemCodeOrItemName", value = "元数据编号或数据项名", required = false) @RequestParam(value = "itemCodeOrItemName", required = false) String itemCodeOrItemName,
            @ApiParam(name = "pageNum", value = "页码", required = false, defaultValue = "1") @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @ApiParam(name = "pageSize", value = "每一页数据数量", required = false, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize)  throws Exception{
        /*LOGGER.debug("call ItemController.queryItemListBysubclass method");
        //对于入参值先做截取两端的可能存在的空格问题
        if (null != subclassCode) {
            subclassCode = subclassCode.trim();
        }
        List<ItemVO> result = itemServiceImpl.queryItemListBysubclass(subclassCode, itemCodeOrItemName, pageNum, pageSize);
        LOGGER.info("Result: ", result.size());
        return new PageInfo<ItemVO>(result);*/
        
        
        List<DataStandardItemVo> list = dataStandardItemServiceImpl.querySubclassItemList(-1, subclassCode, 1, Integer.MAX_VALUE);
        List<ItemVO> result = new ArrayList<ItemVO>();
        if(list!=null) {
            for(DataStandardItemVo dataVo : list) {
                ItemVO vo = new ItemVO();
                BeanUtils.copyProperties(vo,  dataVo);
                result.add(vo);
            }
        }
        return new PageInfo<ItemVO>(result);
    }
    @Resource
    private DataStandardItemService dataStandardItemServiceImpl;
}

