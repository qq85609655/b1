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
import com.gtafe.data.center.dataetl.plsql.vo.SearchResultVo;
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


    /**
     * 列表展现
     *
     * @param param
     * @return
     */
    @RequestMapping(path = "/queryList", method = RequestMethod.POST)
    public @ResponseBody
    PageInfo<PlsqlVo> queryList(@RequestBody PlsqlParamVo param) {
        LOGGER.debug("in PlsqlController.queryList method");
        List<PlsqlVo> result = plsqlServiceImpl.queryList(param.getPageNum(), param.getPageSize(), param.getOrgIds(), param.getNameKey());
        LOGGER.debug("Result: ", result.size());
        return new PageInfo<PlsqlVo>(result);
    }


    /**
     * 根据机构id 查询其下有哪些数据源
     *
     * @param orgId
     * @return
     */
    @RequestMapping(path = "/getDbSourceListByOrgId", method = RequestMethod.GET)
    public @ResponseBody
    List<DatasourceVO> getDbSourceListByOrgId(@RequestParam(value = "orgId", required = true) String orgId) {
        System.out.println("orgid=====" + orgId);
        return datasourceServiceImpl.getDbSourceListByOrgId(Integer.parseInt(orgId));
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


    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    @RequestMapping(path = "/getInfoById", method = RequestMethod.GET)
    public @ResponseBody
    PlsqlVo getInfoById(@RequestParam(value = "id", required = true) String id) {
        return plsqlServiceImpl.getInfoById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
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

    /**
     * 保存
     *
     * @param vo
     * @return
     */
    @RequestMapping(path = "/insertData", method = RequestMethod.POST)
    public @ResponseBody
    boolean insertData(@RequestBody PlsqlVo vo) {
        return this.plsqlServiceImpl.insertData(vo);
    }

    /**
     * 修改
     *
     * @param vo
     * @return
     */
    @RequestMapping(path = "/updateData", method = RequestMethod.POST)
    public @ResponseBody
    boolean updateData(@RequestBody PlsqlVo vo) {
        return this.plsqlServiceImpl.updateData(vo);
    }


    /**
     * 执行查询 出结果  默认查询出 前 100 条 计算总条数
     * 需要针对三种数据库 不能分页
     * 只是说明下数据格式 示例
     *
     * @param id
     * @return
     */
    @RequestMapping(path = "/runNow/{id}", method = RequestMethod.POST)
    public @ResponseBody
    SearchResultVo runNow(@PathVariable("id") int id) {
        return this.plsqlServiceImpl.runNow(id);
    }

}
