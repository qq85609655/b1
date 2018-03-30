package com.gtafe.data.center.information.code.vo;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 中心库对应物理表的当前信息
 * 详细说明:
 * row_format
 * 若一张表里面不存在varchar、text以及其变形、blob以及其变形的字段的话，那么张这个表其实也叫静态表，即该表的row_format是fixed，就是说每条记录所占用的字节一样。其优点读取快，缺点浪费额外一部分空间。
 * 若一张表里面存在varchar、text以及其变形、blob以及其变形的字段的话，那么张这个表其实也叫动态表，即该表的row_format是dynamic，就是说每条记录所占用的字节是动态的。其优点节省空间，缺点增加读取的时间开销。
 * 所以，做搜索查询量大的表一般都以空间来换取时间，设计成静态表。
 * row_format还有其他一些值：
 * DEFAULT | FIXED | DYNAMIC | COMPRESSED | REDUNDANT | COMPACT
 * 修改行格式
 * ALTER TABLE table_name ROW_FORMAT = DEFAULT
 * 修改过程导致：
 * fixed--->dynamic: 这会导致CHAR变成VARCHAR
 * dynamic--->fixed: 这会导致VARCHAR变成CHAR
 * <p>
 * data_free
 * 每当MySQL从你的列表中删除了一行内容，该段空间就会被留空。而在一段时间内的大量删除操作，会使这种留空的空间变得比存储列表内容所使用的空间更大。
 * 当MySQL对数据进行扫描时，它扫描的对象实际是列表的容量需求上限，也就是数据被写入的区域中处于峰值位置的部分。如果进行新的插入操作，MySQL将尝试利用这些留空的区域，但仍然无法将其彻底占用。
 * 1.查询数据库空间碎片：
 * select table_name,data_free,engine from information_schema.tables where table_schema='yourdatabase';
 * 2.对数据表优化：
 * optimeze table `table_name`;
 */
public class MysqlTableVo {

    /**
     * 数据表登记目录
     */
    private String tableCatalog;

    /**
     * 数据表所属的数据库名
     */
    private String tableSchema;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表类型[system view|base table]
     */
    private String tableType;


    /**
     * 使用的数据库引擎[MyISAM|CSV|InnoDB]
     */
    private String engine;


    /**
     * 版本，默认值10
     */
    private String version;

    /**
     * 行格式[Compact|Dynamic|Fixed]
     */
    private String rowFormat;


    /**
     * 表里所存多少行数据
     */
    private long tableRows;
    /**
     * 平均行长度
     */
    private long avgRowLength;

    /**
     * 数据长度
     */
    private long dataLength;

    /**
     * 最大数据长度
     */
    private long maxDataLength;


    /**
     * 索引长度
     */
    private long indexLength;

    /**
     * 空间碎片
     */
    private long dataFree;

    /**
     * 做自增主键的自动增量当前值
     */
    private long autoIncrement;

    /**
     * 表的检查时间
     */
    private Date checkTime;

    /**
     * 表的字符校验编码集
     */
    private String tableCollation;

    /**
     * 校验和
     */
    private int checkSum;

    /**
     * 创建选项
     */
    private String createOptions;
    /**
     * 表的注释、备注
     */
    private String tableComment;

    /**
     * 表的创建时间
     */
    private Date createTime;
    private String createTime_;

    /**
     * 表的更新时间
     */
    private Date updateTime;
    private String updateTime_;

    public String getCreateTime_() {
        return createTime_;
    }

    public void setCreateTime_(String createTime_) {
        this.createTime_ = createTime_;
    }

    public String getUpdateTime_() {
        return updateTime_;
    }

    public void setUpdateTime_(String updateTime_) {
        this.updateTime_ = updateTime_;
    }

    @Override
    public String toString() {
        return "MysqlTableVo{" +
                "tableCatalog='" + tableCatalog + '\'' +
                ", tableSchema='" + tableSchema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableType='" + tableType + '\'' +
                ", engine='" + engine + '\'' +
                ", version='" + version + '\'' +
                ", rowFormat='" + rowFormat + '\'' +
                ", tableRows=" + tableRows +
                ", avgRowLength=" + avgRowLength +
                ", dataLength=" + dataLength +
                ", maxDataLength=" + maxDataLength +
                ", indexLength=" + indexLength +
                ", dataFree=" + dataFree +
                ", autoIncrement=" + autoIncrement +
                ", checkTime=" + checkTime +
                ", tableCollation='" + tableCollation + '\'' +
                ", checkSum=" + checkSum +
                ", createOptions='" + createOptions + '\'' +
                ", tableComment='" + tableComment + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRowFormat() {
        return rowFormat;
    }

    public void setRowFormat(String rowFormat) {
        this.rowFormat = rowFormat;
    }

    public long getTableRows() {
        return tableRows;
    }

    public void setTableRows(long tableRows) {
        this.tableRows = tableRows;
    }

    public long getAvgRowLength() {
        return avgRowLength;
    }

    public void setAvgRowLength(long avgRowLength) {
        this.avgRowLength = avgRowLength;
    }

    public long getDataLength() {
        return dataLength;
    }

    public void setDataLength(long dataLength) {
        this.dataLength = dataLength;
    }

    public long getMaxDataLength() {
        return maxDataLength;
    }

    public void setMaxDataLength(long maxDataLength) {
        this.maxDataLength = maxDataLength;
    }

    public long getIndexLength() {
        return indexLength;
    }

    public void setIndexLength(long indexLength) {
        this.indexLength = indexLength;
    }

    public long getDataFree() {
        return dataFree;
    }

    public void setDataFree(long dataFree) {
        this.dataFree = dataFree;
    }

    public long getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(long autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTableCollation() {
        return tableCollation;
    }

    public void setTableCollation(String tableCollation) {
        this.tableCollation = tableCollation;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public String getCreateOptions() {
        return createOptions;
    }

    public void setCreateOptions(String createOptions) {
        this.createOptions = createOptions;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }





}
