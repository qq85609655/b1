package com.gtafe.data.center.dlms.a;

public class BankWorker implements IBank {
    private Man man;
    public BankWorker(Man man){
        this.man=man;
    }
    @Override
    public void applyBank() {
        System.out.println("开始受理");
        man.applyBank();
        System.out.println("操作完毕");
    }
}
