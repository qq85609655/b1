package com.gtafe.data.center.dataetl.plsql.utils;

import com.gtafe.data.center.dataetl.datasource.mapper.DatasourceMapper;
import com.gtafe.data.center.dataetl.datasource.utils.ConnectDB;
import com.gtafe.data.center.dataetl.datasource.vo.DatasourceVO;
import com.gtafe.framework.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DbInfo {

    /**
     * DatabaseMetaData一些用法
     *
     * @throws Exception
     */
    public void getDBInfo(DatasourceVO datasourceVO) throws Exception {
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        Connection connection = connectDB.getConn();
        DatabaseMetaData dbmd = connection.getMetaData();

        System.out.println(dbmd.getDatabaseProductName());//获取数据库产品名称

        System.out.println(dbmd.getDatabaseProductVersion());//获取数据库产品版本号

        System.out.println(dbmd.getCatalogSeparator());//获取数据库用作类别和表名之间的分隔符   如test.user

        System.out.println(dbmd.getDriverVersion());//获取驱动版本

        System.out.println("*******************可用的数据库列表*********************");
        ResultSet rs = dbmd.getCatalogs();//取可在此数据库中使用的类别名,在mysql中说白了就是可用的数据库名称，只有一列

        while (rs.next()) {

            System.out.println(rs.getString(1));
        }

        System.out.println("********************所有表********************************");
        /**
         * catalog 类别名称
         * schemaPattern 用户方案模式，
         * tableNamePattern 表
         * types 类型
         * 获取所有表
         * dbmd.getTables(catalog, schemaPattern, tableNamePattern, types)
         */

        rs = dbmd.getTables(null, null, null, new String[]{"TABLE"});//参数列表 1:类别名称,2: 模式名称的模式,3:表名称模式,4:要包括的表类型所组成的列表

        while (rs.next()) {

            /** 所有的列信息。如下
             *  TABLE_CAT String => 表类别（可为 null）
             TABLE_SCHEM String => 表模式（可为 null）
             TABLE_NAME String => 表名称
             COLUMN_NAME String => 列名称
             DATA_TYPE int => 来自 java.sql.Types 的 SQL 类型
             TYPE_NAME String => 数据源依赖的类型名称，对于 UDT，该类型名称是完全限定的
             COLUMN_SIZE int => 列的大小。
             BUFFER_LENGTH 未被使用。
             DECIMAL_DIGITS int => 小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 Null。
             NUM_PREC_RADIX int => 基数（通常为 10 或 2）
             NULLABLE int => 是否允许使用 NULL。
             columnNoNulls - 可能不允许使用 NULL 值
             columnNullable - 明确允许使用 NULL 值
             columnNullableUnknown - 不知道是否可使用 null
             REMARKS String => 描述列的注释（可为 null）
             COLUMN_DEF String => 该列的默认值，当值在单引号内时应被解释为一个字符串（可为 null）
             SQL_DATA_TYPE int => 未使用
             SQL_DATETIME_SUB int => 未使用
             CHAR_OCTET_LENGTH int => 对于 char 类型，该长度是列中的最大字节数
             ORDINAL_POSITION int => 表中的列的索引（从 1 开始）
             IS_NULLABLE String => ISO 规则用于确定列是否包括 null。
             YES --- 如果参数可以包括 NULL
             NO --- 如果参数不可以包括 NULL
             空字符串 --- 如果不知道参数是否可以包括 null
             SCOPE_CATLOG String => 表的类别，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
             SCOPE_SCHEMA String => 表的模式，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
             SCOPE_TABLE String => 表名称，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
             SOURCE_DATA_TYPE short => 不同类型或用户生成 Ref 类型、来自 java.sql.Types 的 SQL 类型的源类型（如果 DATA_TYPE 不是 DISTINCT 或用户生成的 REF，则为 null）
             IS_AUTOINCREMENT String => 指示此列是否自动增加
             YES --- 如果该列自动增加
             NO --- 如果该列不自动增加
             空字符串 --- 如果不能确定该列是否是自动增加参数

             */
            System.out.println(rs.getString(3) + "->" + rs.getString(4));//打印表类别,表模式,表名称，列名称，


        }


        System.out.println("##############################################################");

        /**
         * catalog 类别名称
         * schema 用户方案名称
         * table 表名
         * 获取指定表的主键信息
         * dbmd.getPrimaryKeys(catalog, schema, table)
         *
         */
        rs = dbmd.getPrimaryKeys("test", null, "user");

        while (rs.next()) {

            /**
             *  所有列信息如下:
             *  TABLE_CAT String => 表类别（可为 null）
             TABLE_SCHEM String => 表模式（可为 null）
             TABLE_NAME String => 表名称
             COLUMN_NAME String => 列名称
             KEY_SEQ short => 主键中的序列号（值 1 表示主键中的第一列，值 2 表示主键中的第二列）。
             PK_NAME String => 主键的名称（可为 null）

             */

            System.out.println(rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + ","
                    + rs.getString(4) + "," + rs.getShort(5) + "," + rs.getString(6));

        }


        System.out.println("##############################################################");

        /**
         * catalog 类别名称
         * schemaPattern 用户方案，模式
         * tableNamePattern 表
         * columnNamePattern 列
         * 获取表的列信息
         * dbmd.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)
         */
        rs = dbmd.getColumns("test", null, "user", null);

        while (rs.next()) {

            /**
             * 所有列如下：
             * TABLE_CAT String => 表类别（可为 null）
             TABLE_SCHEM String => 表模式（可为 null）
             TABLE_NAME String => 表名称
             COLUMN_NAME String => 列名称
             DATA_TYPE int => 来自 java.sql.Types 的 SQL 类型
             TYPE_NAME String => 数据源依赖的类型名称，对于 UDT，该类型名称是完全限定的
             COLUMN_SIZE int => 列的大小。
             BUFFER_LENGTH 未被使用。
             DECIMAL_DIGITS int => 小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 Null。
             NUM_PREC_RADIX int => 基数（通常为 10 或 2）
             NULLABLE int => 是否允许使用 NULL。
             columnNoNulls - 可能不允许使用 NULL 值
             columnNullable - 明确允许使用 NULL 值
             columnNullableUnknown - 不知道是否可使用 null
             REMARKS String => 描述列的注释（可为 null）
             COLUMN_DEF String => 该列的默认值，当值在单引号内时应被解释为一个字符串（可为 null）
             SQL_DATA_TYPE int => 未使用
             SQL_DATETIME_SUB int => 未使用
             CHAR_OCTET_LENGTH int => 对于 char 类型，该长度是列中的最大字节数
             ORDINAL_POSITION int => 表中的列的索引（从 1 开始）
             IS_NULLABLE String => ISO 规则用于确定列是否包括 null。
             YES --- 如果参数可以包括 NULL
             NO --- 如果参数不可以包括 NULL
             空字符串 --- 如果不知道参数是否可以包括 null
             SCOPE_CATLOG String => 表的类别，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
             SCOPE_SCHEMA String => 表的模式，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
             SCOPE_TABLE String => 表名称，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
             SOURCE_DATA_TYPE short => 不同类型或用户生成 Ref 类型、来自 java.sql.Types 的 SQL 类型的源类型（如果 DATA_TYPE 不是 DISTINCT 或用户生成的 REF，则为 null）
             IS_AUTOINCREMENT String => 指示此列是否自动增加
             YES --- 如果该列自动增加
             NO --- 如果该列不自动增加
             空字符串 --- 如果不能确定该列是否是自动增加参数

             */


            System.out.println(rs.getString("COLUMN_NAME") + " 类型=" + rs.getInt("DATA_TYPE") + " 列大小=" + rs.getInt("COLUMN_SIZE") +
                    " 注释=" + rs.getString("REMARKS") +
                    " 是否允许为NULL=" + rs.getInt("NULLABLE"));


            //还有很多很多方法，在这里就不一一列举了

        }


    }

    /**
     * ResultSetMetaData一些用法
     *
     * @throws Exception
     */
    public void getRsInfo(DatasourceVO datasourceVO, String sqlstr) throws Exception {
        ConnectDB connectDB = StringUtil.getEntityBy(datasourceVO);
        Connection connection = connectDB.getConn();
        PreparedStatement pst = connection.prepareStatement(sqlstr);

        ResultSet rs = pst.executeQuery();

        ResultSetMetaData rsmd = rs.getMetaData();//结果集元

        System.out.println("下面这些方法是ResultSetMetaData中方法");

        Integer columnnums = Integer.valueOf(rsmd.getColumnCount());

        System.out.println("获得该ResultSet所有列的数目 " + columnnums);
        for (int i = 1; i < columnnums + 1; i++) {

            System.out.println(i + "列对应数据类型的类 " + rsmd.getColumnClassName(i));

            System.out.println(i + "列在数据库中类型的最大字符个数" + rsmd.getColumnDisplaySize(i));

            System.out.println(i + "列的默认的列的标题" + rsmd.getColumnLabel(i));

          //  System.out.println(i + "列的类型,返回SqlType中的编号 " + rsmd.getColumnType(i));

            System.out.println(i + "列在数据库中的类型，返回类型全名" + rsmd.getColumnTypeName(i));

            System.out.println(i + "列类型的精确度(类型的长度): " + rsmd.getPrecision(i));

            System.out.println(i + "列小数点后的位数 " + rsmd.getScale(i));

         //   System.out.println(i + "列对应的模式的名称（应该用于Oracle） " + rsmd.getSchemaName(i));

           // System.out.println(i + "列对应的表名 " + rsmd.getTableName(i));

            System.out.println(i + "列是否自动递增" + rsmd.isAutoIncrement(i));

            System.out.println(i + "列在数据库中是否为货币型" + rsmd.isCurrency(i));

            System.out.println(i + "列是否为空" + rsmd.isNullable(i));

            System.out.println(i + "列是否为只读" + rsmd.isReadOnly(i));

        //    System.out.println(i + "列能否出现在where中" + rsmd.isSearchable(i));
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
        rs.close();
        pst.close();

    }

}
