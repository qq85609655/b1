package com.gtafe.data.center.dlms.b;

public class Man implements IBank {
    private String name;
    public Man(String name){
        this.name=name;
    }
    @Override
    public void applyBank() {
        System.out.println(name+"申请办卡");
    }

    @Override
    public void lostBank() {
        System.out.println(name+"挂失卡");
    }
}
