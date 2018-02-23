package com.gtafe.data.center.dataetl.datasource.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
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
import com.gtafe.data.center.dataetl.datasource.service.IDatasourceService;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceParam;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.rule.TableFieldVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(path = "/datasource")
@Api(value = "DatasourceController")
@CrossOrigin
public class DatasourceController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatasourceController.class);

    @Resource
    private IDatasourceService datasourceServiceImpl;

    /**
     * queryDatasourceList:分页查询数据源信息(跟初始化查询数据源用同一个方法). <br/>
     *
     * @param dbType       数据库类型
     * @param nameOrDBName 数据源名称或数据库名称
     * @param pageNum      页码
     * @param pageSize     每一页数据数量
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */
    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    @ApiOperation(value = "查询数据源或初始化查询数据源", notes = "查询数据源或初始化查询数据源")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    PageInfo<DatasourceVO> queryDatasourceList(@RequestBody DatasourceParam param) {
        LOGGER.debug("in DatasourceController.queryDatasourceList method");
        if (null != param.getNameOrDBName()) {
            param.setNameOrDBName(param.getNameOrDBName().trim());
        }
        List<DatasourceVO> result = datasourceServiceImpl.queryDatasourceList(
            param.getDbType(), param.getNameOrDBName(), param.getPageNum(), param.getPageSize(), param.getOrgIds());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<DatasourceVO>(result);
    }
    
    @RequestMapping(path = "/queryListAll", method = RequestMethod.GET)
    @ApiOperation(value = "查询数据源或初始化查询数据源", notes = "查询数据源或初始化查询数据源")
    public @ResponseBody
    List<DatasourceVO> queryDatasourceListAll( @RequestParam(value = "orgIds", required = false) String orgIds) {
        return datasourceServiceImpl.queryDatasourceListAll(orgIds);
    }
    

    /**
     * datasourceDelete:删除数据源信息. <br/>
     *
     * @param id
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除数据源", notes = "删除数据源管理，根据id删除数据源")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否删除成功")})
    public @ResponseBody
    boolean datasourceDelete(
            @ApiParam(name = "id", value = "数据源对应的唯一序号", required = true) @PathVariable(name = "id") int id) {
        LOGGER.debug("Delete datasource id = {}", id);
        return this.datasourceServiceImpl.datasourceDelete(id);
    }

    /**
     * datasourceAdd:新增数据源信息. <br/>
     *
     * @return
     * @author wuhu.zhu
     * @since JDK 1.7
     */

    @RequestMapping(path = "/insertData", method = RequestMethod.POST)
    @ApiOperation(value = "新增数据源", notes = "新增数据源管理")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否新增成功")})
    public @ResponseBody
    boolean datasourceAdd(
            @ApiParam(name = "datasourceVO", value = "数据源信息", required = true) @RequestBody DatasourceVO datasourceVO) {
        LOGGER.debug("Query datasource: datasourceVO = {}", datasourceVO.toString());
        if(!this.testConnect(datasourceVO)){
            throw new OrdinaryException("数据库连接失败！");
        }
        return this.datasourceServiceImpl.datasourceAdd(datasourceVO);
    }

    /**
     * datasourceUpdate:修改数据源信息. <br/>
     *
     * @param id
     * @param datasourceVO
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */
    @RequestMapping(path = "/updateData", method = RequestMethod.POST)
    @ApiOperation(value = "修改数据源信息", notes = "修改数据源信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否修改成功")})
    public @ResponseBody
    boolean datasourceUpdate(
            @ApiParam(name = "datasourceVO", value = "数据源信息", required = true) @RequestBody DatasourceVO datasourceVO) {
        if(!this.testConnect(datasourceVO)){
            throw new OrdinaryException("数据库连接失败！");
        }
        return this.datasourceServiceImpl.datasourceUpdate(datasourceVO);

    }

    /**
     * queryDatasourceStatus:连接数据源测试. <br/>
     *
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */

    @RequestMapping(path = "/connect", method = RequestMethod.POST)
    @ApiOperation(value = "连接数据源测试", notes = "连接数据源测试")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否新增成功")})
    public @ResponseBody
    boolean testConnect(
            @ApiParam(name = "datasourceVO", value = "数据源信息", required = true) @RequestBody DatasourceVO datasourceVO) {
        LOGGER.debug("Query datasourceStatus: datasourceVO = {}", datasourceVO);
        return this.datasourceServiceImpl.queryDatasourceStatus(datasourceVO);
    }

    /**
     * queryTablesByDatasource:根据数据源查询所有表信息. <br/>
     *
     * @param id
     * @return
     * @history
     * @author wuhu.zhu
     * @since JDK 1.7
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据数据源查询所有表信息", notes = "根据数据源查询所有表信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询列表成功，返回值是所有信息，list属性是返回的列表数据")})
    public @ResponseBody
    List<String> queryTablesByDatasource(
            @ApiParam(name = "id", value = "数据源对应的唯一序号", required = true) @PathVariable(name = "id") int id) {
        LOGGER.debug("Query datasource id = {}", id);
        DatasourceVO datasourceVO = this.datasourceServiceImpl.queryDatasourceInfoById(id);
        if (datasourceVO != null) {
            return this.datasourceServiceImpl.queryTablesByDatasource(datasourceVO);

        } else {
            List<String> tbs = new ArrayList<String>();
            return tbs;
        }
    }  
    
    @RequestMapping(path = "/queryTableFields", method = RequestMethod.GET)
    public @ResponseBody
    List<TableFieldVo> queryTableFields(
            @ApiParam(name = "connectionId", value = "数据源id", required = true) @RequestParam(name = "connectionId") int connectionId,
            @ApiParam(name = "table", value = "表名称", required = true) @RequestParam(name = "tableName") String tableName) throws Exception {
        DatasourceVO datasourceVO = this.datasourceServiceImpl.queryDatasourceInfoById(connectionId);
        if (datasourceVO != null) {
            return this.datasourceServiceImpl.queryTableFields(datasourceVO, tableName);
        } else {
            return new ArrayList<TableFieldVo>();
        }
    }


    @RequestMapping(path = "/getDataInfoById", method = RequestMethod.GET)
    public @ResponseBody
    DatasourceVO getDataInfoById(@Param("id") int id) {
        LOGGER.info("id=====" + id);
        DatasourceVO vo = this.datasourceServiceImpl.queryDatasourceInfoById(id);
        return vo;
    }
}
