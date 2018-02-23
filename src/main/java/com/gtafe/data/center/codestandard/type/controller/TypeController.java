package com.gtafe.data.center.codestandard.type.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.gtafe.data.center.codestandard.type.service.ITypeService;
import com.gtafe.data.center.codestandard.type.vo.ByBatchTypeVO;
import com.gtafe.data.center.codestandard.type.vo.TypeVO;
import com.gtafe.framework.base.exception.OrdinaryException;

/**
 * 
 * ClassName: TypeController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:54:15 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/type")
@CrossOrigin
@Api(value = "TypeController")
public class TypeController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TypeController.class);
	@Resource
	private ITypeService typeServiceImpl;

	/**
	 * 
	 * saveTypeInfo:新增代码类型信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param typeVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "", method = RequestMethod.POST)
	@ApiOperation(value = "新增代码类型信息", notes = "新增代码类型")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否新增成功") })
	public @ResponseBody boolean saveTypeInfo(
			@ApiParam(name = "typeVO", value = "代码类型数据", required = true) @RequestBody TypeVO typeVO) {
		LOGGER.debug("saveTypeInfo param : typeVO={}", typeVO);
		this.typeServiceImpl.saveTypeInfo(typeVO);
		return true;
	}

	/**
	 * 
	 * updateTypeInfo:修改代码类型信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param typeVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "", method = RequestMethod.PUT)
	@ApiOperation(value = "修改代码类型信息", notes = "修改代码类型")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否修改成功") })
	public @ResponseBody boolean updateTypeInfo(
			@ApiParam(name = "typeVO", value = "代码类型数据", required = true) @RequestBody TypeVO typeVO) {
		LOGGER.debug("updateTypeInfo param : typeVO={}", typeVO);
		this.typeServiceImpl.updateTypeInfo(typeVO);
		return true;
	}

	/**
	 * 
	 * deleteTypeInfo:删除代码类型信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param typeVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{setId}/{typeId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除代码类型信息", notes = "删除代码类型")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，根据子集编号，类型编号是否删除成功") })
	public @ResponseBody boolean deleteTypeInfo(
			@ApiParam(name = "setId", value = "子集编号", required = true) @PathVariable(name = "setId") int setId,
			@ApiParam(name = "typeId", value = "类型编号", required = true) @PathVariable(name = "typeId") int typeId) {
		LOGGER.debug("deleteTypeInfo param : setId={},typeId={}", setId, typeId);
		// 如果该类型下没有数据可以进行删除操作
		if (this.typeServiceImpl.isTypeCiteRelation(typeId) <= 0) {
			this.typeServiceImpl.deleteTypeInfo(setId, typeId);
			return true;
		}
		throw new OrdinaryException("该节点下有代码数据，不能删除！");
	}

	/**
	 * 
	 * queryTypeInfo:获取所有代码类型信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setId
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/type/{sourceId}/{setId}", method = RequestMethod.GET)
	@ApiOperation(value = "获取所有代码类型信息", notes = "根据子集代码获取所有该子集代码类型")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "根据子集代码获取所有该子集代码类型") })
	public @ResponseBody List<TypeVO> queryTypeInfo(
			@ApiParam(name = "sourceId", value = "代码来源id", required = true) @PathVariable(name = "sourceId", required = true) int sourceId,
			@ApiParam(name = "setId", value = "代码集id", required = true) @PathVariable(name = "setId", required = true) int setId) {
		LOGGER.debug("queryTypeInfo param : setId={}", setId);
		return this.typeServiceImpl.queryTypeInfo(sourceId, setId);
	}

	/**
	 * 
	 * queryTypeInfoByCode:获取代码类型信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setId
	 * @param typeId
	 * @param code
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{setId}/{typeId}/{code}", method = RequestMethod.GET)
	@ApiOperation(value = "获取代码类型信息", notes = "根据setId，typeId获取代码类型信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，根据setId，typeId是否可以获取代码类型") })
	public @ResponseBody TypeVO queryTypeInfoByCode(
			@ApiParam(name = "setId", value = "代码集id", required = true) @PathVariable(name = "setId", required = true) int setId,
			@ApiParam(name = "typeId", value = "代码类型id", required = true) @PathVariable(name = "typeId", required = true) int typeId,
			@ApiParam(name = "code", value = "代码类型编码", required = true) @PathVariable(name = "code", required = true) String code) {
		LOGGER.debug("querySetById param : setId={},typeId={}", setId, typeId);
		return this.typeServiceImpl.queryTypeInfoByCode(setId, typeId, code);
	}

	/**
	 * 
	 * saveTypeBatch:批量新增代码类型. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param sourceid
	 * @param batchSetVOs
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{setId}", method = RequestMethod.POST)
	@ApiOperation(value = "批量新增代码类型", notes = "批量新增代码类型")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否新增成功") })
	public @ResponseBody boolean saveTypeBatch(
			@ApiParam(name = "setId", value = "代码类型编号", required = true) @PathVariable(name = "setId", required = true) int setId,
			@ApiParam(name = "batchTypeVOs", value = "代码类型数据", required = true) @RequestBody List<ByBatchTypeVO> batchTypeVOs) {
		
	    LOGGER.info(batchTypeVOs.toString());
	    return this.typeServiceImpl.saveTypeBatch(setId, batchTypeVOs);
	}
}
