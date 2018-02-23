package com.gtafe.data.center.metadata.subclass.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.information.data.service.DataStandardService;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.data.center.metadata.clazz.vo.ClazzVO;
import com.gtafe.data.center.metadata.subclass.service.ISubsclassService;
import com.gtafe.data.center.metadata.subclass.vo.SubclassByclassVO;
import com.gtafe.data.center.metadata.subclass.vo.SubclassVO;
import com.gtafe.framework.base.controller.BaseController;

/***
 * 
 * ClassName: 数据子类控制层 <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月18日 下午5:16:57 <br/>
 * 
 * @author ken.zhang
 * @version
 */
@Controller
@RequestMapping("/subclass")
@Api(value = "SubclassController")
@CrossOrigin
public class SubclassController extends BaseController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SubclassController.class);

	@Resource
	private ISubsclassService subclassServiceImpl;
	@Resource
    private DataStandardService dataStandardServiceImpl;
	/**
	 * 
	 * list:分页查询子类信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subsetCode
	 *            子集代码
	 * @param classCode
	 *            类代码
	 * @param codeOrName
	 *            子类代码或中文名字
	 * @param pageNum
	 *            页码
	 * @param pageSize
	 *            每一页数据数量
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{sourceId}", method = RequestMethod.GET)
	@ApiOperation(value = "子类分页查询", notes = "查询自己列表，查询条件：code-子集代码；codeName-子集名称", httpMethod = "GET")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据") })
	public @ResponseBody PageInfo<SubclassVO> querySubclassPageInfo(
			@ApiParam(name = "sourceId", value = "子集编号", required = true) @PathVariable(value = "sourceId", required = true) int sourceId,
			@ApiParam(name = "subsetCode", value = "子集代码", required = false) @RequestParam(value = "subsetCode", required = false) String subsetCode,
			@ApiParam(name = "classCode", value = "类代码", required = false) @RequestParam(value = "classCode", required = false) String classCode,
			@ApiParam(name = "codeOrName", value = "子类代码或中文名字", required = false) @RequestParam(value = "codeOrName", required = false) String codeOrName,
			@ApiParam(name = "orderName", value = "排序列名", required = false) @RequestParam(value = "orderName", required = false) String orderName,
			@ApiParam(name = "pageNum", value = "页码", required = false, defaultValue = "1") @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@ApiParam(name = "pageSize", value = "每一页数据数量", required = false, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		/*LOGGER.debug(
				"Query subclass: subsetCode={},classCode={},codeOrName = {},codeName={}, pageNum = {}, pageSize = {}",
				subsetCode, classCode, codeOrName, orderName, pageNum, pageSize);

		List<SubclassVO> result = subclassServiceImpl.querySubclassPageInfo(
				sourceId, subsetCode, classCode, codeOrName, orderName,
				pageNum, pageSize);
		LOGGER.debug("Result: ", result.size());
		return new PageInfo<SubclassVO>(result);*/
	    
	    List<DataStandardVo> list = dataStandardServiceImpl.queryDataOrgListAll(sourceId, subsetCode, classCode, 3);
        List<SubclassVO> result = new ArrayList<SubclassVO>();
        if(list!=null) {
            for(DataStandardVo dataVo : list) {
                SubclassVO vo = new SubclassVO();
                vo.setSubclassCode(dataVo.getCode());
                vo.setSubclassComment(dataVo.getName());
                vo.setClassCode(dataVo.getParentCode());
                vo.setSubclassTableName(dataVo.getTableName());
                result.add(vo);
            }
        }
        return new PageInfo<SubclassVO>(result);
	}

	/**
	 * querybySubclassCode:查看该数据子类是否存在. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassCode
	 * @param classCode
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{classCode}/{subclassCode}", method = RequestMethod.GET)
	@ApiOperation(value = "查看该数据子类是否存在", notes = "根据子类码、类码查询; true：存在，false不存在")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否存在子类") })
	public @ResponseBody boolean querybySubclassCode(
			@ApiParam(name = "subclassCode", value = "子类代码", required = true) @PathVariable(value = "subclassCode") String subclassCode,
			@ApiParam(name = "classCode", value = "数据类代码", required = true) @PathVariable(value = "classCode") String classCode) {
		LOGGER.debug(
				"querybySubclassCode param : subclassCode={},classCode={}",
				subclassCode, classCode);
		if (this.subclassServiceImpl.querybySubclassCode(subclassCode,
				classCode) != null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * saveSubclassInfo:新增子类信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "", method = RequestMethod.POST)
	@ApiOperation(value = "新增子类", notes = "新增子类")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否新增成功") })
	public @ResponseBody boolean saveSubclassInfo(
			@ApiParam(name = "subclassVO", value = "子类数据", required = true) @RequestBody SubclassVO subclassVO) {
		LOGGER.debug("Query subclass : subclassVO={}", subclassVO);
		this.subclassServiceImpl.saveSubclassInfo(subclassVO);
		return true;
	}

	/**
	 * 
	 * updateSubclassInfo:修改子类信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "", method = RequestMethod.PUT)
	@ApiOperation(value = "修改子类", notes = "根据子类subclassCode、classCode修改")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否修改成功") })
	public @ResponseBody boolean updateSubclassInfo(
			@ApiParam(name = "subclassVO", value = "子类数据", required = true) @RequestBody SubclassVO subclassVO) {
		LOGGER.debug("Update subclass : subclassVO={}", subclassVO);
		this.subclassServiceImpl.updateSubclassInfo(subclassVO);
		return true;
	}

	/**
	 * 
	 * deleteSubclassInfo:删除子类信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param subclassVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{subclassCode}/{classCode}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除子类", notes = "根据子类subclassCode、classCode删除")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否删除成功") })
	public @ResponseBody boolean deleteSubclassInfo(

			@ApiParam(name = "classCode", value = "类编码", required = true) @PathVariable(name = "classCode") String classCode,
			@ApiParam(name = "subclassCode", value = "子类编码", required = true) @PathVariable(name = "subclassCode") String subclassCode) {
		LOGGER.debug("deleteSubclassInfo param : subclassCode={},classCode={}",
				subclassCode, classCode);
		// 如果该子类下存在数据，则不能执行删除,否则执行删除
		if (this.subclassServiceImpl.isSubclassCiteRelation(subclassCode) > 0) {
			return false;
		}
		this.subclassServiceImpl.deleteSubclassInfo(subclassCode, classCode);
		return true;
	}

	/**
	 * 
	 * addSubclassBatch:按类编号批量新增子类信息. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param listSubclassVO
	 * @return
	 * @since JDK 1.7
	 */

	@RequestMapping(path = "/{classCode}", method = RequestMethod.POST)
	@ApiOperation(value = "按类编号批量新增子类", notes = "按类编号批量新增子类")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否插入成功") })
	public @ResponseBody boolean addSubclassBatch(
			@ApiParam(name = "classCode", value = "类编号", required = true) @PathVariable(name = "classCode", required = true) String classCode,
			@ApiParam(name = "Batch", value = "子类数据列表", required = true) @RequestBody List<SubclassByclassVO> listSubclassByClassVO) {
		LOGGER.debug("Query subclass: classCode = {},listSubclassVO = {}",
				classCode, listSubclassByClassVO);
		return this.subclassServiceImpl.addSubclassBatch(classCode,
				listSubclassByClassVO);
	}
}
