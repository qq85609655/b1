package com.gtafe.data.center.dlms.a;

public class Man  implements IBank1 {
    private String name;
    public Man(String name){
        this.name=name;
    }
    @Override
    public void applyBank() {
        System.out.println(name+"申请办卡");
    }
}
