package com.gtafe.data.center.dataetl.plsql.service.impl;

import com.gtafe.data.center.common.common.util.empty.EmptyUtil;
import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.data.center.dataetl.datatask.mapper.DataTaskMapper;
import com.gtafe.data.center.dataetl.datatask.vo.DataTaskVo;
import com.gtafe.data.center.dataetl.plsql.mapper.PlsqlMapper;
import com.gtafe.data.center.dataetl.plsql.service.PlsqlService;
import com.gtafe.data.center.dataetl.plsql.utils.DbInfo;
import com.gtafe.data.center.dataetl.plsql.vo.ColumnDetail;
import com.gtafe.data.center.dataetl.plsql.vo.ItemDetailVo;
import com.gtafe.data.center.dataetl.plsql.vo.PlsqlVo;
import com.gtafe.data.center.dataetl.plsql.vo.SearchResultVo;
import com.gtafe.framework.base.controller.BaseController;
import com.gtafe.framework.base.exception.OrdinaryException;
import com.gtafe.framework.base.utils.PropertyUtils;
import com.gtafe.framework.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Service
public class PlsqlServiceImpl extends BaseController implements PlsqlService {

    @Autowired
    private PlsqlMapper plsqlMapper;
    @Autowired
    private DatasourceMapper datasourceMapper;
    @Autowired
    private DataTaskMapper dataTaskMapper;


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
            String[] keys = sqlKeys.split(";");
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
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            System.out.println("******************************************");
            DbInfo dbInfo = new DbInfo();
            dbInfo.getRsInfo(datasourceVO, sqlstr);
            System.out.println("******************************************");
            ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
            connection = connectDB.getConn();
            if (connection == null) {
                throw new OrdinaryException("无法取得合适的链接！ 请检查！");
            }
            statement
                    = connection.createStatement();
            rs = statement.executeQuery(sqlstr);
            if (rs.next()) {
                return true;
            }
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
    public PlsqlVo getInfoById(String id) {
        System.out.println("id======" + id);
        return plsqlMapper.getInfoById(Integer.parseInt(id));
    }

    @Override
    public boolean insertData(PlsqlVo vo) {
        vo.setCreateTime(new Date());
        vo.setCreator(getUserId());
        vo.setUpdateTime(new Date());
        vo.setUpdator(getUserId());
        vo.setStatus(true);
        //System.out.println(vo.toString());
        checkAliansName(vo.getAliansName(), vo.getOrgId(), 0);
        plsqlMapper.insertData(vo);
        int pid = vo.getId();
        // 根据sql脚本 分解item 详细字段说明
        List<ItemDetailVo> itemDetailVos = generatItemDetailVos(vo, pid);
        System.out.println(itemDetailVos.size());
        //保存入库
        for (ItemDetailVo vo1 : itemDetailVos) {
            this.plsqlMapper.insertItemDetail(vo1);
        }
        return true;
    }

    private List<ItemDetailVo> generatItemDetailVos(PlsqlVo vo, int pid) {
        List<ItemDetailVo> vos = new ArrayList<ItemDetailVo>();
        String sqlstr = vo.getContent();
        sqlstr = sqlstr.toUpperCase();
        String sqlKeys = PropertyUtils.getProperty("config.properties", "SQLKEYS");
        if (StringUtil.isNotBlank(sqlKeys)) {
            String[] keys = sqlKeys.split(";");
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
        try {
            PreparedStatement pst = connection.prepareStatement(sqlstr);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();//结果集元
            Integer columnnums = rsmd.getColumnCount();
            for (int i = 1; i < columnnums + 1; i++) {
                ItemDetailVo vvv = new ItemDetailVo();

                System.out.println(i + "列在数据库中类型的最大字符个数" + rsmd.getColumnDisplaySize(i));
                vvv.setDisplaySize(rsmd.getColumnDisplaySize(i));

                System.out.println(i + "列的默认的列的标题" + rsmd.getColumnLabel(i));
                vvv.setColumnLabel(rsmd.getColumnLabel(i));

                System.out.println(i + "列在数据库中的类型，返回类型全名" + rsmd.getColumnTypeName(i));
                vvv.setTypeName(rsmd.getColumnTypeName(i));

                System.out.println(i + "列类型的精确度(类型的长度): " + rsmd.getPrecision(i));
                vvv.setPreci(rsmd.getPrecision(i));

                System.out.println(i + "列小数点后的位数 " + rsmd.getScale(i));
                vvv.setScal(rsmd.getScale(i));

                System.out.println(i + "列是否自动递增" + rsmd.isAutoIncrement(i));
                vvv.setAutoIncrement(rsmd.isAutoIncrement(i));

                System.out.println(i + "列在数据库中是否为货币型" + rsmd.isCurrency(i));
                vvv.setCurrency(rsmd.isCurrency(i));

                System.out.println(i + "列是否为空" + rsmd.isNullable(i));
                vvv.setNullable(rsmd.isNullable(i));

                System.out.println(i + "列是否为只读" + rsmd.isReadOnly(i));
                vvv.setReadOnly(rsmd.isReadOnly(i));

                vvv.setSqlId(pid);
                vos.add(vvv);
            }
            pst.close();
            rs.close();
        } catch (Exception e) {
            if (connection != null) {
                connectDB.closeDbConn(connection);
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connectDB.closeDbConn(connection);
            }
        }
        return vos;
    }


    @Override
    public boolean updateData(PlsqlVo vo) {
        vo.setUpdateTime(new Date());
        vo.setUpdator(getUserId());
        System.out.println(vo.toString());
        //先删掉 字段属性明细数据
        plsqlMapper.deleteItemsById(vo.getId());
        //再重新组装
        List<ItemDetailVo> itemDetailVos = generatItemDetailVos(vo, vo.getId());
        System.out.println(itemDetailVos.size());
        //保存入库
        for (ItemDetailVo vo1 : itemDetailVos) {
            this.plsqlMapper.insertItemDetail(vo1);
        }
        checkAliansName(vo.getAliansName(), vo.getOrgId(), vo.getId());
        // 然后修改主数据
        plsqlMapper.updateData(vo);
        return true;
    }

    private void checkAliansName(String aliansName, String orgId, int id) {
        int countAliansName = this.plsqlMapper.checkAliansNameRepeat(aliansName, orgId, id);
        if (countAliansName > 0) {
            throw new OrdinaryException("别名已经存在了，请重新输入！");
        }
    }

    @Override
    public void deleteBatchs(List<Integer> idList) {
        for (Integer idd : idList) {
            if (checkIsUseing(idd)) {
                throw new OrdinaryException("存在已经被使用的查询语句!");
            }
            plsqlMapper.deleteItemsById(idd);
            plsqlMapper.deleteById(idd);
        }
    }

    private boolean checkIsUseing(Integer idd) {
        boolean b = false;
        PlsqlVo v = this.plsqlMapper.getInfoById(idd);
        String name = v.getAliansName() + "#U";
        int a = this.dataTaskMapper.getCountByAlianName(name);
        if (a > 0) {
            b = true;
        }
        return b;
    }


    @Override
    public boolean upDateColumn(ColumnDetail vo) {
        System.out.println(vo.toString());
        this.plsqlMapper.upDateColumn(vo);
        return true;
    }


    @Override
    public List<ItemDetailVo> queryColunDetailList(int pageNum, int pageSize, int sqlId) {
        return this.plsqlMapper.queryColunDetailList(pageNum, pageSize, sqlId);
    }

    @Override
    public SearchResultVo runNow(int id) {
        List<ItemDetailVo> itemDetailVos = this.plsqlMapper.getItemDetailVos(id);
        int ccount = itemDetailVos.size();
        SearchResultVo vo = new SearchResultVo();
        PlsqlVo plsqlVo = this.plsqlMapper.getInfoById(id);
        DatasourceVO datasourceVO = this.datasourceMapper.queryDatasourceInfoById(plsqlVo.getDbSourceId());
        int dbType = datasourceVO.getDbType();
        StringBuffer sbb = new StringBuffer("");
        String coutsqlStr = "select count(*) c  from ";
        if (dbType == 3) {
            sbb.append("select top 100  ");
        } else {
            sbb.append("selet ");
        }
        String[] types = new String[ccount];
        //把类型放到数组中中
        for (int i = 0; i < ccount; i++) {
            ItemDetailVo v = itemDetailVos.get(i);
            types[i] = v.getTypeName();
        }
        for (ItemDetailVo v : itemDetailVos) {
            sbb.append(v.getColumnLabel()).append(",");
        }
        String sqlstr = sbb.toString().substring(0, sbb.length() - 1) + " from ( " + plsqlVo.getContent() + ") xx ";

        long dataCount = 0;
        List<Object[]> dataLists = new ArrayList<Object[]>();
        coutsqlStr += "(" + plsqlVo.getContent() + ")  xx ";
        if (dbType == 1) {//mysql 数据库
            sqlstr = sqlstr + " LIMIT  100 ";
        } else if (dbType == 2) {//oracle 数据库
            sqlstr = plsqlVo.getContent() + " where ROWNUM <= 100 ";
        }
        System.out.println(sqlstr);
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        if (connectDB.getConn() != null) {
            Connection connection = connectDB.getConn();
            try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sqlstr);
                while (rs.next()) {
                    Object[] datas = new Object[ccount];
                    for (int a = 0; a < ccount; a++) {
                        //判断当前的字段类型是什么再做转换
                        if (types[a].equals("datetime")) {
                            datas[a] = rs.getDate(a + 1);
                        } else if (types[a].equals("varchar")) {
                            datas[a] = rs.getString(a + 1);
                        } else if (types[a].equals("int")) {
                            datas[a] = rs.getInt(a + 1);
                        } else {
                            datas[a] = rs.getString(a + 1);
                        }
                    }
                    dataLists.add(datas);
                }
                //执行查询 总数
                rs = st.executeQuery(coutsqlStr);
                while (rs.next()) {
                    dataCount = rs.getInt("C");
                }
                System.out.println(dataCount);
            } catch (Exception e) {
                throw new OrdinaryException("执行查询异常：" + e.getMessage());
            }
        }
        vo.setItemDetailVos(itemDetailVos);
        vo.setDataCount(dataCount);
        vo.setDatas(dataLists);
        String dbTypeStr = "";
        if (datasourceVO.getDbType() == 1) {
            dbTypeStr = "MYSQL";
        }
        if (datasourceVO.getDbType() == 2) {
            dbTypeStr = "ORACLE";
        }
        if (datasourceVO.getDbType() == 3) {
            dbTypeStr = "MSSQL SEVER";
        }
        vo.setDbType(dbTypeStr);
        vo.setSqlName(plsqlVo.getAliansName());
        System.out.println(vo.toString());
        return vo;
    }
}
