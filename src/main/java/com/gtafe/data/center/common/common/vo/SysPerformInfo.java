package com.gtafe.data.center.common.common.vo;

public class SysPerformInfo {
    private String id;
    private float cpuRate;
    private float memoryRate;
    private int threadCount;
    private float upSpeed;
    private float downSpeed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getCpuRate() {
        return cpuRate;
    }

    public void setCpuRate(float cpuRate) {
        this.cpuRate = cpuRate;
    }

    public float getMemoryRate() {
        return memoryRate;
    }

    public void setMemoryRate(float memoryRate) {
        this.memoryRate = memoryRate;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public float getUpSpeed() {
        return upSpeed;
    }

    public void setUpSpeed(float upSpeed) {
        this.upSpeed = upSpeed;
    }

    public float getDownSpeed() {
        return downSpeed;
    }

    public void setDownSpeed(float downSpeed) {
        this.downSpeed = downSpeed;
    }
}
