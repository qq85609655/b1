package com.gtafe.data.center.codestandard.source.controller;

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

import com.gtafe.data.center.codestandard.source.service.ISourceService;
import com.gtafe.data.center.codestandard.source.vo.SourceVO;

/**
 * 
 * ClassName: SourceController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:51:42 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/source")
@Api("source")
@CrossOrigin
public class SourceController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SourceController.class);
	@Resource
	private ISourceService sourceServiceImpl;

	/**
	 * 
	 * saveSourceInfo:新增代码来源信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param sourceVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "", method = RequestMethod.POST)
	@ApiOperation(value = "新增代码来源信息", notes = "新增代码来源")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否新增成功") })
	public @ResponseBody boolean saveSourceInfo(
			@ApiParam(name = "sourceVO", value = "代码来源数据", required = true) @RequestBody SourceVO sourceVO) {
		LOGGER.debug("saveSourceInfo param : sourceVO={}", sourceVO);
		this.sourceServiceImpl.saveSourceInfo(sourceVO);
		return true;
	}

	/**
	 * 
	 * updateSourceInfo:修改代码来源信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param sourceVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "", method = RequestMethod.PUT)
	@ApiOperation(value = "修改代码来源信息", notes = "修改代码来源")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否修改成功") })
	public @ResponseBody boolean updateSourceInfo(
			@ApiParam(name = "sourceVO", value = "代码来源数据", required = true) @RequestBody SourceVO sourceVO) {
		LOGGER.debug("updateSourceInfo param : sourceVO={}", sourceVO);
		this.sourceServiceImpl.updateSourceInfo(sourceVO);
		return true;
	}

	/**
	 * 
	 * deleteSourceInfo:删除代码来源信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param sourceVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{sourceid}/{code}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除代码来源信息", notes = "删除代码来源")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否删除成功") })
	public @ResponseBody boolean deleteSourceInfo(
			@ApiParam(name = "sourceid", value = "来源编号", required = true) @PathVariable(name = "sourceid") int sourceid,
			@ApiParam(name = "code", value = "来源编码", required = true) @PathVariable(name = "code") String code) {
		LOGGER.debug("deleteSourceInfo param : sourceid={},code={}", sourceid,
				code);
		// 如果该代码来源存在数据则不能删除
		if (this.sourceServiceImpl.isSourceCiteRelation(sourceid) > 0) {
			return false;
		}
		this.sourceServiceImpl.deleteSourceInfo(sourceid, code);
		return true;
	}

	/**
	 * 
	 * querySourceInfoAll:获取所有代码来源信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	@ApiOperation(value = "获取所有代码来源信息", notes = "获取所有代码来源信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值,是否可以获取所有代码来源信息") })
	public @ResponseBody boolean querySourceInfoAll() {
		if (this.sourceServiceImpl.querySourceInfoAll() == null) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * querySourceInfoByCode:获取代码来源信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param sourceid
	 * @param code
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{sourceid}/{code}", method = RequestMethod.GET)
	@ApiOperation(value = "获取代码来源信息", notes = "根据shourceid、code获取代码来源信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "根据typeId、code获取代码") })
	public @ResponseBody SourceVO querySourceInfoByCode(
			@ApiParam(name = "sourceid", value = "代码来源id", required = true) @PathVariable(name = "sourceid") int sourceid,
			@ApiParam(name = "code", value = "来源编码", required = true) @PathVariable(name = "code") String code) {
		LOGGER.debug("querySourceInfoByCode param :sourceid={},code={}",
				sourceid, code);
		return this.sourceServiceImpl.querySourceInfoByCode(sourceid, code);
	}

	/**
	 * 
	 * queryTreeInfo:初始显示树形结构. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/standardTree/{sourceid}", method = RequestMethod.GET)
	@ApiOperation(value = "初始显示树形结构", notes = "根据sourceid初始显示树形结构")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "显示树形结构数据") })
	public @ResponseBody List<SourceVO> queryTreeInfo(
			@ApiParam(name = "sourceid", value = "代码来源id", required = true) @PathVariable(name = "sourceid") int sourceid) {
		LOGGER.debug("queryTreeInfo param :sourceid={}", sourceid);
		return this.sourceServiceImpl.queryTreeInfo(sourceid);
	}
}
