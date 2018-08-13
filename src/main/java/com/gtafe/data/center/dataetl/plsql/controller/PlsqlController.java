package com.gtafe.data.center.dataetl.plsql.controller;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.service.IDatasourceService;
import com.gtafe.data.center.dataetl.datasource.service.impl.DatasourceServiceImpl;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.plsql.service.PlsqlService;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlParamVo;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/plsql")
@CrossOrigin
public class PlsqlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlsqlController.class);

    @Resource
    private PlsqlService plsqlServiceImpl;
    @Resource
    private IDatasourceService datasourceServiceImpl;


    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    public @ResponseBody
    PageInfo<PlsqlVo> queryList(@RequestBody PlsqlParamVo param) {
        LOGGER.debug("in PlsqlController.queryList method");
        List<PlsqlVo> result = plsqlServiceImpl.queryList(param.getPageNum(), param.getPageSize(), param.getOrgIds(), param.getNameKey());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<PlsqlVo>(result);
    }


    @RequestMapping(path = "/getDbSourceListByOrgId", method = RequestMethod.GET)
    public @ResponseBody
    List<DatasourceVO> getDbSourceListByOrgId(@RequestBody int orgId) {
        return datasourceServiceImpl.getDbSourceListByOrgId(orgId);
    }

    /**
     * 检测脚本是否能执行
     *
     * @param vo
     * @return
     */
    @RequestMapping(path = "/checkOut", method = RequestMethod.POST)
    public @ResponseBody
    boolean checkOut(@RequestBody PlsqlVo vo) {
        return this.plsqlServiceImpl.checkOut(vo);
    }


    @RequestMapping(path = "/getInfoById", method = RequestMethod.GET)
    public PlsqlVo getInfoById(@RequestParam(value = "id", required = true) int id) {
        return plsqlServiceImpl.getInfoById(id);
    }

    @RequestMapping(path = "/deleteBatch/{ids}", method = RequestMethod.DELETE)
    public @ResponseBody
    boolean deleteBatch(
            @PathVariable(name = "ids") String ids) {
        LOGGER.debug("delete param:ids={} ", ids);
        String[] ids_ = ids.split(",");
        List<Integer> idList = new ArrayList<Integer>();
        for (String mdddp : ids_) {
            idList.add(Integer.parseInt(mdddp));
        }
        plsqlServiceImpl.deleteBatchs(idList);
        return true;
    }

    @RequestMapping(path = "/insertData", method = RequestMethod.POST)
    public @ResponseBody
    boolean insertData(@RequestBody PlsqlVo vo) {
        return this.plsqlServiceImpl.insertData(vo);
    }

    @RequestMapping(path = "/updateData", method = RequestMethod.POST)
    public @ResponseBody
    boolean updateData(@RequestBody PlsqlVo vo) {
        return this.plsqlServiceImpl.updateData(vo);
    }

}
