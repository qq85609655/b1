package com.gtafe.data.center.common.common.vo;

import java.util.List;

import com.gtafe.data.center.runadmin.nodewatch.vo.EtlTaskStatus;
import com.gtafe.data.center.runadmin.nodewatch.vo.NodeWatchVo;

/**
 * 服务器 参数 对象
 */
public class IndexVo {
    private int a1;
    private int a2;
    private int a3;
    private int a4;
    private int a5;
    private int a6;

    private int totalCounts;

    private int errorCounts;
    
    private List<NodeWatchVo> nodeWatchVos;

    private List<EtlTaskStatus> etlTaskStatuses;

    public int getA1() {
        return a1;
    }

    public void setA1(int a1) {
        this.a1 = a1;
    }

    public int getA2() {
        return a2;
    }

    public void setA2(int a2) {
        this.a2 = a2;
    }

    public int getA3() {
        return a3;
    }

    public void setA3(int a3) {
        this.a3 = a3;
    }

    public int getA4() {
        return a4;
    }

    public void setA4(int a4) {
        this.a4 = a4;
    }

    public int getA5() {
        return a5;
    }

    public void setA5(int a5) {
        this.a5 = a5;
    }

    public int getA6() {
        return a6;
    }

    public void setA6(int a6) {
        this.a6 = a6;
    }

    public int getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(int totalCounts) {
        this.totalCounts = totalCounts;
    }

    public int getErrorCounts() {
        return errorCounts;
    }

    public void setErrorCounts(int errorCounts) {
        this.errorCounts = errorCounts;
    }

    public List<NodeWatchVo> getNodeWatchVos() {
        return nodeWatchVos;
    }

    public void setNodeWatchVos(List<NodeWatchVo> nodeWatchVos) {
        this.nodeWatchVos = nodeWatchVos;
    }

    @Override
    public String toString() {
        return "IndexVo{" +
                "a1=" + a1 +
                ", a2=" + a2 +
                ", a3=" + a3 +
                ", a4=" + a4 +
                ", a5=" + a5 +
                ", a6=" + a6 +
                ", totalCounts=" + totalCounts +
                ", errorCounts=" + errorCounts +
                ", nodeWatchVos=" + nodeWatchVos +
                '}';
    }

    public List<EtlTaskStatus> getEtlTaskStatuses() {
        return etlTaskStatuses;
    }

    public void setEtlTaskStatuses(List<EtlTaskStatus> etlTaskStatuses) {
        this.etlTaskStatuses = etlTaskStatuses;
    }
}
