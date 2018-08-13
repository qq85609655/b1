package com.gtafe.data.center.dataetl.plsql.service.impl;

import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.plsql.mapper.PlsqlMapper;
import com.gtafe.data.center.dataetl.plsql.service.PlsqlService;
import com.gtafe.data.center.dataetl.plsql.utils.DbInfo;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.PropertyUtils;
import com.gtafe.framework.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlsqlServiceImpl implements PlsqlService {

    @Autowired
    private PlsqlMapper plsqlMapper;
    @Autowired
    private DatasourceMapper datasourceMapper;


    @Override
    public List<PlsqlVo> queryList(int pageNum, int pageSize, String orgIds, String nameKey) {
        List<String> orgIdList = StringUtil.splitListString(orgIds);
        if (orgIdList.isEmpty()) {
            return EmptyUtil.emptyList(pageSize, PlsqlVo.class);
        }
        return plsqlMapper.queryList(pageNum, pageSize, orgIdList, nameKey);
    }

    /**
     * 检测 脚本能不能查询 并且检测 脚本里面 是否 含义 delete  update  drop truncate create 等关键字
     *
     * @param vo
     * @return
     */
    @Override
    public boolean checkOut(PlsqlVo vo) {
        String sqlstr = vo.getContent();
        sqlstr = sqlstr.toUpperCase();
        String sqlKeys = PropertyUtils.getProperty("config.properties", "SQLKEYS");
        if (StringUtil.isNotBlank(sqlKeys)) {
            String[] keys = sqlKeys.split("||");
            if (StringUtil.isNotBlank(sqlstr)) {
                for (String s : keys) {
                    if (sqlstr.indexOf(s) > -1) {
                        throw new OrdinaryException("包含非法关键字：" + s + ",请检查！");
                    }
                }
            }
        }
        int dbSourceId = vo.getDbSourceId();
        if (dbSourceId == 0) {
            throw new OrdinaryException("请配置选择数据来源库！");
        }
        DatasourceVO datasourceVO = this.datasourceMapper.queryDatasourceInfoById(dbSourceId);
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        Connection connection = connectDB.getConn();
        Statement statement = null;
        ResultSet rs = null;
        if (connection == null) {
            throw new OrdinaryException("无法取得合适的链接！ 请检查！");
        }
        try {
            statement
                    = connection.createStatement();
            rs = statement.executeQuery(sqlstr);

            if (rs.next()) {
                return true;
            }
            System.out.println("******************************************");
            DbInfo dbInfo = new DbInfo();
            dbInfo.getRsInfo(datasourceVO, sqlstr);
            System.out.println("******************************************");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            //6.关闭数据库相应的资源
            this.release(rs, statement, connection);
        }

        return true;
    }

    private void release(ResultSet rs, Statement statement, Connection connection) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public PlsqlVo getInfoById(int id) {
        return plsqlMapper.getInfoById(id);
    }

    @Override
    public boolean insertData(PlsqlVo vo) {
        return plsqlMapper.insertData(vo);
    }

    @Override
    public boolean updateData(PlsqlVo vo) {
        return plsqlMapper.updateData(vo);
    }

    @Override
    public void deleteBatchs(List<Integer> idList) {
        for (Integer idd : idList) {
            plsqlMapper.deleteById(idd);
        }
    }


}
