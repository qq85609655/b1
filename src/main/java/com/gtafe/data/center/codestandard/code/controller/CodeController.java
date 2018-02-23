package com.gtafe.data.center.codestandard.code.controller;


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

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.codestandard.code.service.ICodeService;
import com.gtafe.data.center.codestandard.code.vo.ByBatchCodeVO;
import com.gtafe.data.center.codestandard.code.vo.CodeVO;
import com.gtafe.data.center.common.common.constant.Constant;
import com.gtafe.data.center.common.login.vo.UserLoginInfo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogVo;
import com.gtafe.data.center.system.org.service.IOrgService;
import com.gtafe.data.center.system.org.vo.OrgVo;
import com.gtafe.data.center.system.user.vo.SysUserVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.utils.LogUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * ClassName: CodeController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017年7月24日 下午5:37:34 <br/>
 *
 * @author ken.zhang
 * @history
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/code")
@Api(value = "CodeController")
@CrossOrigin
public class CodeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            CodeController.class);

    @Resource
    private ICodeService codeServiceImpl;

    @Resource
    private LogService logServiceImpl;

    @Resource
    private IOrgService orgServiceImpl;

    /**
     * saveCodeInfo:新增代码信息
     *
     * @param codeVO
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    @RequestMapping(path = "", method = RequestMethod.POST)
    @ApiOperation(value = "新增代码信息", notes = "新增代码")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否新增成功")})
    public @ResponseBody
    boolean saveCodeInfo(@ApiParam(name = "codeVO", value = "代码数据", required = true) @RequestBody CodeVO codeVO, HttpServletRequest request) {
        LOGGER.debug("saveCodeInfo param : codeVO={}", codeVO);
        this.codeServiceImpl.saveCodeInfo(codeVO);

        return true;
    }

    /**
     * updateCodeInfo:修改代码信息
     *
     * @param codeVO
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    @RequestMapping(path = "", method = RequestMethod.PUT)
    @ApiOperation(value = "修改代码信息", notes = "修改代码信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否修改成功")})
    public @ResponseBody
    boolean updateCodeInfo(@ApiParam(name = "codeVO", value = "代码数据", required = true) @RequestBody CodeVO codeVO) {
        LOGGER.debug("updateCodeInfo param : codeVO={}", codeVO);
        this.codeServiceImpl.updateCodeInfo(codeVO);

        return true;
    }

    /**
     * deleteCodeInfo:删除代码信息
     *
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    @RequestMapping(path = "/{typeId}/{codeId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除代码信息", notes = "删除代码信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否删除成功")})
    public @ResponseBody
    boolean deleteCodeInfo(@ApiParam(name = "typeId", value = "类型编号", required = true) @PathVariable(name = "typeId") int typeId,
                           @ApiParam(name = "codeId", value = "编码", required = true) @PathVariable(name = "codeId") int codeId, HttpServletRequest request) {
        LOGGER.debug("deleteCodeInfo param : typeId={},codeId={}", typeId,
                codeId);
        this.codeServiceImpl.deleteCodeInfo(typeId, codeId);


        return true;
    }

    /**
     * queryCodeInfoByCode:获取代码信息
     *
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    @RequestMapping(path = "/{typeId}/{code}", method = RequestMethod.GET)
    @ApiOperation(value = "获取代码信息", notes = "根据setId，typeId获取代码信息")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "布尔值，根据typeId、code是否可以获取代码")})
    public @ResponseBody
    boolean queryCodeInfoByCode(@ApiParam(name = "typeId", value = "代码类型id", required = true) @RequestParam("typeId") int typeId,
                                @ApiParam(name = "code", value = "代码编码", required = true) @RequestParam("code") String code) {
        LOGGER.debug("queryCodeInfoByCode param :typeId={},code={}", typeId,
                code);
        System.out.println(typeId);
        System.out.println(code);
        this.codeServiceImpl.queryCodeInfoByCode(typeId, code);
        return true;
    }

    /**
     * queryCodeInfoByCode:分页获取代码信息
     *
     * @return
     * @history
     * @author ken.zhang
     * @since JDK 1.7
     */
    @RequestMapping(path = "/{sourceid}/{setId}/{typeId}", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取代码信息", notes = "查询自己列表，查询条件：code代码；name-代码名称；sourceid-代码来源编号；setId-代码子集编号；typeId-代码类型编号")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "查询列表成功，返回值是分页信息，list属性是返回的列表数据，其他是分页相关数据")})
    public @ResponseBody
    PageInfo<CodeVO> queryCodePageInfo(@ApiParam(name = "codeOrName", value = "代码或者中文名字", required = false) @RequestParam(value = "codeOrName", required = false) String codeOrName,
                                       @ApiParam(name = "sourceid", value = "代码来源编号", required = true) @PathVariable(name = "sourceid", required = true) int sourceid,
                                       @ApiParam(name = "setId", value = "代码子集编号", required = true) @PathVariable(name = "setId", required = true) int setId,
                                       @ApiParam(name = "typeId", value = "代码类型编号", required = true) @PathVariable(name = "typeId", required = true) int typeId,
                                       @ApiParam(name = "orderName", value = "排序列", required = false) @PathVariable(name = "orderName", required = false) String orderName,
                                       @ApiParam(name = "pageNum", value = "页码", required = true) @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @ApiParam(name = "pageSize", value = "每页显示数量", required = true) @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        LOGGER.debug(
                "queryCodePageInfo param : codeOrName={},sourceid={},setId={},typeId={},orderName={},pageNum={},pageSize={}",
                codeOrName, sourceid, setId, typeId, orderName, pageNum, pageSize);
        List<CodeVO> result = this.codeServiceImpl.queryCodePageInfo(codeOrName,
                sourceid, setId, typeId, orderName, pageNum, pageSize);
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<CodeVO>(result);
    }


    /**
     * saveCodeBatch:批量新增代码信息.
     *
     * @param typeId
     * @param batchCodeVOs
     * @param request
     * @return
     */
    @RequestMapping(path = "/{typeId}", method = RequestMethod.POST)
    @ApiOperation(value = "批量新增代码信息", notes = "批量新增代码信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "布尔值，是否新增成功")})
    public @ResponseBody
    boolean saveCodeBatch(@ApiParam(name = "typeId", value = "代码类型编号", required = true) @PathVariable(name = "typeId", required = true) int typeId,
                          @ApiParam(name = "batchCodeVOs", value = "代码信息数据", required = true) @RequestBody List<ByBatchCodeVO> batchCodeVOs, HttpServletRequest request) {
        System.out.println(typeId);
        System.out.println(batchCodeVOs.size());
        boolean flag = this.codeServiceImpl.saveCodeBatch(typeId, batchCodeVOs);
        return flag;

    }
}
