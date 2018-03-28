package com.gtafe.data.center.dlms.a;

public class TestA {
    public static void main(String[] args) {
        Man man = new Man("Peakmain");
        BankWorker worker = new BankWorker(man);
        worker.applyBank();
    }
}
