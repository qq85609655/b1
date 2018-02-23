package com.gtafe.data.center.metadata.clazz.controller;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.information.data.service.DataStandardService;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.data.center.metadata.clazz.service.IClazzService;
import com.gtafe.data.center.metadata.clazz.vo.ClazzBySubsetVO;
import com.gtafe.data.center.metadata.clazz.vo.ClazzVO;
import com.gtafe.data.center.metadata.subset.vo.SubsetVO;
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
@RequestMapping(path = "/class")
@Api(value="ClazzController", protocols="http")
@CrossOrigin
public class ClazzController extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClazzController.class);

	@Resource
	private IClazzService clazzServiceImpl;
	@Resource
    private DataStandardService dataStandardServiceImpl;
	/**
	 * 
	 * queryClassList:分页查询类信息(跟初始化类用同一个方法). <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param subsetCode
	 *            子集编号
	 * @param classCodeOrclassName
	 *            类编号或类中文名
	 * @param pageNum
	 *            页码
	 * @param pageSize
	 *            每一页数据数量
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{sourceId}", method = RequestMethod.GET)
	@ApiOperation(value = "查询类或初始化查询或点查看类信息", notes = "查询类查询或初始化查询或点查看类信息")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据") })
	public @ResponseBody PageInfo<ClazzVO> queryClassList(
			@ApiParam(name = "sourceId", value = "标准来源ID", required = true) @PathVariable(name = "sourceId", required = true) int sourceId,
			@ApiParam(name = "subsetCode", value = "子集编号", required = false) @RequestParam(value = "subsetCode", required = false) String subsetCode,
			@ApiParam(name = "classCodeOrclassName", value = "类编号或类中文名", required = false) @RequestParam(value = "classCodeOrclassName", required = false) String classCodeOrclassName,
			@ApiParam(name = "pageNum", value = "页码", required = false, defaultValue = "1") @RequestParam(value = "pageNum", defaultValue="1") int pageNum,
			@ApiParam(name = "pageSize", value = "每一页数据数量", required = false, defaultValue = "10") @RequestParam(value = "pageSize", defaultValue="10") int pageSize) {
		/*LOGGER.debug("in ClazzController.list method");
		//对于入参值先做截取两端的可能存在的空格问题
		if(null!=subsetCode)
		{
			subsetCode=subsetCode.trim();
		}
		if(null!=classCodeOrclassName)
		{
			classCodeOrclassName=classCodeOrclassName.trim();
		}
		List<ClazzVO> result = clazzServiceImpl.queryClassList(sourceId, subsetCode,classCodeOrclassName, pageNum, pageSize);
		LOGGER.debug("Result: ", result.size());
		return new PageInfo<ClazzVO>(result);*/
		
		
		List<DataStandardVo> list = dataStandardServiceImpl.queryDataOrgListAll(sourceId, subsetCode, null, 2);
        List<ClazzVO> result = new ArrayList<ClazzVO>();
        if(list!=null) {
            for(DataStandardVo dataVo : list) {
                ClazzVO vo = new ClazzVO();
                vo.setClassCode(dataVo.getCode());
                vo.setClassName(dataVo.getName());
                vo.setSubsetCode(dataVo.getParentCode());
                result.add(vo);
            }
        }
        return new PageInfo<ClazzVO>(result);
	}
	
	/**
	 * 
	 * classDelete:删除类信息. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param subsetCode
	 * @param classCode
	 * @return
	 * @since JDK 1.7
	 */
	@RequestMapping(path = "/{subsetCode}/{classCode}", method = RequestMethod.DELETE)
	@ApiOperation(value = "删除类", notes = "删除类管理，根据subsetCode和classCode删除类")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否删除成功") })
	public @ResponseBody boolean classDelete(
			@ApiParam(name = "subsetCode", value = "子集编号", required = true) @PathVariable(name = "subsetCode") String subsetCode, 
			@ApiParam(name = "classCode", value = "类编号", required = true) @PathVariable(name = "classCode") String classCode) {
		LOGGER.debug("Delete class subsetCode = {}, classCode = {}", subsetCode, classCode);
		//如果该类下面有子类就不能删除 
		if(this.clazzServiceImpl.querySubClass(classCode)!=0)
		{
			return false;
		}
		return this.clazzServiceImpl.classDelete(subsetCode, classCode);
	}
	
	/**
	 * 
	 * updateClass:修改类信息. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param subsetCode
	 * @param classCode
	 * @param clazzVO
	 * @return
	 * @since JDK 1.7
	 */
	
	@RequestMapping(path = "/{subsetCode}/{classCode}", method = RequestMethod.PUT)
	@ApiOperation(value = "修改类", notes = "修改类管理，只能修改中文名称和描述")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否修改成功") })
	public @ResponseBody boolean updateClass(
			@ApiParam(name = "subsetCode", value = "子集编号", required = true) @PathVariable(name = "subsetCode", required = true) String subsetCode,
			@ApiParam(name = "classCode", value = "类编号", required = true) @PathVariable(name = "classCode", required = true) String classCode, @RequestBody ClazzVO clazzVO) {
		LOGGER.debug("Update subset sourceid = {}, code = {}", subsetCode, classCode);
		clazzVO.setSubsetCode(subsetCode);
		clazzVO.setClassCode(classCode);
		return this.clazzServiceImpl.updateClass(clazzVO);
	}
	
	/**
	 * 
	 * addClass:新增类信息. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param clazzVO
	 * @return
	 * @since JDK 1.7
	 */
	
	@RequestMapping(path = "", method = RequestMethod.POST)
	@ApiOperation(value = "新增类", notes = "新增类管理")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否新增成功") })
	public @ResponseBody boolean addClass(
			@ApiParam(name = "clazzVO", value = "类数据", required = true) @RequestBody ClazzVO clazzVO) {
		LOGGER.debug("Query class: clazzVO = {}", clazzVO);
		return this.clazzServiceImpl.addClass(clazzVO);
	}
	
	
	
	/**
	 * 
	 * addClassBatchBysubset:按子集编号批量新增类信息. <br/>
	 * 
	 * @history
	 * @author wuhu.zhu
	 * @param subsetCode listClazzBySubsetVO
	 * @return
	 * @since JDK 1.7
	 */
	
	@RequestMapping(path = "/{subsetCode}", method = RequestMethod.POST)
	@ApiOperation(value = "按子集编号批量新增类", notes = "按子集编号批量新增类")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "布尔值，是否插入成功") })
	public @ResponseBody boolean addClassBatchBysubset(
			@ApiParam(name = "subsetCode", value = "子集编号", required = true) @PathVariable(name = "subsetCode", required = true) String subsetCode,
			@ApiParam(name = "Batch", value = "类数据列表", required = true) @RequestBody List<ClazzBySubsetVO> listClazzBySubsetVO) {
		LOGGER.debug("Query class: subsetCode = {},listClazzBySubsetVO = {}", subsetCode,listClazzBySubsetVO);
		return this.clazzServiceImpl.addClassBatchBysubset(subsetCode,listClazzBySubsetVO);
	}
	
}
