package com.gtafe.data.center.information.code.vo;

import java.math.BigDecimal;

/**
 * oracle 用户表属性字段
 */
public class OracleTableVo {

    // table_name	-表名
    private String tableName;
    //tablespace_name	-表空间名
    private String tableSpaceName;
    //  cluster_name	-群集名称
    private String clusterName;
    // iot_name	-IOT（Index Organized Table）索引组织表的名称
    private String iotName;
    //  status		-状态
    private String status;
    // pct_free	-为一个块保留的空间百分比
    private BigDecimal pctFree;
    // pct_used	-一个块的使用水位的百分比
    private BigDecimal pctUsed;
    //  ini_trans	-初始交易的数量
    private BigDecimal iniTrans;
    // max_trans	-交易的最大数量
    private BigDecimal maxTrans;
    //initial_extent	-初始扩展数
    private BigDecimal initialExtent;
    //next_extent	-下一次扩展数
    private BigDecimal nextExtent;
    // min_extents	-最小扩展数
    private BigDecimal minExtents;
    //  max_extents	-最大扩展数
    private BigDecimal maxExtents;
    //pct_increase	-表在做了第一次extent后，下次再扩展时的增量，它是一个百分比值
    private BigDecimal pctIncrease;
    // freelists	-可用列表是表中的一组可插入数据的可用块
    private long freelist;
    //freelist_groups	-列表所属组
    private String freelistGroups;
    //   logging		-是否记录日志
    private String logging;
    //  backed_up	-指示自上次修改表是否已备份（Y）或否（N）的
    private String backedUp;
    // num_rows	-表中的行数
    private long numRows;
    //  blocks		-所使用的数据块数量
    private long blocks;
    //  empty_blocks	-空数据块的数量
    private long emptyBlocks;
    //avg_space	-自由空间的平均量
    private long avgSpace;
    //  chain_cnt	-从一个数据块，或迁移到一个新块链接表中的行数
    private long chainCnt;
    //avg_row_len	-行表中的平均长度
    private long avgRowLen;
    // avg_space_freelist_blocks	-一个freelist上的所有块的平均可用空间
    private long avgSpaceFreelistBlocks;
    // num_freelist_blocks		-空闲列表上的块数量
    private long numFreelistBlocks;
    //degree		-每个实例的线程数量扫描表
    private long degree;
    // instances	-跨表进行扫描的实例数量
    private long instances;
    //  cache		-是否是要在缓冲区高速缓存
    private String cache;
    //  table_lock	-是否启用表锁
    private String tableLock;
    // sample_size	-分析这个表所使用的样本大小
    private String sampleSize;
    //  last_analyzed	-最近分析的日期
    private String lastAnalyzed;
    //   partitioned	-表是否已分区
    private String partitioned;
    //  iot_type	-表是否是索引组织表
    private String iotType;
    //  temporary	-表是否是暂时的
    private String temporary;
    //  secondary	-表是否是次要的对象
    private String secondary;
    //   nested		-是否是一个嵌套表
    private String nested;
    //  buffer_pool	-缓冲池的表
    private String bufferPool;
    //  flash_cache	-智能闪存缓存提示可用于表块
    private String flashCache;
    //   cell_flash_cache	-细胞闪存缓存提示可用于表块
    private String cellFlashCache;
    //row_movement	-是否启用分区行运动
    private String rowMovement;
    //  global_stats	-作为一个整体（全球统计）表的统计的是否准确
    private String globalStats;
    // user_stats	-是否有统计
    private String userStats;
    // skip_corrupt	-是否忽略损坏的块标记在表和索引扫描（ENABLED）状态的或将引发一个错误（已禁用）。
    private String skipCorrupt;
    // duration	-临时表的时间
    private String duration;
    ///   monitoring	-是否有监测属性集
    private String monitoring;
    //cluster_owner	-群集的所有者
    private String clusterOwner;
    //  dependencies	-行依赖性跟踪是否已启用
    private String dependencies;
    //   compression	-是否启用表压缩
    private String compression;
    //   compress_for	-什么样的操作的默认压缩
    private String compressFor;
    // dropped		-是否已经删除并在回收站
    private String dropped;
    //  read_only	-表是否是只读
    private String readOnly;
    //  segment_created	-是否创建表段
    private String segmentCreated;
    //  result_cache	-结果缓存表的模式注释
    private String resultCache;

    @Override
    public String toString() {
        return "OracleTableVo{" +
                "tableName='" + tableName + '\'' +
                ", tableSpaceName='" + tableSpaceName + '\'' +
                ", clusterName='" + clusterName + '\'' +
                ", iotName='" + iotName + '\'' +
                ", status='" + status + '\'' +
                ", pctFree=" + pctFree +
                ", pctUsed=" + pctUsed +
                ", iniTrans=" + iniTrans +
                ", maxTrans=" + maxTrans +
                ", initialExtent=" + initialExtent +
                ", nextExtent=" + nextExtent +
                ", minExtents=" + minExtents +
                ", maxExtents=" + maxExtents +
                ", pctIncrease=" + pctIncrease +
                ", freelist=" + freelist +
                ", freelistGroups='" + freelistGroups + '\'' +
                ", logging='" + logging + '\'' +
                ", backedUp='" + backedUp + '\'' +
                ", numRows=" + numRows +
                ", blocks=" + blocks +
                ", emptyBlocks=" + emptyBlocks +
                ", avgSpace=" + avgSpace +
                ", chainCnt=" + chainCnt +
                ", avgRowLen=" + avgRowLen +
                ", avgSpaceFreelistBlocks=" + avgSpaceFreelistBlocks +
                ", numFreelistBlocks=" + numFreelistBlocks +
                ", degree=" + degree +
                ", instances=" + instances +
                ", cache='" + cache + '\'' +
                ", tableLock='" + tableLock + '\'' +
                ", sampleSize='" + sampleSize + '\'' +
                ", lastAnalyzed='" + lastAnalyzed + '\'' +
                ", partitioned='" + partitioned + '\'' +
                ", iotType='" + iotType + '\'' +
                ", temporary='" + temporary + '\'' +
                ", secondary='" + secondary + '\'' +
                ", nested='" + nested + '\'' +
                ", bufferPool='" + bufferPool + '\'' +
                ", flashCache='" + flashCache + '\'' +
                ", cellFlashCache='" + cellFlashCache + '\'' +
                ", rowMovement='" + rowMovement + '\'' +
                ", globalStats='" + globalStats + '\'' +
                ", userStats='" + userStats + '\'' +
                ", skipCorrupt='" + skipCorrupt + '\'' +
                ", duration='" + duration + '\'' +
                ", monitoring='" + monitoring + '\'' +
                ", clusterOwner='" + clusterOwner + '\'' +
                ", dependencies='" + dependencies + '\'' +
                ", compression='" + compression + '\'' +
                ", compressFor='" + compressFor + '\'' +
                ", dropped='" + dropped + '\'' +
                ", readOnly='" + readOnly + '\'' +
                ", segmentCreated='" + segmentCreated + '\'' +
                ", resultCache='" + resultCache + '\'' +
                '}';
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableSpaceName() {
        return tableSpaceName;
    }

    public void setTableSpaceName(String tableSpaceName) {
        this.tableSpaceName = tableSpaceName;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIotName() {
        return iotName;
    }

    public void setIotName(String iotName) {
        this.iotName = iotName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPctFree() {
        return pctFree;
    }

    public void setPctFree(BigDecimal pctFree) {
        this.pctFree = pctFree;
    }

    public BigDecimal getPctUsed() {
        return pctUsed;
    }

    public void setPctUsed(BigDecimal pctUsed) {
        this.pctUsed = pctUsed;
    }

    public BigDecimal getIniTrans() {
        return iniTrans;
    }

    public void setIniTrans(BigDecimal iniTrans) {
        this.iniTrans = iniTrans;
    }

    public BigDecimal getMaxTrans() {
        return maxTrans;
    }

    public void setMaxTrans(BigDecimal maxTrans) {
        this.maxTrans = maxTrans;
    }

    public BigDecimal getInitialExtent() {
        return initialExtent;
    }

    public void setInitialExtent(BigDecimal initialExtent) {
        this.initialExtent = initialExtent;
    }

    public BigDecimal getNextExtent() {
        return nextExtent;
    }

    public void setNextExtent(BigDecimal nextExtent) {
        this.nextExtent = nextExtent;
    }

    public BigDecimal getMinExtents() {
        return minExtents;
    }

    public void setMinExtents(BigDecimal minExtents) {
        this.minExtents = minExtents;
    }

    public BigDecimal getMaxExtents() {
        return maxExtents;
    }

    public void setMaxExtents(BigDecimal maxExtents) {
        this.maxExtents = maxExtents;
    }

    public BigDecimal getPctIncrease() {
        return pctIncrease;
    }

    public void setPctIncrease(BigDecimal pctIncrease) {
        this.pctIncrease = pctIncrease;
    }

    public long getFreelist() {
        return freelist;
    }

    public void setFreelist(long freelist) {
        this.freelist = freelist;
    }

    public String getFreelistGroups() {
        return freelistGroups;
    }

    public void setFreelistGroups(String freelistGroups) {
        this.freelistGroups = freelistGroups;
    }

    public String getLogging() {
        return logging;
    }

    public void setLogging(String logging) {
        this.logging = logging;
    }

    public String getBackedUp() {
        return backedUp;
    }

    public void setBackedUp(String backedUp) {
        this.backedUp = backedUp;
    }

    public long getNumRows() {
        return numRows;
    }

    public void setNumRows(long numRows) {
        this.numRows = numRows;
    }

    public long getBlocks() {
        return blocks;
    }

    public void setBlocks(long blocks) {
        this.blocks = blocks;
    }

    public long getEmptyBlocks() {
        return emptyBlocks;
    }

    public void setEmptyBlocks(long emptyBlocks) {
        this.emptyBlocks = emptyBlocks;
    }

    public long getAvgSpace() {
        return avgSpace;
    }

    public void setAvgSpace(long avgSpace) {
        this.avgSpace = avgSpace;
    }

    public long getChainCnt() {
        return chainCnt;
    }

    public void setChainCnt(long chainCnt) {
        this.chainCnt = chainCnt;
    }

    public long getAvgRowLen() {
        return avgRowLen;
    }

    public void setAvgRowLen(long avgRowLen) {
        this.avgRowLen = avgRowLen;
    }

    public long getAvgSpaceFreelistBlocks() {
        return avgSpaceFreelistBlocks;
    }

    public void setAvgSpaceFreelistBlocks(long avgSpaceFreelistBlocks) {
        this.avgSpaceFreelistBlocks = avgSpaceFreelistBlocks;
    }

    public long getNumFreelistBlocks() {
        return numFreelistBlocks;
    }

    public void setNumFreelistBlocks(long numFreelistBlocks) {
        this.numFreelistBlocks = numFreelistBlocks;
    }

    public long getDegree() {
        return degree;
    }

    public void setDegree(long degree) {
        this.degree = degree;
    }

    public long getInstances() {
        return instances;
    }

    public void setInstances(long instances) {
        this.instances = instances;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getTableLock() {
        return tableLock;
    }

    public void setTableLock(String tableLock) {
        this.tableLock = tableLock;
    }

    public String getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(String sampleSize) {
        this.sampleSize = sampleSize;
    }

    public String getLastAnalyzed() {
        return lastAnalyzed;
    }

    public void setLastAnalyzed(String lastAnalyzed) {
        this.lastAnalyzed = lastAnalyzed;
    }

    public String getPartitioned() {
        return partitioned;
    }

    public void setPartitioned(String partitioned) {
        this.partitioned = partitioned;
    }

    public String getIotType() {
        return iotType;
    }

    public void setIotType(String iotType) {
        this.iotType = iotType;
    }

    public String getTemporary() {
        return temporary;
    }

    public void setTemporary(String temporary) {
        this.temporary = temporary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getNested() {
        return nested;
    }

    public void setNested(String nested) {
        this.nested = nested;
    }

    public String getBufferPool() {
        return bufferPool;
    }

    public void setBufferPool(String bufferPool) {
        this.bufferPool = bufferPool;
    }

    public String getFlashCache() {
        return flashCache;
    }

    public void setFlashCache(String flashCache) {
        this.flashCache = flashCache;
    }

    public String getCellFlashCache() {
        return cellFlashCache;
    }

    public void setCellFlashCache(String cellFlashCache) {
        this.cellFlashCache = cellFlashCache;
    }

    public String getRowMovement() {
        return rowMovement;
    }

    public void setRowMovement(String rowMovement) {
        this.rowMovement = rowMovement;
    }

    public String getGlobalStats() {
        return globalStats;
    }

    public void setGlobalStats(String globalStats) {
        this.globalStats = globalStats;
    }

    public String getUserStats() {
        return userStats;
    }

    public void setUserStats(String userStats) {
        this.userStats = userStats;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSkipCorrupt() {
        return skipCorrupt;
    }

    public void setSkipCorrupt(String skipCorrupt) {
        this.skipCorrupt = skipCorrupt;
    }

    public String getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(String monitoring) {
        this.monitoring = monitoring;
    }

    public String getClusterOwner() {
        return clusterOwner;
    }

    public void setClusterOwner(String clusterOwner) {
        this.clusterOwner = clusterOwner;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public String getCompression() {
        return compression;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public String getCompressFor() {
        return compressFor;
    }

    public void setCompressFor(String compressFor) {
        this.compressFor = compressFor;
    }

    public String getDropped() {
        return dropped;
    }

    public void setDropped(String dropped) {
        this.dropped = dropped;
    }

    public String getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(String readOnly) {
        this.readOnly = readOnly;
    }

    public String getSegmentCreated() {
        return segmentCreated;
    }

    public void setSegmentCreated(String segmentCreated) {
        this.segmentCreated = segmentCreated;
    }

    public String getResultCache() {
        return resultCache;
    }

    public void setResultCache(String resultCache) {
        this.resultCache = resultCache;
    }
}
