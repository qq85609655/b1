package com.gtafe.data.center.information.code.controller;

import java.util.List;

import javax.annotation.Resource;

import com.gtafe.framework.base.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.gtafe.data.center.information.code.service.CodeStandardService;
import com.gtafe.data.center.information.code.vo.CodeInfoParam;
import com.gtafe.data.center.information.code.vo.CodeInfoVo;
import com.gtafe.data.center.information.code.vo.CodeNodeVo;
import com.gtafe.framework.base.annotation.AuthAnnotation;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;

import io.swagger.annotations.Api;

/**
 * @author zg
 */
@RestController
@RequestMapping(path = "/codestandard")
@Api(value = "CodeStandardController", protocols = "http")
@CrossOrigin
public class CodeStandardController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeStandardController.class);
    @Resource
    private CodeStandardService codeStandardServiceImpl;

    /**
     * 查询代码标准列表
     *
     * @author 汪逢建
     * @date 2017年11月8日
     */
    @AuthAnnotation(value = {"032001", "033001"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/queryCodeList", method = RequestMethod.POST)
    public PageInfo<CodeInfoVo> queryCodeList(
            @RequestParam(value = "sourceId", required = false) int sourceId,
            @RequestBody CodeInfoParam param) {
        if (param.getNodeType() < 0) {
            throw new OrdinaryException("参数错误");
        }
        param.setSourceId(sourceId);
        LOGGER.info(param.toString());
        List<CodeInfoVo> result = codeStandardServiceImpl.queryCodeList(param.getKeyWord(), param.getNodeId(), sourceId, param.getNodeType(), param.getPageNum(), param.getPageSize());
        return new PageInfo<CodeInfoVo>(result);
    }


    /**
     * 查询代码标准列表
     *
     * @author 汪逢建
     * @date 2017年11月8日
     */
    @AuthAnnotation(value = {"032001", "033001"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/queryCodeList2", method = RequestMethod.POST)
    public PageInfo<CodeInfoVo> queryCodeList2(
            @RequestParam(value = "sourceId", required = false) int sourceId,
            @RequestParam(value = "parentId", required = false) String parentId,
            @RequestBody CodeInfoParam param) {
        if (param.getNodeType() < 0) {
            throw new OrdinaryException("参数错误");
        }
        param.setSourceId(sourceId);
        LOGGER.info("node_id=====" + param.getNodeId());
        List<CodeInfoVo> result = codeStandardServiceImpl.queryCodeList2(param.getKeyWord(), param.getNodeId() + "", sourceId, param.getPageNum(), param.getPageSize());
        return new PageInfo<CodeInfoVo>(result);
    }





    @AuthAnnotation(value = {"032001", "033001"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/queryCodeList4Reseacher", method = RequestMethod.POST)
    public PageInfo<CodeInfoVo> queryCodeList4Reseacher(
            @RequestParam(value = "sourceId", required = false) int sourceId,
            @RequestParam(value = "parentId", required = false) String parentId,
            @RequestBody CodeInfoParam param) {
        if (param.getNodeType() < 0) {
            throw new OrdinaryException("参数错误");
        }
        param.setSourceId(sourceId);
        LOGGER.info("node_id=====" + param.getNodeId());
        List<CodeInfoVo> result = codeStandardServiceImpl.queryCodeList2(param.getKeyWord(), param.getNodeId() + "", sourceId, param.getPageNum(), param.getPageSize());
        return new PageInfo<CodeInfoVo>(result);
    }


    /**
     * 查询代码标准树
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    @AuthAnnotation(value = {"032001", "033001"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/queryCodeNodeTree", method = RequestMethod.GET)
    public CodeNodeVo queryCodeNodeTree(@RequestParam(value = "sourceId", required = false) int sourceId) {
        return codeStandardServiceImpl.queryCodeNodeTree(sourceId);
    }


    @RequestMapping(path = "/queryCodeNodeTree2", method = RequestMethod.GET)
    public CodeNodeVo queryCodeNodeTree2(@RequestParam(value = "parentId", required = false) String parentId) {
        LOGGER.info(parentId);
        String parentId2 = "";
        if ("1".equals(parentId)) {
            parentId2 = "1000";
        } else if ("2".equals(parentId)) {
            parentId2 = "1001";
        } else if ("3".equals(parentId)) {
            parentId2 = "1002";
        } else if ("4".equals(parentId)) {
            parentId2 = "1003";
        } else if ("5".equals(parentId)) {
            parentId2 = "1004";
        } else if ("61".equals(parentId)) {
            parentId2 = "1201";
        }
        else if ("62".equals(parentId)) {
            parentId2 = "1202";
        }
        else if ("63".equals(parentId)) {
            parentId2 = "1203";
        }
        else if ("64".equals(parentId)) {
            parentId2 = "1204";
        }
        else if ("65".equals(parentId)) {
            parentId2 = "1205";
        } else if ("66".equals(parentId)) {
            parentId2 = "1206";
        }else if ("67".equals(parentId)) {
            parentId2 = "1207";
        }else if ("68".equals(parentId)) {
            parentId2 = "1208";
        }else if ("69".equals(parentId)) {
            parentId2 = "1209";
        }else if ("70".equals(parentId)) {
            parentId2 = "1210";
        }else if ("71".equals(parentId)) {
            parentId2 = "1211";
        }

        return codeStandardServiceImpl.queryCodeNodeTree2(parentId2);

    }

    @AuthAnnotation(value = {"032002", "033002"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/addNodeVos", method = RequestMethod.POST)
    public boolean addNodeVos(@RequestParam(value = "sourceId", required = false) int sourceId,
                              @RequestParam(value = "nodeId", required = false) int nodeId,
                              @RequestBody List<CodeNodeVo> voList) {
        LOGGER.info(sourceId + "===========" + nodeId + "===============" + voList);
        return this.codeStandardServiceImpl.saveNodeVos(sourceId, nodeId, voList, getUserInfo().getUserId());
    }

    @AuthAnnotation(value = {"032002", "033002"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/addCodeVos", method = RequestMethod.POST)
    public boolean addCodeVos(@RequestParam(value = "sourceId", required = false) int sourceId,
                              @RequestParam(value = "nodeId", required = false) int nodeId,
                              @RequestBody List<CodeInfoVo> voList) {
        LOGGER.info(sourceId + "===========" + nodeId + "===============" + voList);
        return codeStandardServiceImpl.saveCodeVos(sourceId, nodeId, voList, this.getUserId());
    }

    @AuthAnnotation(value = {"032003", "033003"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/updateNodeVo", method = RequestMethod.POST)
    public boolean updateNodeVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                @RequestBody CodeNodeVo vo) {
        return this.codeStandardServiceImpl.updateNodeVo(sourceId, vo, getUserInfo().getUserId());
    }

    @AuthAnnotation(value = {"032003", "033003"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/updateCodeVo", method = RequestMethod.POST)
    public boolean updateCodeVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                @RequestBody CodeInfoVo vo) {
        return this.codeStandardServiceImpl.updateCodeVo(sourceId, vo, this.getUserId());
    }

    @AuthAnnotation(value = {"032004", "033004"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/deleteNodeVo", method = RequestMethod.GET)
    public boolean deleteNodeVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                @RequestParam(value = "nodeId", required = false) int nodeId) {

        if (StringUtil.isNotBlank(nodeId + "")) {
            //先删除节点下 对应的 data 数据
            boolean b = this.codeStandardServiceImpl.deleteCodeVoByNodeId(sourceId, nodeId, getUserId());
            if (b) {
                //再删除节点
                this.codeStandardServiceImpl.deleteNodeVo(sourceId, nodeId, getUserId());
            }
        }
        return true;
    }

    @AuthAnnotation(value = {"032004", "033004"}, conditions = {"sourceId=1", "sourceId=2"})
    @RequestMapping(path = "/deleteCodeVo", method = RequestMethod.GET)
    public boolean deleteCodeVo(@RequestParam(value = "sourceId", required = false) int sourceId,
                                @RequestParam(value = "codeId", required = false) int codeId) {
        return this.codeStandardServiceImpl.deleteCodeVo(sourceId, codeId, this.getUserId());
    }

}
