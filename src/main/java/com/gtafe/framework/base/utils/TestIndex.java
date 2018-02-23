package com.gtafe.framework.base.utils;

public class TestIndex {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("11111");

        float cpus=  CpuUsage.getInstance().get();
        float memery=MemUsage.getInstance().get();

        System.out.println(cpus);
        System.out.println(memery);
    }
}
