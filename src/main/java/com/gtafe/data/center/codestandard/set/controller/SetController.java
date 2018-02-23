package com.gtafe.data.center.codestandard.set.controller;

import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogVo;
import com.gtafe.data.center.system.org.service.IOrgService;
import com.gtafe.data.center.system.org.vo.OrgVo;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.utils.LogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.gtafe.data.center.codestandard.set.service.ISetService;
import com.gtafe.data.center.codestandard.set.vo.ByBatchSetVO;
import com.gtafe.data.center.codestandard.set.vo.SetVO;
import com.gtafe.data.center.common.common.constant.Constant;

/**
 * 
 * ClassName: SetController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:40:20 <br/>
 * 
 * @history
 * @author ken.zhang
 * @version
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/set")
@Api(value = "SetController")
@CrossOrigin
public class SetController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SetController.class);
	@Resource
	private ISetService setServiceImpl;
	@Resource
	private LogService logServiceImpl;

	@Resource
	private IOrgService orgServiceImpl;

	/**
	 * 
	 * saveSetInfo:新增代码子集信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{sourceid}", method = RequestMethod.POST)
	@ApiOperation(value = "新增代码子集信息", notes = "新增代码子集", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否新增成功") })
	public @ResponseBody boolean saveSetInfo(
			@ApiParam(name = "sourceid", value = "代码源id", required = true) @PathVariable(name = "sourceid") int sourceid,
			@ApiParam(name = "setVO", value = "代码集数据", required = true) @RequestBody SetVO setVO, HttpServletRequest request) {
		LOGGER.debug("saveSetInfo param : setVO={}", setVO);
		setVO.setSourceid(sourceid);
		return true;
	}

	/**
	 * 
	 * updateSetInfo:修改子集代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setVO
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "", method = RequestMethod.PUT)
	@ApiOperation(value = "修改子集代码信息", notes = "修改代码子集")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否修改成功") })
	public @ResponseBody boolean updateSetInfo(
			@ApiParam(name = "setVO", value = "代码集数据", required = true) @RequestBody SetVO setVO, HttpServletRequest request) {
		LOGGER.debug("updateSetInfo param : setVO={}", setVO);
		this.setServiceImpl.updateSetInfo(setVO);
		return true;
	}

	/**
	 * 
	 * deleteSetInfo:删除子集代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{setId}/{code}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除子集代码信息", notes = "删除代码子集,ture:没有引用删除成功 ，false：有引用删除失败")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否删除成功") })
	public @ResponseBody boolean deleteSetInfo(
			@ApiParam(name = "setId", value = "代码子集编号", required = true) @PathVariable(name = "setId") int setId,
			@ApiParam(name = "code", value = "代码子集编码", required = true) @PathVariable(value = "code") String code, HttpServletRequest request) {
		LOGGER.debug("deleteSetInfo param : sourceid={},setId={},code={}",
				setId, code);
		// 该代码子集没有引用进行删除
		if (this.setServiceImpl.isSetCiteRelation(setId) <= 0) {
			this.setServiceImpl.deleteSetInfo(setId, code);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * querySetInfo:查询子集代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param sourceid
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/source/{sourceid}", method = RequestMethod.GET)
	@ApiOperation(value = "获取子集代码信息", notes = "根据sourceid获取所有子集代码信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "根据sourceid获取所有子集代码信息") })
	public @ResponseBody List<SetVO> querySetInfo(
			@ApiParam(name = "sourceid", value = "代码源id", required = true) @PathVariable(name = "sourceid") int sourceid) {
		LOGGER.debug("querySetInfo param : sourceid={}", sourceid);
		return this.setServiceImpl.querySetInfo(sourceid);
	}

	/**
	 * 
	 * querySetById:查询子集代码信息. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param setId
	 * @param code
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{sourceid}/{setId}", method = RequestMethod.GET)
	@ApiOperation(value = "查询子集代码信息", notes = "根据sourceid,setId，code获取子集代码信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "根据setId,code获取代码集数据") })
	public @ResponseBody SetVO querySetById(
			@ApiParam(name = "sourceid", value = "代码来源编号", required = true) @PathVariable(name = "sourceid") int sourceid,
			@ApiParam(name = "setId", value = "代码集编号", required = true) @PathVariable(name = "setId") int setId,
			@ApiParam(name = "code", value = "代码集编码", required = true) @RequestParam(value = "code") String code) {
		LOGGER.debug("querySetById param : sourceid={},setId={},code={}",
				sourceid, setId, code);
		return this.setServiceImpl.querySetByCode(sourceid, setId, code);
	}

	/**
	 * 
	 * saveSetBatch:批量新增代码集. <br/>
	 * 
	 * @history
	 * @author ken.zhang
	 * @param sourceid
	 * @param batchSetVOs
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/batch/{sourceid}", method = RequestMethod.POST)
	@ApiOperation(value = "批量新增代码集", notes = "批量新增代码集")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否新增成功") })
	public @ResponseBody boolean saveSetBatch(
			@ApiParam(name = "sourceid", value = "代码来源编号", required = true) @PathVariable(name = "sourceid", required = true) int sourceid,
			@ApiParam(name = "batchSetVOs", value = "代码集数据", required = true) @RequestBody List<ByBatchSetVO> batchSetVOs) {
		return this.setServiceImpl.saveSetBatch(sourceid, batchSetVOs);
	}
}
