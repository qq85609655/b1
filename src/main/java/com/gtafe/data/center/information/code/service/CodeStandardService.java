package com.gtafe.data.center.information.code.service;

import java.util.List;

import com.gtafe.data.center.information.code.vo.MysqlTableVo;
import com.gtafe.data.center.information.code.vo.CodeInfoVo;
import com.gtafe.data.center.information.code.vo.CodeNodeVo;
import com.gtafe.data.center.information.code.vo.TableEntity;

public interface CodeStandardService {
    /**
     * 查询代码标准列表
     *
     * @author 汪逢建
     * @date 2017年11月8日
     */
    List<CodeInfoVo> queryCodeList(String keyWord, int nodeId, int sourceId, int nodeType, int pageNum, int pageSize);

    List<CodeInfoVo> queryCodeInfoPage(int nodeId, int pageNum, int pageSize);

    /**
     * 查询代码标准树
     *
     * @author 汪逢建
     * @date 2017年11月6日
     */
    CodeNodeVo queryCodeNodeTree(int sourceId);

    CodeNodeVo queryCodeNodeTree2(String parentId);

    boolean updateCodeVo(int sourceId, CodeInfoVo voList, int userId);

    boolean deleteCodeVo(int sourceId, int codeId, int userId);

    boolean saveNodeVos(int sourceId, int nodeId, List<CodeNodeVo> voList, int userId);

    boolean updateNodeVo(int sourceId, CodeNodeVo vo, int userId);

    boolean deleteNodeVo(int sourceId, int nodeId, int userId);

    boolean saveCodeVos(int sourceId, int nodeId, List<CodeInfoVo> voList, int userId);

    List<CodeInfoVo> queryCodeList2(String keyWord, String parentId_, int sourceId, int pageNum, int pageSize);

    boolean deleteCodeVoByNodeId(int sourceId, int nodeId, int userId);

    List<TableEntity> queryAllCenterTableList(String tableName, int pageNum, int pageSize);

    byte[] generatorCode(List<String> tableNamesList) throws Exception;

    List<CodeNodeVo> queryNodesByParentId(int parentNode, String type);

    List<CodeInfoVo> queryCodeALL(int nodeId);


}