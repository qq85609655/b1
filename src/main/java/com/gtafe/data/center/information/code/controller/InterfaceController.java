package com.gtafe.data.center.information.code.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.information.code.service.CodeStandardService;
import com.gtafe.data.center.information.code.vo.CenterTableParam;
import com.gtafe.data.center.information.code.vo.CenterTableVo;
import com.gtafe.data.center.information.code.vo.CodeInfoParam;
import com.gtafe.data.center.system.config.service.SysConfigService;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.utils.DateUtil;
import com.gtafe.framework.base.utils.StringUtil;
import io.swagger.annotations.Api;
import oracle.jdbc.driver.DatabaseError;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/interface")
@Api(value = "InterfaceController", protocols = "http")
@CrossOrigin
public class InterfaceController extends BaseController {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(InterfaceController.class);
    @Resource
    private CodeStandardService codeStandardServiceImpl;
    @Resource
    private SysConfigService sysConfigServiceImpl;


    @RequestMapping(path = "/queryAllCenterTableList", method = RequestMethod.POST)
    public @ResponseBody
    PageInfo<CenterTableVo> queryAllCenterTableList(@RequestBody CenterTableParam param,
                                                    @RequestParam(value = "tableName", required = false) String tableName,
                                                    @RequestParam(value = "tableType", required = false) String tableType) {
        if (StringUtils.isNotBlank(tableName)) {
            param.setTableName(tableName);
        } else {
            param.setTableName(null);
        }
        if (StringUtils.isNotBlank(tableType)) {
            param.setTableType(tableType);
        } else {
            param.setTableType(null);
        }
        //根据中心库链接配置 先读取中心库所有的表信息 保存入库 然后 再查询出来
        SysConfigVo vo = this.sysConfigServiceImpl.queryCenterDbInfo();
        if (vo != null) {
            ConnectDB connectDB = StringUtil.getEntityBySysConfig(vo);
            Connection connection = connectDB.getConn();
            if (connection != null) {
                List<CenterTableVo> tableVos = this.sysConfigServiceImpl.findByConnection(vo, connection);
                if(tableVos.size()>0) {
                    boolean b = this.sysConfigServiceImpl.saveIntoVo(tableVos);
                }
            }
        }
        List<CenterTableVo> result = codeStandardServiceImpl.queryAllCenterTableList(param.getTableName(), param.getTableType(), param.getPageNum(), param.getPageSize());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<CenterTableVo>(result);
    }

    /**
     * 生成代码 并下载下来
     *
     * @throws Exception
     * @throws ParseErrorException
     * @throws ResourceNotFoundException
     */
    @RequestMapping("/code")
    public void code(@RequestParam(value = "tableName", required = false) String tableName, HttpServletResponse response)
            throws ResourceNotFoundException, ParseErrorException, Exception {
        List<String> tableNamesList = StringUtil.splitListString(tableName);
        byte[] data = codeStandardServiceImpl.generatorCode(tableNamesList);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"InterfaceCode-" + DateUtil.format(new Date(), "yyyyMMddHHMMss") + ".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

}
