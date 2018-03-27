package com.gtafe.data.center.information.code.service;

import java.util.List;

import com.gtafe.data.center.information.code.vo.CenterTableVo;
import com.gtafe.data.center.information.code.vo.CodeInfoVo;
import com.gtafe.data.center.information.code.vo.CodeNodeVo;

public interface CodeStandardService {
    /**
     * 查询代码标准列表
     * @author 汪逢建
     * @date 2017年11月8日
     */
    public List<CodeInfoVo> queryCodeList(String keyWord,int nodeId, int sourceId, int nodeType, int pageNum, int pageSize);
    
    /**
     * 查询代码标准树
     * @author 汪逢建
     * @date 2017年11月6日
     */
    public CodeNodeVo queryCodeNodeTree(int sourceId);

    public CodeNodeVo queryCodeNodeTree2(String parentId);
    
    public boolean updateCodeVo(int sourceId, CodeInfoVo voList, int userId) ;
    
    public boolean deleteCodeVo( int sourceId,  int codeId, int userId) ;

    public boolean saveNodeVos(int sourceId, int nodeId, List<CodeNodeVo> voList, int userId);

    public boolean updateNodeVo(int sourceId, CodeNodeVo vo, int userId);

    public boolean deleteNodeVo(int sourceId, int nodeId, int userId);

    public boolean saveCodeVos(int sourceId, int nodeId, List<CodeInfoVo> voList, int userId);

    List<CodeInfoVo> queryCodeList2(String keyWord, String parentId_, int sourceId, int pageNum, int pageSize);

    boolean deleteCodeVoByNodeId(int sourceId, int nodeId, int userId);

    List<CenterTableVo> queryAllCenterTableList(String tableName, String tableType, int pageNum, int pageSize);

    byte[] generatorCode(List<String> tableNamesList) throws Exception;
}
