package com.gtafe.data.center.information.data.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.gtafe.data.center.system.config.mapper.SysConfigMapper;
import com.gtafe.data.center.system.config.vo.SysConfigVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gtafe.data.center.common.common.constant.LogConstant;
import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.datatask.vo.rule.TableFieldVo;
import com.gtafe.data.center.information.data.mapper.DataStandardItemMapper;
import com.gtafe.data.center.information.data.mapper.DataStandardMapper;
import com.gtafe.data.center.information.data.service.DataStandardItemService;
import com.gtafe.data.center.information.data.vo.DataStandardItemVo;
import com.gtafe.data.center.information.data.vo.DataStandardVo;
import com.gtafe.data.center.metadata.item.vo.ItemVO;
import com.gtafe.data.center.system.log.service.LogService;
import com.gtafe.data.center.system.log.vo.LogInfo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.StringUtil;

@Service
public class DataStandardItemServiceImpl extends BaseController implements DataStandardItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            DataStandardItemServiceImpl.class);
    @Resource
    private DataStandardItemMapper dataStandardItemMapper;
    @Resource
    private DataStandardMapper dataStandardMapper;
    @Resource
    private DatasourceMapper datasourceMapper;
    @Resource
    private DataTaskMapper dataTaskMapper;
    @Resource
    private LogService logServiceImpl;


    @Resource
    private SysConfigMapper sysConfigMapper;

    static PreparedStatement ps = null;
    static ResultSet rs = null;

    @Override
    public List<DataStandardItemVo> querySubclassItemList(int sourceId,
                                                          String subclassCode,
                                                          int pageNum,
                                                          int pageSize) {
        return dataStandardItemMapper.querySubclassItemList(sourceId, subclassCode, pageNum, pageSize);
    }

    @Override
    public List<TableFieldVo> queryItemListAll(int sourceId,
                                               String subclassCode) {
        List<ItemVO> list = this.dataStandardItemMapper.queryItemListByParams(subclassCode, sourceId);
        List<TableFieldVo> result = new ArrayList<TableFieldVo>();
        if (list != null) {
            for (ItemVO vo : list) {
                TableFieldVo info = new TableFieldVo();
                info.setField(vo.getItemName());
                info.setComment(vo.getItemComment());
                info.setDataType(vo.getDataType());
                info.setDecimalLength(0);
                if (EmptyUtil.isBlank(vo.getDataLength())) {
                    info.setLength(0);
                } else {
                    String dataLength = vo.getDataLength().trim();
                    if (dataLength.matches("^[0-9]+$")) {
                        info.setLength(Long.parseLong(vo.getDataLength()));
                    } else if (dataLength.matches("^[0-9]+\\,\\ ?[0-9]+$")) {
                        String[] ls = dataLength.split(",");
                        info.setLength(Long.parseLong(ls[0]));
                        info.setDecimalLength(Long.parseLong(ls[1]));
                    } else {
                        info.setLength(0);
                        LOGGER.error("未知的字段长度，子类code：" + subclassCode + ",字段名：" + info.getField());
                    }
                }
                info.setNullable(vo.getDataNullable());
                info.setPrimarykey(vo.getDataPrimarykey());
                result.add(info);
            }
        }
        return result;
    }

    @Override
    public List<DataStandardItemVo> queryItemList(int sourceId, String code,
                                                  int nodeType, String keyWord, int pageNum,
                                                  int pageSize) {
        return dataStandardItemMapper.queryItemList(sourceId, code, nodeType, keyWord, pageNum, pageSize);
    }

    @Override
    public DataStandardItemVo getItemById(int id) {
        return dataStandardItemMapper.getItemById(id);
    }

    @Override
    public boolean addItemVo(int sourceId, DataStandardItemVo itemVo) {
        DataStandardVo subclassVo = dataStandardMapper.getDataStandardVo(itemVo.getSubclassCode());
        if (subclassVo == null || subclassVo.getNodeType() != 3) {
            throw new OrdinaryException("子类节点不存在或已被删除！");
        }
        this.checkTaskUsing(itemVo.getSubclassCode(), sourceId);
        DataStandardItemVo repeatVo = this.dataStandardItemMapper.checkCodeNameRepeat(itemVo);
        if (repeatVo != null) {
            if (repeatVo.getItemCode().equals(itemVo.getItemCode())) {
                throw new OrdinaryException("编号已存在！");
            }
            if (repeatVo.getItemName().equals(itemVo.getItemName())) {
                throw new OrdinaryException("数据项名已存在！");
            }
        }
        //根据子集编号自动编号 ++1
        String subclassCode = itemVo.getSubclassCode();
        String itemCode = "";
        //取最大的id 对应的itemCode 自动+1
        String maxItemCode = this.dataStandardItemMapper.getMaxItemCode(subclassCode);
        if (StringUtil.isNotBlank(maxItemCode)) {
            String[] a = maxItemCode.split(subclassCode);
            if (a.length > 1) {
                String numbers = a[1];
                if (StringUtil.isNotBlank(numbers)) {
                    Integer nn = Integer.parseInt(numbers);
                    nn++;
                    if (nn >= 1 && nn < 9) {
                        itemCode = subclassCode + "00" + nn;
                    }
                    if (nn >= 9 && nn < 99) {
                        itemCode = subclassCode + "0" + nn;
                    }
                    if (nn >= 99 && nn < 999) {
                        itemCode = subclassCode + nn;
                    }
                }
            }
        } else {
            itemCode = subclassCode + "001";
        }
        itemVo.setItemCode(itemCode);
        this.dataStandardItemMapper.insertDataStandardItemVo(itemVo, sourceId, this.getUserId());
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Standard_Item);
        logInfo.setOperType("新增");
        logInfo.setOperContent("新增元数据:" + itemVo.getItemCode() + "/" + itemVo.getItemName());
        this.logServiceImpl.saveLog(logInfo);
        this.rebuildTableByItem(itemVo.getSubclassCode(), sourceId);
        return true;
    }

    /**
     * 检查是否存在资源任务使用数据表
     *
     * @author 汪逢建
     * @date 2017年12月12日
     */
    private void checkTaskUsing(String subclassCode, int sourceId) {
        List<DataTaskVo> dataTaskVoList = this.dataTaskMapper.findTasksBySubclass(subclassCode);
        if (!dataTaskVoList.isEmpty()) {
            StringBuffer fbtask = new StringBuffer("");
            StringBuffer dytask = new StringBuffer("");
            for (DataTaskVo vo : dataTaskVoList) {
                if (vo.getBusinessType() == 1) {
                    fbtask.append(vo.getTaskName()).append(",");
                } else {
                    dytask.append(vo.getTaskName()).append(",");
                }
            }
            StringBuffer error = new StringBuffer();
            if (fbtask.length() > 0) {
                error.append("发布资源【").append(fbtask.substring(0, fbtask.length() - 1)).append("】");
            }
            if (dytask.length() > 0) {
                if (error.length() > 0) {
                    dytask.append("，");
                }
                error.append("订阅资源【").append(dytask.substring(0, dytask.length() - 1)).append("】");
            }
            String msg = "当前元数据相关的数据表被" + error.toString() + "引用，请先删除数据资源任务!";
            throw new OrdinaryException(msg);
        }
    }

    @Override
    public boolean rebuildSubclassTable(String subclassCode) {
        this.rebuildTableByItem(subclassCode, 1);
        return true;
    }

    private void rebuildTableByItem(String subclassCode, int sourceId) {
        boolean res = false;
        DataStandardVo subclassVo = this.dataStandardMapper.getDataStandardVo(subclassCode);
        if (subclassVo == null) {
            throw new OrdinaryException("数据子类不存在，或已被删除！");
        }
        sourceId = subclassVo.getSourceId();
        String tableName = subclassVo.getTableName();
        LOGGER.info(tableName);
        res = createTable(subclassCode, tableName, sourceId);
        LOGGER.info("ok 表[" + tableName + "]已经创建了。。。");
    }

    /**
     * 判断表是否存在 如果存在 则先删除表 再创建表
     *
     * @param
     * @return
     */
    private boolean createTable(String subclassCode, String tableName, int sourceId) {
        boolean flag = false;
        LOGGER.info(subclassCode);
        String sql = "";
        //根据itemVo 找到表名称
        if (StringUtil.isNotBlank(tableName)) {
            //开始验证中心库中是否存在此表
            List<Integer> a = new ArrayList<>();
            a.add(0);
            //   List<DatasourceVO> dvos = this.datasourceMapper.queryCenterData();
            //如果不存在 或者 配置多个中心库 或者 没配置
            // if (dvos == null || dvos.size() == 0 || dvos.isEmpty()) {
            //  throw new OrdinaryException("请检查中心库配置！");
            //}
            SysConfigVo vo = sysConfigMapper.queryCenterDbInfo();
            // LOGGER.info(datasourceVO.toString());
            ConnectDB connectDB = StringUtil.getEntityBySysConfig(vo);
            Connection connection = connectDB.getConn();
            if (connection != null) {
                //mysql
                String dbType = vo.getDbType();
                int dType = Integer.parseInt(dbType);
                if (dType == 1) {
                    sql = "SELECT COLUMN_NAME  FROM INFORMATION_SCHEMA. COLUMNS WHERE table_name = '" + tableName + "' AND table_schema = '" + vo.getDbName() + "'";
                } else if (dType == 2) {
                    //oracle
                    sql = "select  COLS.COLUMN_NAME COLUMN_NAME from user_tab_cols COLS   where COLS.TABLE_NAME='" + tableName + "'";
                } else if (dType == 3) {
                    //sqlserver
                    sql = "SELECT  CAST (col.name AS NVARCHAR(128)) COLUMN_NAME FROM sys.objects obj  WHERE obj.name ='" + tableName + "'";
                }
                try {
                    LOGGER.info(sql);
                    ps = connection.prepareStatement(sql);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        LOGGER.info("表存在");
                        LOGGER.info("需要先删除表 再组装建表语句~ 建表！");
                        dropTable(tableName, connection);
                        LOGGER.info("旧表已经删除！");
                    }
                    ps.close();
                    sql = this.compactCreateTableSql(subclassCode, tableName, dType, sourceId);
                    LOGGER.info("建表语句sql:" + sql);
                    ps = connection.prepareStatement(sql);
                    boolean b = ps.execute();
                    flag = b;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    connectDB.closeDbConn(connection);
                }
            }
        }
        return flag;
    }


    /**
     * 组装 建表 语句
     *
     * @param tableName
     * @param dbType
     * @return
     */
    private String compactCreateTableSql(String subclassCode, String tableName, int dbType, int sourceId) {
        List<DataStandardItemVo> itemVOS = this.dataStandardItemMapper.queryItemListBy(subclassCode, sourceId);
        return StringUtil.createSql(tableName, itemVOS, dbType);
    }


    private boolean dropTable(String tableName, Connection connection) {
        boolean b = false;
        String sql = "drop table " + tableName;
        try {
            ps = connection.prepareStatement(sql);
            b = ps.execute();
            if (b) {
                LOGGER.info("删除成功!");
            } else {
                LOGGER.info("删除失败!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public boolean updateItemVo(int sourceId, DataStandardItemVo itemVo) {
        DataStandardItemVo dbVo = this.dataStandardItemMapper.getItemById(itemVo.getId());
        if (dbVo == null) {
            throw new OrdinaryException("当前元数据不存在或已被删除！");
        }
        this.checkTaskUsing(itemVo.getSubclassCode(), sourceId);
        boolean b = dataStandardItemMapper.updateDataStandardItemVo(itemVo, sourceId, this.getUserId());
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Standard_Item);
        logInfo.setOperType("修改");
        logInfo.setOperContent("修改元数据:" + itemVo.getItemCode() + "/" + itemVo.getItemName() + "，" + itemVo.getItemComment());
        this.logServiceImpl.saveLog(logInfo);
        if (b) {
            this.rebuildTableByItem(itemVo.getSubclassCode(), sourceId);
        }
        return true;
    }

    @Override
    public boolean deleteItemVo(int sourceId, int id) {
        DataStandardItemVo dbVo = this.dataStandardItemMapper.getItemById(id);
        if (dbVo == null) {
            throw new OrdinaryException("元数据不存在，或已被删除！");
        }
        this.checkTaskUsing(dbVo.getSubclassCode(), sourceId);

        List<DataStandardItemVo> vos = this.dataStandardItemMapper.querySubclassItemList(sourceId, dbVo.getSubclassCode(),
                1, 1);
        DataStandardVo vo = this.dataStandardMapper.getDataStandardVo(dbVo.getSubclassCode());
        if (vos.size() == 0) {
            //没有元数据的话就得删除表了
    /*        List<DatasourceVO> dvos = this.datasourceMapper.queryCenterData();
            //如果不存在 或者 配置多个中心库 或者 没配置
            if (dvos == null || dvos.size() == 0 || dvos.isEmpty()) {
                throw new OrdinaryException("请检查中心库配置！");
            }
            DatasourceVO datasourceVO = dvos.get(0);
            LOGGER.info(datasourceVO.toString());*/
            ConnectDB connectDB = StringUtil.getEntityBySysConfig(sysConfigMapper.queryCenterDbInfo());
            Connection connection = connectDB.getConn();
            this.dropTable(vo.getTableName(), connection);
        }

        dataStandardItemMapper.deleteDataStandardItemVo(id, sourceId);
        LogInfo logInfo = new LogInfo();
        logInfo.setModuleId(LogConstant.Module_Standard_Item);
        logInfo.setOperType("删除");
        logInfo.setOperContent("删除元数据:" + dbVo.getItemCode() + "/" + dbVo.getItemName());
        this.logServiceImpl.saveLog(logInfo);
        return true;
    }
}
