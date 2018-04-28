package com.gtafe.data.center.information.code.service.impl;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;

import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.information.code.vo.MysqlTableVo;
import com.gtafe.data.center.information.code.vo.TableEntity;
import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.GenUtils;
import com.gtafe.framework.base.utils.StringUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.information.code.mapper.CodeStandardMapper;
import com.gtafe.data.center.information.code.service.CodeStandardService;
import com.gtafe.data.center.information.code.vo.CodeInfoVo;
import com.gtafe.data.center.information.code.vo.CodeNodeVo;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;

import static com.gtafe.data.center.common.common.constant.LogConstant.*;

@Service
public class CodeStandardServiceImpl implements CodeStandardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeStandardServiceImpl.class);
    @Resource
    private CodeStandardMapper codeStandardMapper;
    @Autowired
    private LogService logServiceImpl;

    @Resource
    private SysConfigMapper sysConfigMapper;

    @Override
    public List<CodeInfoVo> queryCodeList(String keyWord, int nodeId, int sourceId,
                                          int nodeType, int pageNum, int pageSize) {
        return codeStandardMapper.queryCodeList(keyWord, nodeId, sourceId, nodeType, pageNum, pageSize);
    }


    @Override
    public List<CodeInfoVo> queryCodeInfoPage(int nodeId, int pageNum, int pageSize) {
        return codeStandardMapper.queryCodeInfoPage(nodeId, pageNum, pageSize);
    }

    @Override
    public CodeNodeVo queryCodeNodeTree(int sourceId) {
        List<CodeNodeVo> list = this.codeStandardMapper.queryCodeNodes(sourceId);
        Map<Integer, CodeNodeVo> map = new HashMap<Integer, CodeNodeVo>();
        for (CodeNodeVo vo : list) {
            map.put(vo.getNodeId(), vo);
        }
        CodeNodeVo root = null;
        for (CodeNodeVo vo : list) {
            if (vo.getParentnodeId() <= 0) {
                root = vo;
            }
            CodeNodeVo parent = map.get(vo.getParentnodeId());
            if (parent != null) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<CodeNodeVo>());
                }
                parent.getChildren().add(vo);
            }
        }
        return root;
    }


    @Override
    public CodeNodeVo queryCodeNodeTree2(String parentId) {
        List<CodeNodeVo> list = this.codeStandardMapper.queryCodeNodesByParentId(parentId);
        Map<Integer, CodeNodeVo> map = new HashMap<Integer, CodeNodeVo>();
        for (CodeNodeVo vo : list) {
            map.put(vo.getNodeId(), vo);
        }
        CodeNodeVo root = null;
        for (CodeNodeVo vo : list) {
            if (vo.getParentnodeId() == 1) {
                root = vo;
            }
            CodeNodeVo parent = map.get(vo.getParentnodeId());
            if (parent != null) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<CodeNodeVo>());
                }
                parent.getChildren().add(vo);
            }
        }
        return root;
    }

    private int getParentNodeIdByCodeId(int codeId) {
        int pNodeId = 0;
        CodeInfoVo infoVo = this.codeStandardMapper.queryCodeEntity(codeId);
        int nodeId = infoVo.getNodeId();
        CodeNodeVo parentNodeVo = this.codeStandardMapper.queryNodeEntity(nodeId);
        if (parentNodeVo != null) {
            if (parentNodeVo.getNodeType() == NODE_TYPE_SECOND) {
                pNodeId = parentNodeVo.getParentnodeId();
            } else if (parentNodeVo.getNodeType() == NODE_TYPE_THIRE) {
                CodeNodeVo parentNodeVo3 = this.codeStandardMapper.queryNodeEntity(parentNodeVo.getParentnodeId());
                pNodeId = parentNodeVo3.getParentnodeId();
            }
        }
        return pNodeId;
    }

    @Override
    public boolean updateNodeVo(int sourceId, CodeNodeVo vo, int userId) {
        CodeNodeVo oldCodeNodeVo = this.codeStandardMapper.queryNodeEntity(vo.getNodeId());
        boolean result = this.codeStandardMapper.updateNode(vo.getNodeId(), vo.getName(), vo.getDescription(), userId);
        LogInfo logInfo = new LogInfo();
        int pNodeId = oldCodeNodeVo.getParentnodeId();
        if (vo.getNodeType() == NODE_TYPE_THIRE) {
            CodeNodeVo pNodevo = this.codeStandardMapper.queryNodeEntity(oldCodeNodeVo.getParentnodeId());
            if (pNodevo != null) {
                pNodeId = pNodevo.getParentnodeId();
            }
        }
        logInfo.setModuleId(getModuleId(sourceId, pNodeId));
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改" + getName(sourceId, pNodeId) + "代码类信息： " + vo.getCode() + "/[" + oldCodeNodeVo.getName() + " --> " + vo.getName() + "]");
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }


    private int getModuleId(int sourceId, int parentNodeId) {
        if (sourceId == DICTIONARY) {
            return LogConstant.Module_Code_School;
        } else {
            if (parentNodeId == Module_Code_BASIC_CODE) {
                return LogConstant.Module_Code_BASIC;
            } else if (parentNodeId == Module_Code_GBRS_CODE) {
                return LogConstant.Module_Code_GBRS;
            } else if (parentNodeId == Module_Code_GJ_CODE) {
                return LogConstant.Module_Code_GJ;
            } else if (parentNodeId == Module_Code_HY_CODE) {
                return LogConstant.Module_Code_HY;
            } else if (parentNodeId == Module_Code_XK_CODE) {
                return LogConstant.Module_Code_XK;
            }
        }
        return 0;
    }

    private String getName(int sourceId, int parentNodeId) {
        String name = "";
        if (sourceId == DICTIONARY) {
            name = "数据字典";
        } else {
            if (parentNodeId == Module_Code_BASIC_CODE) {
                name = "标准代码-教育基础代码";
            } else if (parentNodeId == Module_Code_GBRS_CODE) {
                name = "标准代码-干部人事管理代码";
            } else if (parentNodeId == Module_Code_GJ_CODE) {
                name = "标准代码-国家标准代码";
            } else if (parentNodeId == Module_Code_HY_CODE) {
                name = "标准代码-行业标准代码";
            } else if (parentNodeId == Module_Code_XK_CODE) {
                name = "标准代码-学科专业资产代码";
            }
        }
        return name;
    }

    @Override
    public boolean deleteNodeVo(int sourceId, int nodeId, int userId) {
        CodeNodeVo nodeVo = this.codeStandardMapper.queryNodeEntity(nodeId);
        boolean result = false;
        int pNodeId = nodeVo.getParentnodeId();
        if (nodeVo.getNodeType() == NODE_TYPE_THIRE) {
            pNodeId = this.codeStandardMapper.queryNodeEntity(nodeVo.getParentnodeId()).getParentnodeId();
        }
        if (nodeVo != null) {
            String code = nodeVo.getCode();
            String name = nodeVo.getName();
            result = this.codeStandardMapper.deleteNodeBy(nodeId);
            if (result) {
                LogInfo logInfo = new LogInfo();
                logInfo.setModuleId(getModuleId(sourceId, pNodeId));
                logInfo.setOperType("删除");
                logInfo.setOperContent("删除" + getName(sourceId, pNodeId) + "代码类信息：" + code + "/" + name);
                this.logServiceImpl.saveLog(logInfo);
            }
        }
        return result;
    }

    @Override
    public List<CodeInfoVo> queryCodeALL(int nodeId) {
        return this.codeStandardMapper.queryCodeALL(nodeId);
    }

    @Override
    public List<CodeNodeVo> queryNodesByParentId(int ParentId, String type) {
        List<CodeNodeVo> list = new ArrayList<CodeNodeVo>();
        //  if (type.equals("1")) {
        list = this.codeStandardMapper.queryDataNodeBy(ParentId + "");
        //   }
        return list;
    }

    @Override
    public boolean saveNodeVos(int sourceId, int nodeId, List<CodeNodeVo> voList, int userId) {
        StringBuffer operContent = new StringBuffer();
        int pNodeId = 0;
        //nodeId 为父级节点
        CodeNodeVo codeNodeVo = this.codeStandardMapper.queryNodeEntity(nodeId);
        if (codeNodeVo.getNodeType() == NODE_TYPE_SECOND) {
            pNodeId = codeNodeVo.getParentnodeId();
        } else {
            pNodeId = nodeId;
        }
        for (CodeNodeVo vo : voList) {
            vo.setSourceId(sourceId);
            vo.setParentnodeId(nodeId);
            vo.setCreater(userId);
            if (codeNodeVo != null) {
                int node_type = codeNodeVo.getNodeType();
                vo.setNodeType(node_type + 1);
            }
            LOGGER.info(vo.toString());
            this.codeStandardMapper.saveNodeVo(vo);
            operContent.append(vo.getCode() + "/" + vo.getName()).append(";");
        }
        LOGGER.info("pNodeId======" + pNodeId + ";sourceId====" + sourceId);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getModuleId(sourceId, pNodeId));
        logInfo.setOperType("添加");
        logInfo.setOperContent("添加" + getName(sourceId, pNodeId) + "代码类信息：" + operContent.toString());
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public boolean saveCodeVos(int sourceId, int nodeId, List<CodeInfoVo> voList, int userId) {
        CodeNodeVo nodeVo = this.codeStandardMapper.queryNodeEntity(nodeId);
        int pNodeId = nodeVo.getParentnodeId();
        if (nodeVo != null) {
            if (nodeVo.getNodeType() == NODE_TYPE_THIRE) {
                CodeNodeVo pNodeVO = this.codeStandardMapper.queryNodeEntity(nodeVo.getParentnodeId());
                if (pNodeVO != null) {
                    pNodeId = pNodeVO.getParentnodeId();
                }
            }
        }
        LOGGER.info("pNodeId==" + pNodeId);
        StringBuffer operContent = new StringBuffer();
        for (CodeInfoVo v : voList) {
            v.setNodeId(nodeId);
            v.setCreater(userId);
            v.setUpdater(userId);
            v.setSourceId(sourceId);
            this.codeStandardMapper.saveCodeVo(v);
            operContent.append(v.getCode() + "/" + v.getName()).append(";");
        }
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getModuleId(sourceId, pNodeId));
        logInfo.setOperType("添加");
        logInfo.setOperContent("添加" + getName(sourceId, pNodeId) + "代码信息：" + operContent.toString());
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }

    @Override
    public List<CodeInfoVo> queryCodeList2(String keyWord, String parentId_, int sourceId, int pageNum, int pageSize) {
        return codeStandardMapper.queryCodeList2(keyWord, parentId_, sourceId, pageNum, pageSize);
    }

    @Override
    public boolean deleteCodeVoByNodeId(int sourceId, int nodeId, int userId) {
        CodeNodeVo nodeVo = this.codeStandardMapper.queryNodeEntity(nodeId);
        int pNodeId = nodeVo.getParentnodeId();
        if (nodeVo != null) {
            if (nodeVo.getNodeType() == NODE_TYPE_THIRE) {
                CodeNodeVo pnodeVo = this.codeStandardMapper.queryNodeEntity(nodeVo.getParentnodeId());
                if (pnodeVo != null) {
                    pNodeId = pnodeVo.getParentnodeId();
                }
            }
        }
        List<CodeInfoVo> codeInfoVos = this.codeStandardMapper.queryCodeALL(nodeId);
        if (codeInfoVos.size() > 0) {
            StringBuffer operContent = new StringBuffer();
            for (CodeInfoVo vo : codeInfoVos) {
                operContent.append(vo.getCode() + "/" + vo.getName()).append(";");
            }
            codeStandardMapper.deleteCodesBy(nodeId);
            LogInfo logInfo = new LogInfo();
            logInfo.setModuleId(getModuleId(sourceId, pNodeId));
            logInfo.setOperType("删除");
            logInfo.setOperContent("删除" + getName(sourceId, pNodeId) + "下面的代码信息：" + operContent.toString());
            this.logServiceImpl.saveLog(logInfo);
        }
        return true;
    }

    @Override
    public boolean updateCodeVo(int sourceId, CodeInfoVo v, int userId) {
        int pNodeId = getParentNodeIdByCodeId(v.getCodeId());
        CodeInfoVo oldCodeInfoVo = this.codeStandardMapper.queryCodeEntity(v.getCodeId());
        if (oldCodeInfoVo == null) {
            throw new OrdinaryException("数据已经被删除了!");
        }
        LOGGER.info("pNodeId======" + pNodeId + ";sourceId====" + sourceId);
        v.setCreater(userId);
        v.setUpdater(userId);
        boolean result = this.codeStandardMapper.updateCodeVo(v);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getModuleId(sourceId, pNodeId));
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改" + getName(sourceId, pNodeId) + "的 代码信息：" + v.getCode() + "/[" + oldCodeInfoVo.getName() + "--> " + v.getName() + "," + oldCodeInfoVo.getDescription() + "-->" + v.getDescription() + "]");
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }

    /**
     * 删除代码 需要记录日志：
     * 根据代码找到类信息 并根据类信息找到根类的信息
     *
     * @param sourceId
     * @param codeId
     * @param userId
     * @return
     */
    @Override
    public boolean deleteCodeVo(int sourceId, int codeId, int userId) {
        CodeInfoVo infoVo = this.codeStandardMapper.queryCodeEntity(codeId);
        int pNodeId = getParentNodeIdByCodeId(codeId);
        LOGGER.info("pNodeId======" + pNodeId + ";sourceId====" + sourceId);
        boolean result = this.codeStandardMapper.deleteCodeByCodeId(codeId);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(getModuleId(sourceId, pNodeId));
        logInfo.setOperType("删除");//被删除的代码类编号及名称
        logInfo.setOperContent("删除" + getName(sourceId, pNodeId) + "代码信息:代码类编号为" + infoVo.getCode() + ",名称为:" + infoVo.getName());
        this.logServiceImpl.saveLog(logInfo);
        return result;
    }


    @Override
    public List<TableEntity> queryAllCenterTableList(String tableName, int pageNum, int pageSize) {
        return this.codeStandardMapper.queryAllCenterTableList(tableName, pageNum, pageSize);
    }

    private List<Map<String, String>> queryColumns(String tableName) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        SysConfigVo vo = this.sysConfigMapper.queryCenterDbInfo();
        if (vo != null) {
            ConnectDB connectDB = StringUtil.getEntityBySysConfig(vo);
            Connection connection = null;
            if (connectDB.getConn() != null) {
                try {
                    connection = connectDB.getConn();
                    Statement st = connection.createStatement();
                    String dbType = vo.getDbType();
                    if (dbType.equals("1")) {
                        String sql = "select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns\n" +
                                "  where table_name = '" + tableName + "' and table_schema = (select database()) order by ordinal_position";
                        ResultSet rs = st.executeQuery(sql);
                        while (rs.next()) {
                            Map<String, String> mm = new HashMap<String, String>();
                            String columnName = rs.getString(1);
                            String dataType = rs.getString(2);
                            String columnComment = rs.getString(3);
                            String columnKey = rs.getString(4);
                            String extra = rs.getString(5);
                            if (StringUtil.isNotBlank(columnName)) {
                                mm.put("columnName", columnName);
                            }
                            if (StringUtil.isNotBlank(dataType)) {
                                mm.put("dataType", dataType);
                            }
                            if (StringUtil.isNotBlank(columnComment)) {
                                mm.put("columnComment", columnComment);
                            }
                            if (StringUtil.isNotBlank(columnKey)) {
                                mm.put("columnKey", columnKey);
                            }
                            if (StringUtil.isNotBlank(extra)) {
                                mm.put("extra", extra);
                            }
                            list.add(mm);
                        }
                        connectDB.closeDbConn(connection);
                        connection.close();
                        rs.close();
                        st.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    connectDB.closeDbConn(connection);
                }
            }
        }
        return list;
    }

    /**
     * 根据表名，查询表
     */
    public Map<String, String> queryTable(String tableName) {
        Map<String, String> tableInfo = new HashMap<String, String>();
        SysConfigVo vo = this.sysConfigMapper.queryCenterDbInfo();
        if (vo != null) {
            ConnectDB connectDB = StringUtil.getEntityBySysConfig(vo);
            Connection connection = null;
            if (connectDB.getConn() != null) {
                try {
                    connection = connectDB.getConn();
                    Statement st = connection.createStatement();
                    String dbType = vo.getDbType();
                    String sql = "select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables \n" +
                            " where table_schema = (select database()) and table_name = '" + tableName + "'";
                    ResultSet rs = st.executeQuery(sql);
                    while (rs.next()) {
                        String tableName_ = rs.getString(1);
                        String engine = rs.getString(2);
                        String comment = rs.getString(3);
                        String createTime = rs.getString(4);
                        if (StringUtil.isNotBlank(tableName_)) {
                            tableInfo.put("tableName", tableName_);
                        }
                        if (StringUtil.isNotBlank(engine)) {
                            tableInfo.put("engine", engine);
                        }
                        if (StringUtil.isNotBlank(comment)) {
                            tableInfo.put("comment", comment);
                        }
                        if (StringUtil.isNotBlank(createTime)) {
                            tableInfo.put("createTime", createTime);
                        }
                    }
                    rs.close();
                    st.close();
                    connectDB.closeDbConn(connection);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    connectDB.closeDbConn(connection);
                }


            }
        }
        return tableInfo;
    }

    /**
     * 生成代码文件
     *
     * @param tableNames
     * @return
     */
/*    @Override
    public byte[] generatorCode(List<String> tableNames) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            // 生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }*/

 /*   @Override
    public boolean generatorCode2(List<String> tableNames)  {
        boolean bbb=false;
        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            // 生成代码
            GenUtils.generatorCode2(table, columns);
        }
        return bbb;
    }
*/

    /**
     * 生成代码文件
     *
     * @param tableNames
     * @return
     */
    @Override
    public byte[] generatorCode(List<String> tableNames) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            // 生成代码
            GenUtils.generatorCode(table, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

}
