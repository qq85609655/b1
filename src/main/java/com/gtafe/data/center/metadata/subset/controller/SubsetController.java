package com.gtafe.data.center.metadata.subset.controller;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.information.data.service.DataStandardService;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.data.center.metadata.subset.service.ISubsetService;
import com.gtafe.data.center.metadata.subset.vo.SubsetVO;
import com.gtafe.data.center.metadata.subset.vo.Subsets;
import com.gtafe.framework.base.controller.BaseController;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/subset")
@Api(value = "SubsetController")
@CrossOrigin
public class SubsetController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubsetController.class);

	@Resource
	private ISubsetService subsetServiceImpl;
	@Resource
    private DataStandardService dataStandardServiceImpl;

	@RequestMapping(path = "/{sourceId}", method = RequestMethod.GET)
	@ApiOperation(value = "查询子集列表", notes = "查询子集列表，查询条件：code-子集代码；codeName-子集名称，分页查询")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据") })
	public @ResponseBody PageInfo<SubsetVO> list(
			@ApiParam(name = "sourceId", value = "数据来源id，例如：来自于“国家数据标准”、“学校数据标准”等", required = true) @PathVariable(value = "sourceId", required = true) String sourceId,
			@ApiParam(name = "codeOrName", value = "子集代码或者中文名称", required = false) @RequestParam(value = "codeOrName", required = false) String codeOrName,
			@ApiParam(name = "pageNum", value = "页码", required = false, defaultValue = "1") @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@ApiParam(name = "pageSize", value = "每一页数据数量", required = false, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		/*LOGGER.debug("Query subset: codeOrName = {}, pageNum = {}, pageSize = {}", codeOrName, pageNum, pageSize);
		List<SubsetVO> result = subsetServiceImpl.query(sourceId, codeOrName, pageNum, pageSize);
		return new PageInfo<SubsetVO>(result);*/
	    
	    List<DataStandardVo> list = dataStandardServiceImpl.queryDataOrgListAll(Integer.parseInt(sourceId), null, null, 1);
	    List<SubsetVO> result = new ArrayList<SubsetVO>();
	    if(list!=null) {
	        for(DataStandardVo dataVo : list) {
	            SubsetVO vo = new SubsetVO();
	            vo.setCode(dataVo.getCode());
	            vo.setCodeName(dataVo.getName());
	            result.add(vo);
	        }
	    }
	    return new PageInfo<SubsetVO>(result);
	}

	@RequestMapping(path = "/{sourceid}", method = RequestMethod.POST)
	@ApiOperation(value = "新增子集", notes = "新增子集")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否插入成功") })
	public @ResponseBody boolean insert(
			@ApiParam(name = "sourceid", value = "来源id", required = true) @PathVariable(name = "sourceid", required = true) int sourceid,
			@ApiParam(name = "subsetVO", value = "子集数据", required = true) @RequestBody SubsetVO subsetVO) {
		LOGGER.debug("Query subset: subsetVO = {}", subsetVO);
		subsetVO.setSourceid(sourceid);
		this.subsetServiceImpl.insert(subsetVO);
		return true;
	}

	@RequestMapping(path = "/batch", method = RequestMethod.POST)
	@ApiOperation(value = "批量新增子集", notes = "批量新增子集")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否插入成功") })
	public @ResponseBody boolean insertBatch(
			@ApiParam(name = "Batch", value = "子集数据列表", required = true) @RequestBody Subsets subsets) {
		LOGGER.debug("Query subset: subsets = {}", subsets);
		this.subsetServiceImpl.insertBatch(subsets);
		return true;
	}

	@RequestMapping(path = "/{sourceid}/{code}", method = RequestMethod.PUT)
	@ApiOperation(value = "修改子集", notes = "修改子集，只能修改中文名称和描述")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否修改成功") })
	public @ResponseBody boolean update(
			@ApiParam(name = "sourceid", value = "来源id", required = true) @PathVariable(name = "sourceid", required = true) int sourceid,
			@ApiParam(name = "code", value = "子集代码", required = true) @PathVariable(name = "code", required = true) String code,
			@ApiParam(name = "subsetVO", value = "子集数据， 数据中code可以不设置", required = true) @RequestBody SubsetVO subsetVO) {
		LOGGER.debug("Update subset sourceid = {}, code = {}", sourceid, code);
		subsetVO.setSourceid(sourceid);
		subsetVO.setCode(code);
		this.subsetServiceImpl.update(subsetVO);
		return true;
	}

	@RequestMapping(path = "/{sourceid}/{code}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除子集", notes = "删除子集，根据sourceid和code删除")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否删除成功") })
	public @ResponseBody boolean delete(
			@ApiParam(name = "sourceid", value = "标准来源id", required = true) @PathVariable(name = "sourceid") int sourceid,
			@ApiParam(name = "code", value = "子集代码", required = true) @PathVariable(name = "code") String code) {
		LOGGER.debug("Delete subset sourceid = {}, code = {}", sourceid, code);

		// 如果该子集下面有类就不能删除
		if (this.subsetServiceImpl.queryClass(code) != 0) {
			return false;
		}
		this.subsetServiceImpl.delete(sourceid, code);
		return true;
	}
}
